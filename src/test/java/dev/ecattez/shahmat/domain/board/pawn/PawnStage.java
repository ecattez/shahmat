package dev.ecattez.shahmat.domain.board.pawn;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.AfterScenario;
import com.tngtech.jgiven.annotation.BeforeStage;
import dev.ecattez.shahmat.board.violation.ImpossibleToMove;
import dev.ecattez.shahmat.game.ChessGame;
import dev.ecattez.shahmat.board.Direction;
import dev.ecattez.shahmat.board.Piece;
import dev.ecattez.shahmat.board.PieceBox;
import dev.ecattez.shahmat.board.PieceColor;
import dev.ecattez.shahmat.board.PieceFactory;
import dev.ecattez.shahmat.board.PieceType;
import dev.ecattez.shahmat.board.violation.RulesViolation;
import dev.ecattez.shahmat.board.Square;
import dev.ecattez.shahmat.command.Move;
import dev.ecattez.shahmat.event.BoardEvent;
import dev.ecattez.shahmat.event.PieceCaptured;
import dev.ecattez.shahmat.event.PieceMoved;
import dev.ecattez.shahmat.event.PiecePositioned;
import org.assertj.core.api.Assertions;
import org.mockito.Mockito;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.mock;

public class PawnStage extends Stage<PawnStage> {

    private Piece pawn;
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

    public PawnStage $_is_vacant(String location) {
        // Todo: nettoyer la case au cas où ?
        return self();
    }

    public PawnStage $_is_not_vacant(String location) {
        Piece anotherPiece = mock(Piece.class);
        Mockito.when(anotherPiece.toString())
            .thenReturn("X");

        history.add(
            new PiecePositioned(
                anotherPiece,
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
        this.to = from.getNeighbour(Direction.FORWARD, pawn.orientation());
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

    public PawnStage the_pawn_is_moved_forward_two_squares() {
        this.to = from.getNeighbour(
            Direction.FORWARD,
            pawn.orientation(),
            2
        );
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

    public PawnStage the_opponent_piece_is_captured_by_the_pawn() {
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
                    pawn,
                    to
                )
            );
        return self();
    }
}
