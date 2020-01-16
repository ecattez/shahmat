package dev.ecattez.shahmat.domain.board.pawn;

import com.tngtech.jgiven.annotation.ScenarioStage;
import com.tngtech.jgiven.junit5.JGivenExtension;
import com.tngtech.junit.dataprovider.DataProvider;
import com.tngtech.junit.dataprovider.DataProviderExtension;
import com.tngtech.junit.dataprovider.UseDataProviderExtension;
import dev.ecattez.shahmat.domain.board.Rules;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({
    JGivenExtension.class,
    DataProviderExtension.class,
    UseDataProviderExtension.class
})
@Rules
@PawnTag
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
    public void pawn_can_immediately_capture_an_opponent_pawn_that_moved_forward_two_squares_on_a_neighbouring_file(
        String color,
        String from,
        String opponentRank,
        String captureTo
    ) {
        stage
            .given().a_$_pawn_in_$(color, from)
            .and().an_opponent_pawn_that_moved_forward_two_squares_on_a_neighbouring_file(opponentRank)
            .when().the_opponent_pawn_is_immediately_captured_en_passant()
            .then().the_pawn_is_in_$(captureTo)
            .and().the_opponent_pawn_is_removed_from_the_game();
    }

    @TestTemplate
    @DataProvider(value = {
        "WHITE, C5, B",
        "BLACK, C4, D",
    })
    public void pawn_can_not_capture_an_opponent_pawn_that_moved_forward_two_squares_on_a_neighbouring_file(
        String color,
        String from,
        String opponentRank
    ) {
        stage
            .given().a_$_pawn_in_$(color, from)
            .and().an_opponent_pawn_that_moved_forward_two_squares_on_a_neighbouring_file(opponentRank)
            .and().an_other_opponent_piece_as_moved_since()
            .when().the_opponent_pawn_is_captured_en_passant()
            .then().the_capture_is_refused();
    }

}
