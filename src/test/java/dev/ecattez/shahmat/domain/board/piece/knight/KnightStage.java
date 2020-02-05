package dev.ecattez.shahmat.domain.board.piece.knight;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.AfterScenario;
import com.tngtech.jgiven.annotation.BeforeStage;
import dev.ecattez.shahmat.domain.board.BoardStringFormatter;
import dev.ecattez.shahmat.domain.board.Direction;
import dev.ecattez.shahmat.domain.board.piece.Piece;
import dev.ecattez.shahmat.domain.board.piece.PieceBox;
import dev.ecattez.shahmat.domain.board.piece.PieceColor;
import dev.ecattez.shahmat.domain.board.piece.PieceFactory;
import dev.ecattez.shahmat.domain.board.piece.PieceType;
import dev.ecattez.shahmat.domain.board.square.Square;
import dev.ecattez.shahmat.domain.board.violation.ImpossibleToMove;
import dev.ecattez.shahmat.domain.board.violation.RulesViolation;
import dev.ecattez.shahmat.domain.command.Move;
import dev.ecattez.shahmat.domain.event.ChessEvent;
import dev.ecattez.shahmat.domain.event.PieceCaptured;
import dev.ecattez.shahmat.domain.event.PieceMoved;
import dev.ecattez.shahmat.domain.event.PiecePositioned;
import dev.ecattez.shahmat.domain.event.TurnChanged;
import dev.ecattez.shahmat.domain.game.BoardDecision;
import dev.ecattez.shahmat.domain.game.ChessGame;
import org.assertj.core.api.Assertions;
import org.mockito.Mockito;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.mock;

public class KnightStage extends Stage<KnightStage> {

    private Piece knight;
    private Square from;
    private Square to;
    private Square direction;
    private Piece opponentPiece;

    private PieceFactory pieceFactory;
    private List<ChessEvent> returnedEvents;
    private List<ChessEvent> history;
    private RulesViolation violation;
    private BoardStringFormatter formatter;

    @BeforeStage
    public void init() {
        this.pieceFactory = PieceBox.getInstance();
        this.history = new LinkedList<>();
        this.returnedEvents = new LinkedList<>();
        this.formatter = new BoardStringFormatter();
    }

    @AfterScenario
    public void after() {
        System.out.println("Before command: " + knight.color());
        System.out.println(
            formatter.format(
                BoardDecision.replay(history)
            )
        );
        System.out.println();

        System.out.println("After command: " + knight.color());
        System.out.println(
            formatter.format(
                BoardDecision.replay(
                    Stream.of(history, returnedEvents)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList())
                )
            )
        );
        if (violation != null) {
            System.out.println(violation.getMessage());
        }
        System.out.println();
    }

    public KnightStage a_$_knight_in_$(String color, String from) {
        this.from = new Square(from);
        this.knight = pieceFactory.createPiece(
            PieceType.KNIGHT,
            PieceColor.valueOf(color)
        );
        this.history.addAll(
            List.of(
                new PiecePositioned(
                    knight,
                    this.from
                ),
                new TurnChanged(
                    knight.color()
                )
            )
        );
        return self();
    }

    public KnightStage the_square_$_is_not_vacant(String obstructedSquare) {
        Piece anotherPiece = mock(Piece.class);
        Mockito.when(anotherPiece.unicode())
            .thenReturn("X");

        history.add(
            new PiecePositioned(
                anotherPiece,
                new Square(obstructedSquare)
            )
        );
        return self();
    }

    public KnightStage an_ally_piece_in_$(String allyLocation) {
        this.to = new Square(allyLocation);
        this.opponentPiece = pieceFactory.createPiece(
            PieceType.PAWN,
            knight.color()
        );
        history.add(
            new PiecePositioned(
                opponentPiece,
                to
            )
        );
        return self();
    }

    public KnightStage an_opponent_piece_is_in_$(String opponentLocation) {
        this.to = new Square(opponentLocation);
        this.opponentPiece = pieceFactory.createPiece(
            PieceType.PAWN,
            knight.color().opposite()
        );
        history.add(
            new PiecePositioned(
                opponentPiece,
                to
            )
        );
        return self();
    }

    public KnightStage the_knight_is_moved_to_$_two_times(String direction) {
        this.direction = from.getNeighbour(
            Direction.valueOf(direction),
            knight.orientation(),
            2
        );
        return self();
    }

    public KnightStage then_moved_$_one_square(String perpendicular) {
        try {
            this.returnedEvents = ChessGame.move(
                Collections.unmodifiableList(history),
                new Move(
                    PieceType.KNIGHT,
                    from,
                    direction.getNeighbour(
                        Direction.valueOf(perpendicular),
                        knight.orientation()
                    )
                )
            );
        } catch (RulesViolation e) {
            violation = e;
        }
        return self();
    }

    public KnightStage the_knight_is_moved_to_$(String to) {
        try {
            this.returnedEvents = ChessGame.move(
                Collections.unmodifiableList(history),
                new Move(
                    PieceType.KNIGHT,
                    from,
                    new Square(to)
                )
            );
        } catch (RulesViolation e) {
            violation = e;
        }
        return self();
    }

    public KnightStage the_knight_is_in_$(String to) {
        Assertions
            .assertThat(returnedEvents)
            .contains(
                new PieceMoved(
                    knight,
                    from,
                    new Square(to)
                )
            );
        return self();
    }

    public KnightStage the_knight_captures_the_opponent_piece() {
        Assertions
            .assertThat(returnedEvents)
            .contains(
                new PieceCaptured(
                    opponentPiece,
                    to,
                    knight,
                    from
                )
            );
        return self();
    }

    public KnightStage the_move_is_refused() {
        Assertions
            .assertThat(violation)
            .isInstanceOf(ImpossibleToMove.class);
        return self();
    }
}
