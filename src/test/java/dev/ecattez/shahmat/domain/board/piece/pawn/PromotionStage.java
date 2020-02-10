package dev.ecattez.shahmat.domain.board.piece.pawn;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.AfterScenario;
import com.tngtech.jgiven.annotation.BeforeStage;
import dev.ecattez.shahmat.domain.board.BeforeAfterOutput;
import dev.ecattez.shahmat.domain.board.Direction;
import dev.ecattez.shahmat.domain.board.piece.Piece;
import dev.ecattez.shahmat.domain.board.piece.PieceBox;
import dev.ecattez.shahmat.domain.board.piece.PieceColor;
import dev.ecattez.shahmat.domain.board.piece.PieceFactory;
import dev.ecattez.shahmat.domain.board.piece.PieceType;
import dev.ecattez.shahmat.domain.board.square.Square;
import dev.ecattez.shahmat.domain.board.violation.PromotionMustBeDone;
import dev.ecattez.shahmat.domain.board.violation.PromotionRefused;
import dev.ecattez.shahmat.domain.board.violation.RulesViolation;
import dev.ecattez.shahmat.domain.command.Move;
import dev.ecattez.shahmat.domain.command.Promote;
import dev.ecattez.shahmat.domain.event.ChessEvent;
import dev.ecattez.shahmat.domain.event.PawnPromoted;
import dev.ecattez.shahmat.domain.event.PieceMoved;
import dev.ecattez.shahmat.domain.event.PiecePositioned;
import dev.ecattez.shahmat.domain.event.PromotionProposed;
import dev.ecattez.shahmat.domain.event.TurnChanged;
import dev.ecattez.shahmat.domain.game.ChessGame;
import org.assertj.core.api.Assertions;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class PromotionStage extends Stage<PromotionStage> {

    private Piece pawn;
    private Square from;
    private Square to;

    private PieceFactory pieceFactory;
    private List<ChessEvent> returnedEvents;
    private List<ChessEvent> history;
    private RulesViolation violation;

    @BeforeStage
    public void init() {
        this.pieceFactory = PieceBox.getInstance();
        this.history = new LinkedList<>();
        this.returnedEvents = new LinkedList<>();
    }

    @AfterScenario
    public void after() {
        BeforeAfterOutput.display(
            pawn,
            history,
            returnedEvents,
            violation
        );
    }

    public PromotionStage a_$_pawn_in_$(String color, String from) {
        this.from = new Square(from);
        this.pawn = pieceFactory.createPiece(
            PieceType.PAWN,
            PieceColor.valueOf(color)
        );
        history.addAll(
            List.of(
                new PiecePositioned(
                    pawn,
                    this.from
                ),
                new TurnChanged(
                    pawn.color()
                )
            )
        );
        return self();
    }

    public PromotionStage a_pawn_in_the_other_side_of_the_chess_board() {
        this.from = new Square("A1");
        this.pawn = pieceFactory.createPiece(
            PieceType.PAWN,
            PieceColor.valueOf("BLACK")
        );
        history.addAll(
            List.of(
                new PieceMoved(
                    pawn,
                    new Square("A2"),
                    from
                ),
                new PromotionProposed(
                    from
                )
            )
        );
        return self();
    }

    public PromotionStage a_pawn_not_in_the_other_side_of_the_chess_board() {
        return a_$_pawn_in_$("BLACK", "A2");
    }

    public PromotionStage the_promotion_is_proposed() {
        history.add(
            new PromotionProposed(
                from
            )
        );
        return self();
    }

    public PromotionStage the_pawn_is_moved_forward() {
        this.to = from.getNeighbour(
            Direction.FORWARD,
            pawn.orientation()
        );
        try {
            this.returnedEvents = ChessGame.move(
                Collections.unmodifiableList(history),
                new Move(
                    PieceType.PAWN,
                    from,
                    to
                )
            );
        } catch (RulesViolation e) {
            violation = e;
        }
        return self();
    }

    public PromotionStage the_pawn_is_promoted_to_a_$(String promotionType) {
        try {
            this.returnedEvents = ChessGame.promote(
                Collections.unmodifiableList(history),
                new Promote(
                    from,
                    PieceType.valueOf(promotionType)
                )
            );
        } catch (RulesViolation e) {
            violation = e;
        }
        return self();
    }

    public PromotionStage the_pawn_is_promoted_to_a_valid_piece() {
        return the_pawn_is_promoted_to_a_$("QUEEN");
    }

    public PromotionStage an_other_piece_is_moved() {
        history.add(
            new PiecePositioned(
                pieceFactory.createPiece(PieceType.PAWN, PieceColor.WHITE),
                new Square("C4")
            )
        );

        try {
            this.returnedEvents = ChessGame.move(
                Collections.unmodifiableList(history),
                new Move(
                    PieceType.PAWN,
                    new Square("C4"),
                    new Square("C5")
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
                    PieceType.valueOf(type)
                )
            );
        return self();
    }

    public PromotionStage the_move_is_refused() {
        Assertions
            .assertThat(violation)
            .isInstanceOf(PromotionMustBeDone.class);
        return self();
    }

    public PromotionStage $_as_reason(String reason) {
        Assertions
            .assertThat(violation.getMessage())
            .isEqualTo(reason);
        return self();
    }
}
