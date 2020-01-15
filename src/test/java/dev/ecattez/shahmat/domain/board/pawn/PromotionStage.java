package dev.ecattez.shahmat.domain.board.pawn;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.BeforeStage;
import dev.ecattez.shahmat.board.ChessGame;
import dev.ecattez.shahmat.board.Direction;
import dev.ecattez.shahmat.board.OutsideSquare;
import dev.ecattez.shahmat.board.Piece;
import dev.ecattez.shahmat.board.PieceBox;
import dev.ecattez.shahmat.board.PieceColor;
import dev.ecattez.shahmat.board.PieceFactory;
import dev.ecattez.shahmat.board.PieceType;
import dev.ecattez.shahmat.board.PromotionRefused;
import dev.ecattez.shahmat.board.RulesViolation;
import dev.ecattez.shahmat.board.Square;
import dev.ecattez.shahmat.command.Move;
import dev.ecattez.shahmat.command.Promote;
import dev.ecattez.shahmat.event.BoardEvent;
import dev.ecattez.shahmat.event.PawnPromoted;
import dev.ecattez.shahmat.event.PiecePositioned;
import dev.ecattez.shahmat.event.PromotionProposed;
import org.assertj.core.api.Assertions;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.mock;

public class PromotionStage extends Stage<PromotionStage> {

    private PieceFactory pieceFactory;
    private Piece pawn;
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

    public PromotionStage a_$_pawn_in_$(String color, String from) {
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

    public PromotionStage a_pawn_in_the_other_side_of_the_chess_board() {
        return a_$_pawn_in_$("BLACK", "A1");
    }

    public PromotionStage the_pawn_is_moved_forward() {
        this.to = from.neighbour(Direction.FORWARD, pawn.orientation()).orElseThrow(OutsideSquare::new);
        try {
            this.returnedEvents = ChessGame.move(
                Collections.unmodifiableList(history),
                new Move(
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

    public PromotionStage the_pawn_is_promoted_for_a_$(String pieceType) {
        try {
            this.returnedEvents = ChessGame.promote(
                Collections.unmodifiableList(history),
                new Promote(
                    from,
                    PieceType.valueOf(pieceType)
                )
            );
        } catch (RulesViolation e) {
            violation = e;
        }
        return self();
    }

    public PromotionStage a_promotion_is_proposed_in_$(String to) {
        Assertions
            .assertThat(returnedEvents)
            .contains(
                new PromotionProposed(
                    new Square(to)
                )
            );
        return self();
    }

    public PromotionStage the_promotion_is_refused() {
        Assertions
            .assertThat(violation)
            .isInstanceOf(PromotionRefused.class);
        return self();
    }

    public PromotionStage a_$_replaces_the_pawn(String type) {
        Assertions
            .assertThat(returnedEvents)
            .contains(
                new PawnPromoted(
                    from,
                    pieceFactory.createPiece(PieceType.valueOf(type), pawn.color)
                )
            );
        return self();
    }

}