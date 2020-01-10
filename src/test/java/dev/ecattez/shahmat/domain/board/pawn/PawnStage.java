package dev.ecattez.shahmat.domain.board.pawn;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.BeforeStage;
import dev.ecattez.shahmat.board.ImpossibleToMove;
import dev.ecattez.shahmat.board.ChessGame;
import dev.ecattez.shahmat.board.Direction;
import dev.ecattez.shahmat.board.OutsideSquare;
import dev.ecattez.shahmat.board.Piece;
import dev.ecattez.shahmat.board.PieceBox;
import dev.ecattez.shahmat.board.PieceColor;
import dev.ecattez.shahmat.board.PieceFactory;
import dev.ecattez.shahmat.board.PieceType;
import dev.ecattez.shahmat.board.RulesViolation;
import dev.ecattez.shahmat.board.Square;
import dev.ecattez.shahmat.board.TradeRefused;
import dev.ecattez.shahmat.command.MovePiece;
import dev.ecattez.shahmat.command.TradePawn;
import dev.ecattez.shahmat.event.BoardEvent;
import dev.ecattez.shahmat.event.PawnTraded;
import dev.ecattez.shahmat.event.PieceCaptured;
import dev.ecattez.shahmat.event.PieceMoved;
import dev.ecattez.shahmat.event.PiecePositioned;
import dev.ecattez.shahmat.event.TradeProposed;
import org.assertj.core.api.Assertions;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.mock;

public class PawnStage extends Stage<PawnStage> {

    private PieceFactory pieceFactory;
    private Piece pawn;
    private Piece opponentPiece;
    private Square from;
    private Square to;
    private List<BoardEvent> returnedEvents;
    private List<BoardEvent> history;
    private RulesViolation violation;

    @BeforeStage
    public void init() {
        this.pieceFactory = new PieceBox();
        this.history = new LinkedList<>();
    }

    public PawnStage a_$_pawn_in_$(String color, String from) {
        this.from = new Square(from);
        this.pawn = pieceFactory.createPiece(
            PieceType.PAWN,
            PieceColor.valueOf(color)
        );
        this.history.add(
            new PiecePositioned(
                pawn,
                this.from
            )
        );
        return self();
    }

    public PawnStage a_pawn_in_the_other_side_of_the_chess_board() {
        return a_$_pawn_in_$("BLACK", "A1");
    }

    public PawnStage $_is_vacant(String location) {
        return self();
    }

    public PawnStage $_is_not_vacant(String location) {
        history.add(
            new PiecePositioned(
                mock(Piece.class),
                new Square(location)
            )
        );
        return self();
    }

    public PawnStage an_opponent_piece_in_$(String opponentLocation) {
        this.to = new Square(opponentLocation);
        this.opponentPiece = pieceFactory.createPiece(
            PieceType.PAWN,
            pawn.color.opposite()
        );
        history.add(
            new PiecePositioned(
                opponentPiece,
                to
            )
        );
        return self();
    }

    public PawnStage the_pawn_is_moved_forward() {
        this.to = from.neighbour(Direction.FORWARD, pawn.orientation()).orElseThrow(OutsideSquare::new);
        try {
            this.returnedEvents = ChessGame.move(
                Collections.unmodifiableList(history),
                new MovePiece(
                    pawn,
                    from,
                    to
                )
            );
        } catch (RulesViolation e) {
            violation = e;
        }
        return self();
    }

    public PawnStage the_pawn_is_moved_forward_two_squares() {
        this.to = from
            .neighbour(Direction.FORWARD, pawn.orientation())
            .flatMap(forwardSquare -> forwardSquare.neighbour(Direction.FORWARD, pawn.orientation()))
            .orElseThrow(OutsideSquare::new);
        try {
            this.returnedEvents = ChessGame.move(
                Collections.unmodifiableList(history),
                new MovePiece(
                    pawn,
                    from,
                    to
                )
            );
        } catch (RulesViolation e) {
            violation = e;
        }
        return self();
    }

    public PawnStage the_opponent_piece_is_captured_by_the_pawn() {
        this.returnedEvents = ChessGame.move(
            Collections.unmodifiableList(history),
            new MovePiece(
                pawn,
                from,
                to
            )
        );
        return self();
    }

    public PawnStage the_pawn_is_traded_for(String type) {
        this.returnedEvents = ChessGame.trade(
            Collections.unmodifiableList(history),
            new TradePawn(
                from,
                PieceType.valueOf(type)
            )
        );
        return self();
    }

    public PawnStage the_pawn_is_traded_for_a_king() {
        try {
            this.returnedEvents = ChessGame.trade(
                Collections.unmodifiableList(history),
                new TradePawn(
                    from,
                    PieceType.KING
                )
            );
        } catch (RulesViolation e) {
            violation = e;
        }
        return self();
    }

    public PawnStage the_pawn_is_in_$(String to) {
        Assertions
            .assertThat(returnedEvents)
            .contains(
                new PieceMoved(
                    pawn,
                    from,
                    new Square(to)
                )
            );
        return self();
    }

    public PawnStage the_pawn_stays_in_$(String location) {
        Assertions
            .assertThat(violation)
            .isInstanceOf(ImpossibleToMove.class);
        return self();
    }

    public PawnStage the_opponent_piece_is_removed_from_the_game() {
        Assertions
            .assertThat(returnedEvents)
            .contains(
                new PieceCaptured(
                    opponentPiece,
                    pawn
                )
            );
        return self();
    }

    public PawnStage a_trade_is_proposed_in_$(String to) {
        Assertions
            .assertThat(returnedEvents)
            .contains(
                new TradeProposed(
                    new Square(to)
                )
            );
        return self();
    }

    public PawnStage the_trade_is_refused() {
        Assertions
            .assertThat(violation)
            .isInstanceOf(TradeRefused.class);
        return self();
    }

    public PawnStage a_$_replace_the_pawn(String type) {
        Assertions
            .assertThat(returnedEvents)
            .contains(
                new PawnTraded(
                    from,
                    pieceFactory.createPiece(PieceType.valueOf(type), pawn.color)
                )
            );
        return self();
    }

}