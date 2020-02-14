package dev.ecattez.shahmat.infra;

import dev.ecattez.shahmat.domain.board.Direction;
import dev.ecattez.shahmat.domain.board.Orientation;
import dev.ecattez.shahmat.domain.board.piece.PieceBox;
import dev.ecattez.shahmat.domain.board.piece.PieceColor;
import dev.ecattez.shahmat.domain.board.piece.PieceType;
import dev.ecattez.shahmat.domain.board.square.Square;
import dev.ecattez.shahmat.domain.command.InitBoard;
import dev.ecattez.shahmat.domain.command.Move;
import dev.ecattez.shahmat.domain.event.BoardInitialized;
import dev.ecattez.shahmat.domain.event.ChessEvent;
import dev.ecattez.shahmat.domain.event.PawnPromoted;
import dev.ecattez.shahmat.domain.event.PieceCaptured;
import dev.ecattez.shahmat.domain.event.PieceMoved;
import dev.ecattez.shahmat.domain.game.ChessGame;
import dev.ecattez.shahmat.domain.game.GameType;
import dev.ecattez.shahmat.infra.aggregate.ChessGameId;
import dev.ecattez.shahmat.infra.handler.EventLogger;
import dev.ecattez.shahmat.infra.publisher.ChessGamePubSub;
import dev.ecattez.shahmat.infra.publisher.PubSub;
import dev.ecattez.shahmat.infra.store.ChessGameEventStore;
import dev.ecattez.shahmat.infra.store.EventStore;
import dev.ecattez.shahmat.infra.store.SequenceAlreadyExists;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.annotation.Nullable;
import java.util.List;

public class ChessGameInfraTest {

    @Test
    public void dispatch_init_board() {
        EventStore eventStore = new ChessGameEventStore();
        PubSub pubSub = new ChessGamePubSub(
            eventStore,
            List.of(
                new EventLogger()
            )
        );

        ChessGameId chessGameId = ChessGameId.newInstance();

        startGame(pubSub, eventStore, chessGameId);

        Assertions
            .assertThat(eventStore.history(chessGameId))
            .containsOnlyOnce(
                new BoardInitialized(GameType.CLASSICAL)
            );
        ;
    }

    @Test
    public void dispatch_move_piece_on_board() {
        EventStore eventStore = new ChessGameEventStore();
        PubSub pubSub = new ChessGamePubSub(
            eventStore,
            List.of(
                new EventLogger()
            )
        );

        ChessGameId chessGameId = ChessGameId.newInstance();

        startGame(pubSub, eventStore, chessGameId);
        move(pubSub, eventStore, chessGameId, PieceType.PAWN, new Square("a2"), new Square("a3"));


        Assertions
            .assertThat(eventStore.history(chessGameId))
            .containsOnlyOnce(
                new PieceMoved(
                    PieceBox.getInstance().createPiece(
                        PieceType.PAWN,
                        PieceColor.WHITE
                    ),
                    new Square("a2"),
                    new Square("a3")
                )
            )
        ;
    }

    @Test
    public void dispatch_promote_piece_on_board() {
        EventStore eventStore = new ChessGameEventStore();
        PubSub pubSub = new ChessGamePubSub(
            eventStore,
            List.of(
                new EventLogger()
            )
        );

        ChessGameId chessGameId = ChessGameId.newInstance();

        startGame(pubSub, eventStore, chessGameId);

        List<MovePair> moveSeries = List.of(
            new MovePair("a3", "h6"),
            new MovePair("a4", "h5"),
            new MovePair("a5", "h4"),
            new MovePair("a6", "h3"),
            new MovePair("b7", "g2")
        );

        for (int i = 0; i < moveSeries.size(); i++) {
            MovePair pair = moveSeries.get(i);
            MovePair before = i - 1 >= 0
                ? moveSeries.get(i - 1)
                : new MovePair(
                    new Square(pair.whiteMove).getNeighbour(Direction.BACKWARD, Orientation.UPWARD).toString(),
                    new Square(pair.blackMove).getNeighbour(Direction.BACKWARD, Orientation.DOWNWARD).toString()
                );

            Square whiteMove = new Square(pair.whiteMove);
            Square blackMove = new Square(pair.blackMove);

            move(pubSub, eventStore, chessGameId, PieceType.PAWN, new Square(before.whiteMove), whiteMove);
            move(pubSub, eventStore, chessGameId, PieceType.PAWN, new Square(before.blackMove), blackMove);
        }

        move(pubSub, eventStore, chessGameId, PieceType.PAWN, new Square("b7"), new Square("a8"), PieceType.QUEEN);

        Assertions
            .assertThat(eventStore.history(chessGameId))
            .containsOnlyOnce(
                new PawnPromoted(
                    new PieceCaptured(
                        PieceBox.getInstance()
                            .createPiece(PieceType.ROOK, PieceColor.BLACK),
                        new Square("a8"),
                        PieceBox.getInstance()
                            .createPiece(PieceType.PAWN, PieceColor.WHITE),
                        new Square("b7")
                    ),
                    PieceBox.getInstance()
                        .createPiece(PieceType.QUEEN, PieceColor.WHITE)
                )
            )
        ;
    }

    @Test
    public void dispatch_with_existing_sequence_must_fails() {
        EventStore eventStore = new ChessGameEventStore();
        PubSub pubSub = new ChessGamePubSub(
            eventStore,
            List.of(
                new EventLogger()
            )
        );

        ChessGameId chessGameId = ChessGameId.newInstance();
        long existingSequence = eventStore.history(chessGameId).size();

        startGame(pubSub, eventStore, chessGameId);

        Exception violation = null;
        try {
            pubSub.dispatch(
                chessGameId,
                existingSequence,
                ChessGame.move(
                    eventStore.history(chessGameId),
                    new Move(
                        PieceType.PAWN,
                        new Square("a2"),
                        new Square("a3")
                    )
                )
            );
        } catch (Exception e) {
            violation = e;
        }

        Assertions
            .assertThat(violation)
            .isInstanceOf(SequenceAlreadyExists.class);
    }

    private void startGame(PubSub pubSub, EventStore eventStore, ChessGameId chessGameId) {
        List<ChessEvent> history = eventStore.history(chessGameId);

        pubSub.dispatch(
            chessGameId,
            (long) history.size(),
            ChessGame.initalizeBoard(
                history,
                new InitBoard(
                    GameType.CLASSICAL
                )
            )
        );
    }

    private void move(
        PubSub pubSub,
        EventStore eventStore,
        ChessGameId chessGameId,
        PieceType pieceType,
        Square from,
        Square to
    ) {
        move(pubSub, eventStore, chessGameId, pieceType, from, to, null);
    }

    private void move(
        PubSub pubSub,
        EventStore eventStore,
        ChessGameId chessGameId,
        PieceType pieceType,
        Square from,
        Square to,
        @Nullable PieceType promotedTo
    ) {
        List<ChessEvent> history = eventStore.history(chessGameId);

        pubSub.dispatch(
            chessGameId,
            (long) history.size(),
            ChessGame.move(
                history,
                new Move(
                    pieceType,
                    from,
                    to,
                    promotedTo
                )
            )
        );
    }

}
