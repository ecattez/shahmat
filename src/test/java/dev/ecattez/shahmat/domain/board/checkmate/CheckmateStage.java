package dev.ecattez.shahmat.domain.board.checkmate;

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
import dev.ecattez.shahmat.domain.board.violation.RulesViolation;
import dev.ecattez.shahmat.domain.command.Move;
import dev.ecattez.shahmat.domain.event.ChessEvent;
import dev.ecattez.shahmat.domain.event.KingCheckmated;
import dev.ecattez.shahmat.domain.event.PawnPromoted;
import dev.ecattez.shahmat.domain.event.PieceMoved;
import dev.ecattez.shahmat.domain.event.PiecePositioned;
import dev.ecattez.shahmat.domain.event.TurnChanged;
import dev.ecattez.shahmat.domain.game.BoardDecision;
import dev.ecattez.shahmat.domain.game.ChessGame;
import org.assertj.core.api.Assertions;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class CheckmateStage extends Stage<CheckmateStage> {

    private PieceColor playerColor = PieceColor.WHITE;
    private PieceColor opponentColor = PieceColor.BLACK;

    private Piece king;
    private Square kingLocation;

    private Piece promotion;

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

    public CheckmateStage a_king_is_put_in_check_because_of_an_opposing_move() {
        king = pieceFactory.createPiece(
            PieceType.KING,
            playerColor
        );

        kingLocation = new Square("E1");

        history.addAll(
            List.of(
                new PiecePositioned(
                    king,
                    kingLocation
                ),
                new PiecePositioned(
                    pieceFactory.createPiece(
                        PieceType.QUEEN,
                        opponentColor
                    ),
                    new Square("A2")
                ),
                new PiecePositioned(
                    pieceFactory.createPiece(
                        PieceType.ROOK,
                        opponentColor
                    ),
                    new Square("H2")
                ),
                new TurnChanged(
                    opponentColor
                )
            )
        );

        try {
            returnedEvents = ChessGame.move(
                Collections.unmodifiableList(history),
                new Move(
                    PieceType.QUEEN,
                    new Square("A2"),
                    new Square("A1")
                )
            );
        } catch (RulesViolation e) {
            violation = e;
        }

        return self();
    }

    public CheckmateStage a_king_is_put_in_check_because_of_an_opposing_promotion() {
        king = pieceFactory.createPiece(
            PieceType.KING,
            playerColor
        );

        kingLocation = new Square("E1");

        promotion = pieceFactory.createPiece(
            PieceType.QUEEN,
            opponentColor
        );

        history.addAll(
            List.of(
                new PiecePositioned(
                    king,
                    kingLocation
                ),
                new PiecePositioned(
                    pieceFactory.createPiece(
                        PieceType.PAWN,
                        opponentColor
                    ),
                    new Square("A2")
                ),
                new PiecePositioned(
                    pieceFactory.createPiece(
                        PieceType.ROOK,
                        opponentColor
                    ),
                    new Square("H2")
                ),
                new TurnChanged(
                    opponentColor
                )
            )
        );

        try {
            returnedEvents = ChessGame.move(
                Collections.unmodifiableList(history),
                new Move(
                    PieceType.PAWN,
                    new Square("A2"),
                    new Square("A1"),
                    promotion.type()
                )
            );
        } catch (RulesViolation e) {
            violation = e;
        }

        return self();
    }

    public CheckmateStage the_king_s_player_cannot_make_any_legal_move() {
        Board board = BoardDecision.replay(history, returnedEvents);

        Assertions
            .assertThat(
                BoardDecision.canMoveAwareOfCheck(board, kingLocation, king)
            )
            .isFalse();

        return self();
    }

    public CheckmateStage the_king_is_checkmate() {
        Assertions
            .assertThat(returnedEvents)
            .containsAnyOf(
                new KingCheckmated(
                    new PieceMoved(
                        pieceFactory.createPiece(
                            PieceType.QUEEN,
                            opponentColor
                        ),
                        new Square("A2"),
                        new Square("A1")
                    ),
                    kingLocation
                ),
                new KingCheckmated(
                    new PawnPromoted(
                        new PieceMoved(
                            pieceFactory.createPiece(
                                PieceType.PAWN,
                                opponentColor
                            ),
                            new Square("A2"),
                            new Square("A1")
                        ),
                        promotion
                    ),
                    kingLocation
                )
            );

        return self();
    }

    public CheckmateStage the_game_is_over() {
        Board board = BoardDecision.replay(history, returnedEvents);

        Assertions
            .assertThat(board.isOver())
            .isTrue();

        return self();
    }

    public CheckmateStage the_player_s_opponent_as_winner() {
        Board board = BoardDecision.replay(history, returnedEvents);

        Assertions
            .assertThat(board.findWinner())
            .contains(opponentColor);

        return self();
    }
}
