package dev.ecattez.shahmat.domain.board.piece.pawn;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.AfterScenario;
import com.tngtech.jgiven.annotation.BeforeStage;
import dev.ecattez.shahmat.domain.board.BeforeAfterOutput;
import dev.ecattez.shahmat.domain.board.Board;
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
import dev.ecattez.shahmat.domain.event.ChessEvent;
import dev.ecattez.shahmat.domain.event.PawnPromoted;
import dev.ecattez.shahmat.domain.event.PieceCaptured;
import dev.ecattez.shahmat.domain.event.PiecePositioned;
import dev.ecattez.shahmat.domain.event.TurnChanged;
import dev.ecattez.shahmat.domain.game.BoardDecision;
import dev.ecattez.shahmat.domain.game.ChessGame;
import org.assertj.core.api.Assertions;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class PromotionStage extends Stage<PromotionStage> {

    private static final PieceType[] VALID_PROMOTION_PIECES = {
        PieceType.QUEEN,
        PieceType.ROOK,
        PieceType.BISHOP,
        PieceType.KNIGHT
    };

    private Piece pawn;
    private Square from;
    private Square to;
    private Piece promotion;
    private Piece opponent;
    private Square opponentLocation;

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

    private Square.File randomFile() {
        Square.File[] files = Square.File.values();
        int random = (int) (Math.random() * files.length);
        return files[random];
    }

    public PromotionStage a_$_pawn_in_the_penultimate_rank(String color, Integer penultimateRank) {
        this.from = new Square(randomFile(), Square.Rank.valueOf(penultimateRank));
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

    public PromotionStage an_opposing_piece_in_$(String opponentLocation) {
        opponent = pieceFactory.createPiece(
            PieceType.PAWN,
            pawn.color().opposite()
        );

        this.opponentLocation = new Square(opponentLocation);

        history.addAll(
            List.of(
                new PiecePositioned(
                    opponent,
                    this.opponentLocation
                )
            )
        );
        return self();
    }

    public PromotionStage the_pawn_reaches_the_other_side_of_the_chess_board(Integer lastRank) {
        this.to = new Square(from.file, Square.Rank.valueOf(lastRank));
        return self();
    }

    public PromotionStage the_pawn_s_owner_does_not_promote_the_pawn() {
        this.promotion = null;

        try {
            returnedEvents = ChessGame.move(
                Collections.unmodifiableList(history),
                new Move(
                    pawn.type(),
                    from,
                    to,
                    null
                )
            );
        } catch (RulesViolation e) {
            violation = e;
        }
        return self();
    }

    public PromotionStage the_pawn_s_owner_has_chosen_a_valid_promotion_type() {
        this.promotion = pieceFactory.createPiece(
            VALID_PROMOTION_PIECES[(int) (Math.random() * VALID_PROMOTION_PIECES.length)],
            pawn.color()
        );

        try {
            returnedEvents = ChessGame.move(
                Collections.unmodifiableList(history),
                new Move(
                    pawn.type(),
                    from,
                    to,
                    promotion.type()
                )
            );
        } catch (RulesViolation e) {
            violation = e;
        }

        return self();
    }

    public PromotionStage the_pawn_moves_to_$(String opponentLocation) {
        this.to = new Square(opponentLocation);

        return self();
    }

    public PromotionStage the_pawn_s_owner_has_chosen_to_promote_it_with_a_$(String promotionType) {
        this.promotion = pieceFactory.createPiece(
            PieceType.valueOf(promotionType),
            pawn.color()
        );

        try {
            returnedEvents = ChessGame.move(
                Collections.unmodifiableList(history),
                new Move(
                    pawn.type(),
                    from,
                    to,
                    promotion.type()
                )
            );
        } catch (RulesViolation e) {
            violation = e;
        }

        return self();
    }

    public PromotionStage a_$_is_promoted(String pieceType, String from, String to) {
        Piece otherPieceThanPawn = pieceFactory.createPiece(
            PieceType.valueOf(pieceType),
            PieceColor.WHITE
        );

        history.addAll(
            List.of(
                new PiecePositioned(
                    otherPieceThanPawn,
                    new Square(from)
                ),
                new TurnChanged(
                    PieceColor.WHITE
                )
            )
        );

        this.from = new Square(from);
        this.to = new Square(to);

        this.promotion = pieceFactory.createPiece(
            VALID_PROMOTION_PIECES[(int) (Math.random() * VALID_PROMOTION_PIECES.length)],
            otherPieceThanPawn.color()
        );

        try {
            returnedEvents = ChessGame.move(
                Collections.unmodifiableList(history),
                new Move(
                    otherPieceThanPawn.type(),
                    this.from,
                    this.to,
                    promotion.type()
                )
            );
        } catch (RulesViolation e) {
            violation = e;
        }

        return self();
    }

    public PromotionStage the_pawn_captures_the_opposing_piece() {
        Assertions
            .assertThat(returnedEvents)
            .contains(
                new PawnPromoted(
                    new PieceCaptured(
                        opponent,
                        opponentLocation,
                        pawn,
                        from
                    ),
                    promotion
                )
            );
        return self();
    }

    public PromotionStage the_promotion_piece_replaces_the_pawn() {
        Board board = BoardDecision.replay(history, returnedEvents);

        Assertions
            .assertThat(board.findPiece(to))
            .contains(promotion);

        return self();
    }

    public PromotionStage a_$_of_the_same_color_replaces_the_pawn(String promotionType) {
        Board board = BoardDecision.replay(history, returnedEvents);

        Assertions
            .assertThat(board.findPiece(to))
            .contains(
                pieceFactory.createPiece(
                    PieceType.valueOf(promotionType),
                    pawn.color()
                )
            );

        return self();
    }

    public PromotionStage the_move_is_refused_because_promotion_must_be_done() {
        Assertions
            .assertThat(violation)
            .isInstanceOf(PromotionMustBeDone.class);
        return self();
    }

    public PromotionStage the_promotion_is_refused() {
        Assertions
            .assertThat(violation)
            .isInstanceOf(PromotionRefused.class);
        return self();
    }

    public PromotionStage $_as_reason(String reason) {
        Assertions
            .assertThat(violation.getMessage())
            .isEqualTo(reason);
        return self();
    }

}
