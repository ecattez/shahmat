package dev.ecattez.shahmat.domain.board.checkmate;

import com.tngtech.jgiven.annotation.ScenarioStage;
import com.tngtech.jgiven.junit5.JGivenExtension;
import com.tngtech.junit.dataprovider.DataProviderExtension;
import com.tngtech.junit.dataprovider.UseDataProviderExtension;
import dev.ecattez.shahmat.domain.board.Rules;
import dev.ecattez.shahmat.domain.board.check.CheckTag;
import dev.ecattez.shahmat.domain.board.init.BoardTag;
import dev.ecattez.shahmat.domain.board.move.MoveTag;
import dev.ecattez.shahmat.domain.board.piece.king.KingTag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({
    JGivenExtension.class,
    DataProviderExtension.class,
    UseDataProviderExtension.class
})
@Rules
@MoveTag
@KingTag
@BoardTag
@CheckTag
@CheckmateTag
public class CheckmateSpec {

    @ScenarioStage
    private CheckmateStage stage;

    @Test
    public void a_king_is_checkmate_because_of_an_opposing_move() {
        stage
            .when().a_king_is_put_in_check_because_of_an_opposing_move()
            .and().the_king_s_player_cannot_make_any_legal_move()
            .then().the_king_is_checkmate()
            .and().the_game_is_over()
            .with().the_player_s_opponent_as_winner();
    }

    @Test
    public void a_king_is_checkmate_because_of_an_opposing_promotion() {
        stage
            .when().a_king_is_put_in_check_because_of_an_opposing_promotion()
            .and().the_king_s_player_cannot_make_any_legal_move()
            .then().the_king_is_checkmate()
            .and().the_game_is_over()
            .with().the_player_s_opponent_as_winner();
    }

}
