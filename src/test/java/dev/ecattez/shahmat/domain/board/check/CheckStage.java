package dev.ecattez.shahmat.domain.board.check;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.AfterScenario;
import com.tngtech.jgiven.annotation.BeforeStage;
import com.tngtech.jgiven.annotation.Hidden;
import dev.ecattez.shahmat.domain.board.BeforeAfterOutput;
import dev.ecattez.shahmat.domain.board.Board;
import dev.ecattez.shahmat.domain.board.piece.Piece;
import dev.ecattez.shahmat.domain.board.piece.PieceBox;
import dev.ecattez.shahmat.domain.board.piece.PieceColor;
import dev.ecattez.shahmat.domain.board.piece.PieceFactory;
import dev.ecattez.shahmat.domain.board.piece.PieceType;
import dev.ecattez.shahmat.domain.board.piece.move.Capture;
import dev.ecattez.shahmat.domain.board.piece.move.MoveOnVacant;
import dev.ecattez.shahmat.domain.board.square.Square;
import dev.ecattez.shahmat.domain.board.violation.PieceCanNotBeMoved;
import dev.ecattez.shahmat.domain.board.violation.RulesViolation;
import dev.ecattez.shahmat.domain.command.Move;
import dev.ecattez.shahmat.domain.command.Promote;
import dev.ecattez.shahmat.domain.event.ChessEvent;
import dev.ecattez.shahmat.domain.event.KingChecked;
import dev.ecattez.shahmat.domain.event.PawnPromoted;
import dev.ecattez.shahmat.domain.event.PieceCaptured;
import dev.ecattez.shahmat.domain.event.PieceMoved;
import dev.ecattez.shahmat.domain.event.PiecePositioned;
import dev.ecattez.shahmat.domain.event.PromotionProposed;
import dev.ecattez.shahmat.domain.event.TurnChanged;
import dev.ecattez.shahmat.domain.game.BoardDecision;
import dev.ecattez.shahmat.domain.game.ChessGame;
import org.assertj.core.api.Assertions;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.mock;

public class CheckStage extends Stage<CheckStage> {

    private PieceColor playerColor = PieceColor.WHITE;
    private PieceColor opponentColor = PieceColor.BLACK;

    private Piece king;
    private Square kingLocation;
    private Piece opponent;
    private Square opponentLocation;
    private Piece ally;
    private Square allyLocation;
    private Square to;

    private Piece movingPiece;
    private Square movingPieceLocation;

    private PieceType promotionType;

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
            king,
            history,
            returnedEvents,
            violation
        );
    }

    public CheckStage a_king_in_$(String kingLocation) {
        this.kingLocation = new Square(kingLocation);
        this.king = pieceFactory.createPiece(
            PieceType.KING,
            playerColor
        );
        history.addAll(
            List.of(
                new PiecePositioned(
                    king,
                    this.kingLocation
                )
            )
        );
        return self();
    }

    public CheckStage a_king_in_check_in_$(String kingLocation, @Hidden String opponentType, @Hidden String opponentLocation) {
        a_king_in_$(kingLocation);

        history.addAll(
            List.of(
                new KingChecked(
                    new PieceMoved(
                        pieceFactory.createPiece(
                            PieceType.valueOf(opponentType),
                            king.color().opposite()
                        ),
                        mock(Square.class),
                        new Square(opponentLocation)
                    ),
                    new Square(kingLocation)
                )
            )
        );

        return self();
    }

    public CheckStage an_opposing_$_in_$(String opponentType, String opponentLocation) {
        this.opponentLocation = new Square(opponentLocation);
        this.opponent = pieceFactory.createPiece(
            PieceType.valueOf(opponentType),
            opponentColor
        );
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

    public CheckStage the_square_$_not_in_its_bounds_of_movement(String to) {
        this.to = new Square(to);

        Board board = BoardDecision.replay(history);

        BoardDecision.findMovementAwareOfCheck(
            board,
            opponentLocation,
            this.to,
            opponent
        ).ifPresent(move ->
            Assertions
                .assertThat(move)
                .isEqualTo(
                    new MoveOnVacant(
                        pieceFactory.createPiece(
                            PieceType.PAWN,
                            opponent.color()
                        ),
                        opponentLocation,
                        this.to
                    )
                )
        );

        return self();
    }

    public CheckStage a_$_ally_of_the_king_in_$(String allyType, String allyLocation) {
        this.allyLocation = new Square(allyLocation);
        this.ally = pieceFactory.createPiece(
            PieceType.valueOf(allyType),
            playerColor
        );
        history.addAll(
            List.of(
                new PiecePositioned(
                    ally,
                    this.allyLocation
                )
            )
        );
        return self();
    }

    public CheckStage an_opposing_pawn_in_the_other_side_of_the_board(@Hidden String promotionLocation) {
        this.opponent = pieceFactory.createPiece(
            PieceType.PAWN,
            opponentColor
        );

        this.opponentLocation = new Square(promotionLocation);
        this.to = new Square(promotionLocation);

        history.addAll(
            List.of(
                new PiecePositioned(
                    opponent,
                    opponentLocation
                ),
                new PromotionProposed(
                    opponentLocation
                )
            )
        );

        return self();
    }

    public CheckStage the_opposing_piece_is_moved_to_$(String to) {
        this.to = new Square(to);

        history.addAll(
            List.of(
                new TurnChanged(
                    opponent.color()
                )
            )
        );

        move(opponent, opponentLocation);

        return self();
    }

    public CheckStage the_king_is_moved_to_$(String to) {
        this.to = new Square(to);

        history.addAll(
            List.of(
                new TurnChanged(
                    king.color()
                )
            )
        );

        move(king, kingLocation);

        return self();
    }

    public CheckStage the_ally_is_moved_to_$(String to) {
        this.to = new Square(to);

        history.addAll(
            List.of(
                new TurnChanged(
                    ally.color()
                )
            )
        );

        move(ally, allyLocation);

        return self();
    }

    public CheckStage the_king_is_in_the_bounds_of_movement_of_the_opposing_piece() {
        Board board = BoardDecision.replay(
            Stream.of(
                history,
                returnedEvents
            )
            .flatMap(Collection::stream)
            .collect(Collectors.toList())
        );

        Assertions
            .assertThat(
                BoardDecision.findMovement(
                    board,
                    to,
                    kingLocation,
                    opponent
                )
            ).contains(
                new Capture(
                    opponent,
                    to,
                    kingLocation,
                    king
                )
        );

        return self();
    }

    public CheckStage the_opposing_pawn_is_promoted_to_$(String promotionType) {
        this.promotionType = PieceType.valueOf(promotionType);

        try {
            returnedEvents = ChessGame.promote(
                Collections.unmodifiableList(history),
                new Promote(
                    opponentLocation,
                    this.promotionType
                )
            );
        } catch (RulesViolation e) {
            violation = e;
        }
        return self();
    }

    public CheckStage the_king_is_in_check() {
        Assertions
            .assertThat(returnedEvents)
            .containsAnyOf(
                new KingChecked(
                    new PieceMoved(
                        opponent,
                        opponentLocation,
                        to
                    ),
                    kingLocation
                ),
                new KingChecked(
                    new PawnPromoted(
                        opponentLocation,
                        promotionType
                    ),
                    kingLocation
                )
            );

        return self();
    }

    public CheckStage the_move_is_refused() {
        Assertions
            .assertThat(violation)
            .isInstanceOf(PieceCanNotBeMoved.class);
        return self();
    }

    public CheckStage the_king_is_no_longer_in_check() {
        Assertions
            .assertThat(returnedEvents)
            .containsAnyOf(
                new PieceMoved(
                    movingPiece,
                    movingPieceLocation,
                    to
                ),
                new PieceCaptured(
                    opponent,
                    opponentLocation,
                    movingPiece,
                    movingPieceLocation
                )
            );

        return self();
    }

    private CheckStage move(Piece movingPiece, Square movingPieceLocation) {
        this.movingPiece = movingPiece;
        this.movingPieceLocation = movingPieceLocation;

        try {
            returnedEvents = ChessGame.move(
                Collections.unmodifiableList(history),
                new Move(
                    movingPiece.type(),
                    movingPieceLocation,
                    this.to
                )
            );
        } catch (RulesViolation e) {
            violation = e;
        }
        return self();
    }
}
