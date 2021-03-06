package dev.ecattez.shahmat.domain.board.turn;

import com.tngtech.jgiven.annotation.ScenarioStage;
import com.tngtech.jgiven.junit5.JGivenExtension;
import com.tngtech.junit.dataprovider.DataProvider;
import com.tngtech.junit.dataprovider.DataProviderExtension;
import com.tngtech.junit.dataprovider.UseDataProviderExtension;
import dev.ecattez.shahmat.domain.board.Rules;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({
    JGivenExtension.class,
    DataProviderExtension.class,
    UseDataProviderExtension.class
})
@Rules
@TurnTag
public class TurnSpec {

    @ScenarioStage
    private TurnStage stage;

    @Test
    public void white_moves_first() {
        stage
            .when().a_chess_game_starts()
            .then().it_is_the_turn_of_$("WHITE");
    }

    @TestTemplate
    @DataProvider(value = {
        "WHITE, BLACK",
        "BLACK, WHITE"
    })
    public void turn_changes_after_a_move(
        String currentPlayer,
        String nextPlayer
    ) {
        stage
            .given().a_ready_to_play_chess_game()
            .and().$_is_playing(currentPlayer)
            .when().$_moves_one_of_its_piece(currentPlayer)
            .then().it_is_the_turn_of_$(nextPlayer);
    }

    @TestTemplate
    @DataProvider(value = {
        "WHITE, BLACK",
        "BLACK, WHITE"
    })
    public void a_player_can_not_move_its_opponent_s_piece(
        String color,
        String opponentColor
    ) {
        stage
            .given().a_ready_to_play_chess_game()
            .and().$_is_playing(color)
            .when().$_moves_a_$_piece(color, opponentColor)
            .then().the_move_is_refused();
    }

}
