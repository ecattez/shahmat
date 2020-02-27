package dev.ecattez.shahmat.domain.board.turn;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.BeforeStage;
import dev.ecattez.shahmat.domain.board.Direction;
import dev.ecattez.shahmat.domain.board.piece.Piece;
import dev.ecattez.shahmat.domain.board.piece.PieceBox;
import dev.ecattez.shahmat.domain.board.piece.PieceColor;
import dev.ecattez.shahmat.domain.board.piece.PieceFactory;
import dev.ecattez.shahmat.domain.board.piece.PieceType;
import dev.ecattez.shahmat.domain.board.square.Square;
import dev.ecattez.shahmat.domain.board.violation.PieceNotOwned;
import dev.ecattez.shahmat.domain.board.violation.RulesViolation;
import dev.ecattez.shahmat.domain.command.InitBoard;
import dev.ecattez.shahmat.domain.command.Move;
import dev.ecattez.shahmat.domain.event.ChessEvent;
import dev.ecattez.shahmat.domain.event.PiecePositioned;
import dev.ecattez.shahmat.domain.event.TurnChanged;
import dev.ecattez.shahmat.domain.game.ChessGame;
import dev.ecattez.shahmat.domain.game.GameType;
import org.assertj.core.api.Assertions;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class TurnStage extends Stage<TurnStage> {

    private PieceFactory pieceFactory;
    private List<ChessEvent> returnedEvents;
    private List<ChessEvent> history;
    private RulesViolation violation;

    private Square from;
    private Square promotionLocation;

    @BeforeStage
    public void init() {
        this.pieceFactory = PieceBox.getInstance();
        this.history = new LinkedList<>();
        this.returnedEvents = new LinkedList<>();
    }

    public TurnStage a_ready_to_play_chess_game() {
        history.clear();
        a_chess_game_starts();
        history.addAll(returnedEvents);
        return self();
    }

    public TurnStage a_chess_game_starts() {
        try {
            returnedEvents = ChessGame.initalizeBoard(
                history,
                new InitBoard(GameType.CLASSICAL)
            );
        } catch (RulesViolation e) {
            violation = e;
        }
        return self();
    }

    public TurnStage $_is_playing(String color) {
        history.add(
            new TurnChanged(
                PieceColor.valueOf(color)
            )
        );
        return self();
    }

    public TurnStage a_$_pawn_in_other_side_of_the_chess_board(String color) {
        PieceColor playerColor = PieceColor.valueOf(color);

        from = PieceColor.WHITE.equals(playerColor)
            ? new Square("a7")
            : new Square("a2");

        Piece playerPiece = pieceFactory.createPiece(PieceType.PAWN, playerColor);

        Square to = from.getNeighbour(Direction.FORWARD, playerPiece.orientation());

        promotionLocation = to;

        history.addAll(
            List.of(
                new PiecePositioned(
                    playerPiece,
                    from
                )
            )
        );
        return self();
    }

    public TurnStage $_moves_one_of_its_piece(String currentPlayer) {
        PieceColor playerColor = PieceColor.valueOf(currentPlayer);
        Piece piece = pieceFactory.createPiece(PieceType.PAWN, playerColor);
        Square from = new Square("a5");

        history.addAll(
            List.of(
                new PiecePositioned(
                    piece,
                    from
                )
            )
        );

        try {
            returnedEvents = ChessGame.move(
                Collections.unmodifiableList(history),
                new Move(
                    PieceType.PAWN,
                    from,
                    from.getNeighbour(Direction.FORWARD, piece.orientation())
                )
            );
        } catch (RulesViolation e) {
            violation = e;
        }
        return self();
    }

    public TurnStage $_moves_a_$_piece(String color, String opponentColor) {
        Piece piece = pieceFactory.createPiece(PieceType.PAWN, PieceColor.valueOf(opponentColor));
        Square from = new Square("a5");

        history.addAll(
            List.of(
                new PiecePositioned(
                    piece,
                    from
                )
            )
        );

        try {
            returnedEvents = ChessGame.move(
                Collections.unmodifiableList(history),
                new Move(
                    PieceType.PAWN,
                    from,
                    from.getNeighbour(Direction.FORWARD, piece.orientation())
                )
            );
        } catch (RulesViolation e) {
            violation = e;
        }
        return self();
    }

    public TurnStage it_is_the_turn_of_$(String color) {
        Assertions
            .assertThat(returnedEvents)
            .endsWith(
                new TurnChanged(PieceColor.valueOf(color))
            );
        return self();
    }

    public TurnStage the_move_is_refused() {
        Assertions
            .assertThat(violation)
            .isInstanceOf(PieceNotOwned.class);
        return self();
    }

}
