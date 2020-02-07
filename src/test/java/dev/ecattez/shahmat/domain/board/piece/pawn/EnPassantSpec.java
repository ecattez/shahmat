package dev.ecattez.shahmat.domain.board.piece.pawn;

import com.tngtech.jgiven.annotation.ScenarioStage;
import com.tngtech.jgiven.junit5.JGivenExtension;
import com.tngtech.junit.dataprovider.DataProvider;
import com.tngtech.junit.dataprovider.DataProviderExtension;
import com.tngtech.junit.dataprovider.UseDataProviderExtension;
import dev.ecattez.shahmat.domain.board.Rules;
import dev.ecattez.shahmat.domain.board.move.MoveTag;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({
    JGivenExtension.class,
    DataProviderExtension.class,
    UseDataProviderExtension.class
})
@Rules
@PawnTag
@MoveTag
@EnPassantTag
public class EnPassantSpec {

    @ScenarioStage
    private EnPassantStage stage;

    @TestTemplate
    @DataProvider(value = {
        "WHITE, C5, B, B6",
        "WHITE, C5, D, D6",
        "BLACK, C4, D, D3",
        "BLACK, C4, B, B3",
    })
    public void a_pawn_can_immediately_capture_an_opposing_pawn_that_moved_forward_two_squares_on_a_neighbouring_file(
        String color,
        String from,
        String opponentRank,
        String endsTo
    ) {
        stage
            .given().a_$_pawn_in_$(color, from)
            .and().an_opposing_pawn_that_moved_forward_two_squares_on_a_neighbouring_file(opponentRank)
            .when().the_opposing_pawn_is_immediately_captured_en_passant()
            .then().the_pawn_moves_behind_the_opposing_piece(endsTo);
    }

    @TestTemplate
    @DataProvider(value = {
        "WHITE, C5, B",
        "BLACK, C4, D",
    })
    public void a_pawn_can_not_capture_an_opposing_pawn_that_moved_forward_two_squares_on_a_neighbouring_file(
        String color,
        String from,
        String opponentRank
    ) {
        stage
            .given().a_$_pawn_in_$(color, from)
            .and().an_opposing_pawn_that_moved_forward_two_squares_on_a_neighbouring_file(opponentRank)
            .and().an_other_opposing_piece_as_moved_since()
            .when().the_opposing_pawn_is_captured_en_passant()
            .then().the_capture_is_refused();
    }

}
