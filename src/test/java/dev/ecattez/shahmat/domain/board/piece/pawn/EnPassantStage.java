package dev.ecattez.shahmat.domain.board.piece.pawn;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.AfterScenario;
import com.tngtech.jgiven.annotation.BeforeStage;
import com.tngtech.jgiven.annotation.Hidden;
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
import dev.ecattez.shahmat.domain.event.PieceCapturedEnPassant;
import dev.ecattez.shahmat.domain.event.PieceMoved;
import dev.ecattez.shahmat.domain.event.PiecePositioned;
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

public class EnPassantStage extends Stage<EnPassantStage> {

    private Piece pawn;
    private Square from;
    private Square to;
    private Piece opponentPawn;

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
        System.out.println("Before command: " + pawn.color());
        System.out.println(
            formatter.format(
                BoardDecision.replay(history)
            )
        );
        System.out.println();

        System.out.println("After command: " + pawn.color());
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

    public EnPassantStage a_$_pawn_in_$(String color, String from) {
        this.from = new Square(from);
        this.pawn = pieceFactory.createPiece(
            PieceType.PAWN,
            PieceColor.valueOf(color)
        );
        this.history.addAll(
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

    public EnPassantStage an_opponent_pawn_that_moved_forward_two_squares_on_a_neighbouring_file(String opponentFile) {
        this.opponentPawn = pieceFactory.createPiece(
            PieceType.PAWN,
            pawn.color().opposite()
        );

        Square opponentLocation = new Square(
            Square.File.valueOf(opponentFile),
            from.rank
        );

        history.addAll(
            List.of(
                new TurnChanged(
                    opponentPawn.color()
                ),
                new PieceMoved(
                    opponentPawn,
                    opponentLocation
                        .getNeighbour(Direction.BACKWARD, opponentPawn.orientation(), 2),
                    opponentLocation
                ),
                new TurnChanged(
                    pawn.color()
                )
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
                    PieceType.QUEEN,
                    pawn.color().opposite()
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
                PieceType.PAWN,
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

    public EnPassantStage the_pawn_moves_behind_the_opponent_piece(@Hidden String endsTo) {
        Assertions
            .assertThat(returnedEvents)
            .contains(
                new PieceCapturedEnPassant(
                    opponentPawn,
                    new Square(endsTo),
                    pawn,
                    from
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
