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
import dev.ecattez.shahmat.domain.board.violation.PieceCanNotBeMoved;
import dev.ecattez.shahmat.domain.board.violation.RulesViolation;
import dev.ecattez.shahmat.domain.command.Move;
import dev.ecattez.shahmat.domain.event.ChessEvent;
import dev.ecattez.shahmat.domain.event.PieceCaptured;
import dev.ecattez.shahmat.domain.event.PieceMoved;
import dev.ecattez.shahmat.domain.event.PiecePositioned;
import dev.ecattez.shahmat.domain.event.TurnChanged;
import dev.ecattez.shahmat.domain.game.ChessGame;
import org.assertj.core.api.Assertions;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.mock;

public class PawnStage extends Stage<PawnStage> {

    private Piece pawn;
    private Square from;
    private Square to;
    private Piece opponentPiece;

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

    public PawnStage a_$_pawn_in_$(String color, String from) {
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

    public PawnStage $_is_vacant(String location) {
        // Todo: nettoyer la case au cas où ?
        return self();
    }

    public PawnStage $_is_not_vacant(String location) {
        Piece anotherPiece = mock(Piece.class);
        Mockito.when(anotherPiece.unicode())
            .thenReturn("X");

        history.add(
            new PiecePositioned(
                anotherPiece,
                new Square(location)
            )
        );
        return self();
    }

    public PawnStage an_opposing_piece_in_$(String opponentLocation) {
        this.to = new Square(opponentLocation);
        this.opponentPiece = pieceFactory.createPiece(
            PieceType.PAWN,
            pawn.color().opposite()
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

    public PawnStage the_pawn_is_moved_to_$(String to) {
        this.returnedEvents = ChessGame.move(
            Collections.unmodifiableList(history),
            new Move(
                PieceType.PAWN,
                from,
                new Square(to)
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
            .isInstanceOf(PieceCanNotBeMoved.class);
        return self();
    }

    public PawnStage the_pawn_captures_the_opposing_piece() {
        Assertions
            .assertThat(returnedEvents)
            .contains(
                new PieceCaptured(
                    opponentPiece,
                    to,
                    pawn,
                    from
                )
            );
        return self();
    }
}
