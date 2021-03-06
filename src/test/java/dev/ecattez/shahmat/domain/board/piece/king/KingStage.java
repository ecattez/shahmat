package dev.ecattez.shahmat.domain.board.piece.king;

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

public class KingStage extends Stage<KingStage> {

    private Piece king;
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
            king,
            history,
            returnedEvents,
            violation
        );
    }

    public KingStage a_$_king_in_$(String color, String from) {
        this.from = new Square(from);
        this.king = pieceFactory.createPiece(
            PieceType.KING,
            PieceColor.valueOf(color)
        );
        history.addAll(
            List.of(
                new PiecePositioned(
                    king,
                    this.from
                ),
                new TurnChanged(
                    king.color()
                )
            )
        );
        return self();
    }

    public KingStage the_one_square_$_is_vacant(String direction) {
        // todo: event to clean a square / make a square vacant
        return self();
    }

    public KingStage the_one_square_$_is_not_vacant(String direction) {
        Piece anotherPiece = mock(Piece.class);
        Mockito.when(anotherPiece.unicode())
            .thenReturn("X");

        history.add(
            new PiecePositioned(
                anotherPiece,
                from.getNeighbour(
                    Direction.valueOf(direction),
                    king.orientation()
                )
            )
        );
        return self();
    }

    public KingStage the_square_$_is_not_vacant(String obstructed) {
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

    public KingStage an_opposing_piece_is_in_$(String opponentLocation) {
        this.to = new Square(opponentLocation);
        this.opponentPiece = pieceFactory.createPiece(
            PieceType.PAWN,
            king.color().opposite()
        );
        history.add(
            new PiecePositioned(
                opponentPiece,
                to
            )
        );
        return self();
    }

    public KingStage the_king_is_moved_$_$(int times, String direction) {
        try {
            this.returnedEvents = ChessGame.move(
                Collections.unmodifiableList(history),
                new Move(
                    PieceType.KING,
                    from,
                    from.getNeighbour(
                        Direction.valueOf(direction),
                        king.orientation(),
                        times
                    )
                )
            );
        } catch (RulesViolation e) {
            violation = e;
        }
        return self();
    }

    public KingStage the_king_is_moved_$(String direction) {
        try {
            this.returnedEvents = ChessGame.move(
                Collections.unmodifiableList(history),
                new Move(
                    PieceType.KING,
                    from,
                    from.getNeighbour(
                        Direction.valueOf(direction),
                        king.orientation()
                    )
                )
            );
        } catch (RulesViolation e) {
            violation = e;
        }
        return self();
    }

    public KingStage the_king_is_moved_to_$(String to) {
        try {
            this.returnedEvents = ChessGame.move(
                Collections.unmodifiableList(history),
                new Move(
                    PieceType.KING,
                    from,
                    new Square(to)
                )
            );
        } catch (RulesViolation e) {
            violation = e;
        }
        return self();
    }

    public KingStage the_king_is_in_$(String to) {
        Assertions
            .assertThat(returnedEvents)
            .contains(
                new PieceMoved(
                    king,
                    from,
                    new Square(to)
                )
            );
        return self();
    }

    public KingStage the_move_is_refused() {
        Assertions
            .assertThat(violation)
            .isInstanceOf(PieceCanNotBeMoved.class);
        return self();
    }

    public KingStage the_king_captures_the_opposing_piece() {
        Assertions
            .assertThat(returnedEvents)
            .contains(
                new PieceCaptured(
                    opponentPiece,
                    to,
                    king,
                    from
                )
            );
        return self();
    }

}
