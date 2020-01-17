package dev.ecattez.shahmat.domain.board.pawn;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.AfterScenario;
import com.tngtech.jgiven.annotation.BeforeStage;
import dev.ecattez.shahmat.board.Direction;
import dev.ecattez.shahmat.board.Piece;
import dev.ecattez.shahmat.board.PieceBox;
import dev.ecattez.shahmat.board.PieceColor;
import dev.ecattez.shahmat.board.PieceFactory;
import dev.ecattez.shahmat.board.PieceType;
import dev.ecattez.shahmat.board.Square;
import dev.ecattez.shahmat.board.violation.ImpossibleToMove;
import dev.ecattez.shahmat.board.violation.RulesViolation;
import dev.ecattez.shahmat.command.Move;
import dev.ecattez.shahmat.event.BoardEvent;
import dev.ecattez.shahmat.event.PieceCaptured;
import dev.ecattez.shahmat.event.PieceMoved;
import dev.ecattez.shahmat.event.PiecePositioned;
import dev.ecattez.shahmat.game.ChessGame;
import org.assertj.core.api.Assertions;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.mock;

public class EnPassantStage extends Stage<EnPassantStage> {

    private Piece pawn;
    private Square from;
    private Square to;
    private Piece opponentPawn;
    private Square opponentLocation;

    private PieceFactory pieceFactory;
    private List<BoardEvent> returnedEvents;
    private List<BoardEvent> history;
    private RulesViolation violation;

    @BeforeStage
    public void init() {
        this.pieceFactory = PieceBox.getInstance();
        this.history = new LinkedList<>();
        this.returnedEvents = new LinkedList<>();
    }

    @AfterScenario
    public void after() {
        System.out.println("Before command");
        System.out.println(
            ChessGame.replay(
                history
            ).toString()
        );
        System.out.println();

        System.out.println("After command");
        System.out.println(
            ChessGame.replay(
                Stream.of(history, returnedEvents)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList())
            ).toString()
        );
        if (violation != null) {
            System.out.println(violation.getMessage());
        }
        System.out.println();
    }

    public EnPassantStage a_$_pawn_in_$(String color, String from) {
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

    public EnPassantStage an_opponent_pawn_that_moved_forward_two_squares_on_a_neighbouring_file(String opponentFile) {
        this.opponentPawn = pieceFactory.createPiece(
            PieceType.PAWN,
            pawn.color.opposite()
        );

        this.opponentLocation = new Square(
            Square.File.valueOf(opponentFile),
            from.rank
        );

        history.add(
            new PieceMoved(
                opponentPawn,
                opponentLocation
                    .getNeighbour(Direction.BACKWARD, opponentPawn.orientation(), 2),
                opponentLocation
            )
        );

        this.to = opponentLocation
            .getNeighbour(Direction.BACKWARD, opponentPawn.orientation());

        return self();
    }

    public EnPassantStage an_other_opponent_piece_as_moved_since() {
        this.history.add(
            new PieceMoved(
                pieceFactory.createPiece(
                    mock(PieceType.class),
                    pawn.color.opposite()
                ),
                mock(Square.class),
                mock(Square.class)
            )
        );

        return self();
    }

    public EnPassantStage the_opponent_pawn_is_immediately_captured_en_passant() {
        this.returnedEvents = ChessGame.move(
            Collections.unmodifiableList(history),
            new Move(
                pawn,
                from,
                to
            )
        );
        return self();
    }

    public EnPassantStage the_opponent_pawn_is_captured_en_passant() {
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

    public EnPassantStage the_pawn_is_in_$(String to) {
        Assertions
            .assertThat(returnedEvents)
            .containsSubsequence(
                new PieceMoved(
                    pawn,
                    from,
                    new Square(to)
                )
            );
        return self();
    }

    public EnPassantStage the_opponent_pawn_is_removed_from_the_game() {
        Assertions
            .assertThat(returnedEvents)
            .contains(
                new PieceCaptured(
                    opponentPawn,
                    pawn,
                    opponentLocation
                )
            );
        return self();
    }

    public EnPassantStage the_capture_is_refused() {
        Assertions
            .assertThat(violation)
            .isInstanceOf(ImpossibleToMove.class);
        return self();
    }
}
