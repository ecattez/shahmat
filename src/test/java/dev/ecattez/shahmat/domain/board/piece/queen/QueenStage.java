package dev.ecattez.shahmat.domain.board.piece.queen;

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

public class QueenStage extends Stage<QueenStage> {

    private Piece queen;
    private Square from;
    private Square to;
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
        System.out.println("Before command: " + queen.color());
        System.out.println(
            formatter.format(
                BoardDecision.replay(history)
            )
        );
        System.out.println();

        System.out.println("After command: " + queen.color());
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

    public QueenStage a_$_queen_in_$(String color, String from) {
        this.from = new Square(from);
        this.queen = pieceFactory.createPiece(
            PieceType.QUEEN,
            PieceColor.valueOf(color)
        );
        this.history.addAll(
            List.of(
                new PiecePositioned(
                    queen,
                    this.from
                ),
                new TurnChanged(
                    queen.color()
                )
            )
        );
        return self();
    }

    public QueenStage the_squares_between_$_and_$_are_vacant(String from, String to) {
        // todo: event to clean a square / make a square vacant
        return self();
    }

    public QueenStage the_square_$_is_not_vacant(String obstructed) {
        Piece anotherPiece = mock(Piece.class);
        Mockito.when(anotherPiece.unicode())
            .thenReturn("X");

        history.add(
            new PiecePositioned(
                anotherPiece,
                new Square(obstructed)
            )
        );
        return self();
    }

    public QueenStage an_opponent_piece_is_in_$(String opponentLocation) {
        this.to = new Square(opponentLocation);
        this.opponentPiece = pieceFactory.createPiece(
            PieceType.PAWN,
            queen.color().opposite()
        );
        history.add(
            new PiecePositioned(
                opponentPiece,
                to
            )
        );
        return self();
    }

    public QueenStage the_queen_is_moved_$_$(int times, String direction) {
        try {
            this.returnedEvents = ChessGame.move(
                Collections.unmodifiableList(history),
                new Move(
                    PieceType.QUEEN,
                    from,
                    from.getNeighbour(
                        Direction.valueOf(direction),
                        queen.orientation(),
                        times
                    )
                )
            );
        } catch (RulesViolation e) {
            violation = e;
        }
        return self();
    }

    public QueenStage the_queen_is_moved_to_$(String to) {
        try {
            this.returnedEvents = ChessGame.move(
                Collections.unmodifiableList(history),
                new Move(
                    PieceType.QUEEN,
                    from,
                    new Square(to)
                )
            );
        } catch (RulesViolation e) {
            violation = e;
        }
        return self();
    }

    public QueenStage the_queen_is_in_$(String to) {
        Assertions
            .assertThat(returnedEvents)
            .contains(
                new PieceMoved(
                    queen,
                    from,
                    new Square(to)
                )
            );
        return self();
    }

    public QueenStage the_move_is_refused() {
        Assertions
            .assertThat(violation)
            .isInstanceOf(ImpossibleToMove.class);
        return self();
    }

    public QueenStage the_queen_captures_the_opponent_piece() {
        Assertions
            .assertThat(returnedEvents)
            .contains(
                new PieceCaptured(
                    opponentPiece,
                    to,
                    queen,
                    from
                )
            );
        return self();
    }
}
