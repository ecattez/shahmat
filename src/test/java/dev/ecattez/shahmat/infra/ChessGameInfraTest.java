package dev.ecattez.shahmat.infra;

import dev.ecattez.shahmat.domain.board.piece.PieceBox;
import dev.ecattez.shahmat.domain.board.piece.PieceColor;
import dev.ecattez.shahmat.domain.board.piece.PieceType;
import dev.ecattez.shahmat.domain.board.square.Square;
import dev.ecattez.shahmat.domain.command.InitBoard;
import dev.ecattez.shahmat.domain.command.Move;
import dev.ecattez.shahmat.domain.command.Promote;
import dev.ecattez.shahmat.domain.event.ChessEvent;
import dev.ecattez.shahmat.domain.event.PawnPromoted;
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

        initBoard(pubSub, eventStore, chessGameId);

        Assertions
            .assertThat(eventStore.history(chessGameId))
            .hasSize(33);
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

        initBoard(pubSub, eventStore, chessGameId);
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

        initBoard(pubSub, eventStore, chessGameId);

        String previous = "a2";
        for (String location: List.of("a4", "a5", "a6", "b7", "a8")) {
            move(pubSub, eventStore, chessGameId, PieceType.PAWN, new Square(previous), new Square(location));
            previous = location;
        }

        promote(pubSub, eventStore, chessGameId, new Square("a8"), PieceType.QUEEN);


        Assertions
            .assertThat(eventStore.history(chessGameId))
            .containsOnlyOnce(
                new PawnPromoted(
                    new Square("a8"),
                    PieceType.QUEEN
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

        initBoard(pubSub, eventStore, chessGameId);

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

    private void initBoard(PubSub pubSub, EventStore eventStore, ChessGameId chessGameId) {
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

    private void move(PubSub pubSub, EventStore eventStore, ChessGameId chessGameId, PieceType pieceType, Square from, Square to) {
        List<ChessEvent> history = eventStore.history(chessGameId);

        pubSub.dispatch(
            chessGameId,
            (long) history.size(),
            ChessGame.move(
                history,
                new Move(
                    pieceType,
                    from,
                    to
                )
            )
        );
    }

    private void promote(PubSub pubSub, EventStore eventStore, ChessGameId chessGameId, Square location, PieceType promotedTo) {
        List<ChessEvent> history = eventStore.history(chessGameId);

        pubSub.dispatch(
            chessGameId,
            (long) history.size(),
            ChessGame.promote(
                history,
                new Promote(
                    location,
                    promotedTo
                )
            )
        );
    }

}
