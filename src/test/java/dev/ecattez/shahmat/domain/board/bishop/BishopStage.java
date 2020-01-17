package dev.ecattez.shahmat.domain.board.bishop;

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
import org.mockito.Mockito;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.mock;

public class BishopStage extends Stage<BishopStage> {

    private Piece bishop;
    private Square from;
    private Square to;
    private Piece opponentPiece;

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
        System.out.println("Before command: " + bishop.color);
        System.out.println(
            ChessGame.replay(
                history
            ).toString()
        );
        System.out.println();

        System.out.println("After command: " + bishop.color);
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

    public BishopStage a_$_bishop_in_$(String color, String from) {
        this.from = new Square(from);
        this.bishop = pieceFactory.createPiece(
            PieceType.BISHOP,
            PieceColor.valueOf(color)
        );
        this.history.add(
            new PiecePositioned(
                bishop,
                this.from
            )
        );
        return self();
    }

    public BishopStage the_squares_between_$_and_$_are_vacant(String from, String to) {
        // todo: event to clean a square / make a square vacant
        return self();
    }

    public BishopStage the_square_$_is_not_vacant(Square obstructedSquare) {
        Piece anotherPiece = mock(Piece.class);
        Mockito.when(anotherPiece.toString())
            .thenReturn("X");

        history.add(
            new PiecePositioned(
                anotherPiece,
                obstructedSquare
            )
        );
        return self();
    }

    public BishopStage an_opponent_piece_is_in_$(String opponentLocation) {
        this.to = new Square(opponentLocation);
        this.opponentPiece = pieceFactory.createPiece(
            PieceType.PAWN,
            bishop.color.opposite()
        );
        history.add(
            new PiecePositioned(
                opponentPiece,
                to
            )
        );
        return self();
    }

    public BishopStage the_bishop_is_moved_$_$(int times, String direction) {
        try {
            this.returnedEvents = ChessGame.move(
                Collections.unmodifiableList(history),
                new Move(
                    bishop,
                    from,
                    from.getNeighbour(
                        Direction.valueOf(direction),
                        bishop.orientation(),
                        times
                    )
                )
            );
        } catch (RulesViolation e) {
            violation = e;
        }
        return self();
    }

    public BishopStage the_bishop_moved_to_$(String to) {
        try {
            this.returnedEvents = ChessGame.move(
                Collections.unmodifiableList(history),
                new Move(
                    bishop,
                    from,
                    new Square(to)
                )
            );
        } catch (RulesViolation e) {
            violation = e;
        }
        return self();
    }

    public BishopStage the_bishop_is_in_$(String to) {
        Assertions
            .assertThat(returnedEvents)
            .contains(
                new PieceMoved(
                    bishop,
                    from,
                    new Square(to)
                )
            );
        return self();
    }

    public BishopStage the_move_is_refused() {
        Assertions
            .assertThat(violation)
            .isInstanceOf(ImpossibleToMove.class);
        return self();
    }

    public BishopStage the_opponent_piece_is_removed_from_the_game() {
        Assertions
            .assertThat(returnedEvents)
            .contains(
                new PieceCaptured(
                    opponentPiece,
                    bishop,
                    to
                )
            );
        return self();
    }
}
