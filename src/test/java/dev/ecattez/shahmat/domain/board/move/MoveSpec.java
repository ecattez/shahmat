package dev.ecattez.shahmat.domain.board.move;

import com.tngtech.jgiven.junit5.JGivenExtension;
import com.tngtech.junit.dataprovider.DataProviderExtension;
import com.tngtech.junit.dataprovider.UseDataProviderExtension;
import dev.ecattez.shahmat.domain.board.Rules;
import dev.ecattez.shahmat.domain.board.piece.PieceBox;
import dev.ecattez.shahmat.domain.board.piece.PieceColor;
import dev.ecattez.shahmat.domain.board.piece.PieceFactory;
import dev.ecattez.shahmat.domain.board.piece.PieceType;
import dev.ecattez.shahmat.domain.board.square.Square;
import dev.ecattez.shahmat.domain.board.violation.InvalidMove;
import dev.ecattez.shahmat.domain.board.violation.NoPieceOnSquare;
import dev.ecattez.shahmat.domain.board.violation.RulesViolation;
import dev.ecattez.shahmat.domain.board.violation.WrongPieceSelected;
import dev.ecattez.shahmat.domain.command.Move;
import dev.ecattez.shahmat.domain.event.PiecePositioned;
import dev.ecattez.shahmat.domain.game.ChessGame;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Collections;
import java.util.List;

@ExtendWith({
    JGivenExtension.class,
    DataProviderExtension.class,
    UseDataProviderExtension.class
})
@Rules
@MoveTag
public class MoveSpec {

    private PieceFactory pieceFactory = PieceBox.getInstance();

    @Test
    public void move_fails_when_from_and_to_are_the_same_square() {
        for (PieceType pieceType: PieceType.values()) {
            try {
                ChessGame.move(
                    Collections.emptyList(),
                    new Move(
                        pieceType,
                        new Square("A2"),
                        new Square("A2")
                    )
                );
            } catch (RulesViolation violation) {
                Assertions
                    .assertThat(violation)
                    .isInstanceOf(InvalidMove.class);
            }
        }
    }

    @Test
    public void move_fails_when_piece_is_not_on_selected_square() {
        try {
            ChessGame.move(
                List.of(
                    new PiecePositioned(
                        pieceFactory.createPiece(
                            PieceType.QUEEN,
                            PieceColor.WHITE
                        ),
                        new Square("A3")
                    )
                ),
                new Move(
                    PieceType.PAWN,
                    new Square("B2"),
                    new Square("B3")
                )
            );
        } catch (RulesViolation violation) {
            Assertions
                .assertThat(violation)
                .isInstanceOf(NoPieceOnSquare.class);
        }
    }

    @Test
    public void move_fails_when_piece_is_not_on_the_type_on_selected_square() {
        try {
            ChessGame.move(
                List.of(
                    new PiecePositioned(
                        pieceFactory.createPiece(
                            PieceType.QUEEN,
                            PieceColor.WHITE
                        ),
                        new Square("A3")
                    )
                ),
                new Move(
                    PieceType.PAWN,
                    new Square("A3"),
                    new Square("A4")
                )
            );
        } catch (RulesViolation violation) {
            Assertions
                .assertThat(violation)
                .isInstanceOf(WrongPieceSelected.class);
        }
    }

}
