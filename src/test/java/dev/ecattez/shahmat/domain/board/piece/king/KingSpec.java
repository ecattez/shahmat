package dev.ecattez.shahmat.domain.board.piece.king;

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
@KingTag
public class KingSpec {

    @ScenarioStage
    private KingStage stage;

    @TestTemplate
    @DataProvider(value = {
        "WHITE, E4, E5, FORWARD",
        "WHITE, E6, D7, FORWARD_LEFT",
        "WHITE, C3, D4, FORWARD_RIGHT",
        "WHITE, B6, B5, BACKWARD",
        "WHITE, B5, A4, BACKWARD_LEFT",
        "WHITE, A5, B4, BACKWARD_RIGHT",
        "WHITE, E5, D5, SHIFT_LEFT",
        "WHITE, F6, G6, SHIFT_RIGHT",
        "BLACK, E5, E4, FORWARD",
        "BLACK, D5, E4, FORWARD_LEFT",
        "BLACK, E5, D4, FORWARD_RIGHT",
        "BLACK, D5, D6, BACKWARD",
        "BLACK, G6, H7, BACKWARD_LEFT",
        "BLACK, G5, F6, BACKWARD_RIGHT",
        "BLACK, E4, F4, SHIFT_LEFT",
        "BLACK, G6, F6, SHIFT_RIGHT",
    })
    public void king_moves_exactly_one_square_horizontally_vertically_or_diagonally(
        String color,
        String from,
        String to,
        String direction
    ) {
        stage
            .given().a_$_king_in_$(color, from)
            .and().the_one_square_$_is_vacant(direction)
            .when().the_king_is_moved_$(direction)
            .then().the_king_is_in_$(to);
    }

    @TestTemplate
    @DataProvider(value = {
        "WHITE, E4, FORWARD",
        "WHITE, E6, FORWARD_LEFT",
        "WHITE, C3, FORWARD_RIGHT",
        "WHITE, B6, BACKWARD",
        "WHITE, B5, BACKWARD_LEFT",
        "WHITE, A5, BACKWARD_RIGHT",
        "WHITE, E5, SHIFT_LEFT",
        "WHITE, F6, SHIFT_RIGHT",
        "BLACK, E5, FORWARD",
        "BLACK, D5, FORWARD_LEFT",
        "BLACK, E5, FORWARD_RIGHT",
        "BLACK, D5, BACKWARD",
        "BLACK, G6, BACKWARD_LEFT",
        "BLACK, G5, BACKWARD_RIGHT",
        "BLACK, E4, SHIFT_LEFT",
        "BLACK, G6, SHIFT_RIGHT",
    })
    public void king_can_not_move_beyond_an_obstructed_path(
        String color,
        String from,
        String direction
    ) {
        stage
            .given().a_$_king_in_$(color, from)
            .and().the_one_square_$_is_not_vacant(direction)
            .when().the_king_is_moved_$(direction)
            .then().the_move_is_refused();
    }

    @TestTemplate
    @DataProvider(value = {
        "WHITE, E4, D3",
        "WHITE, E4, D4",
        "WHITE, E4, D5",
        "WHITE, E4, E3",
        "WHITE, E4, E5",
        "WHITE, E4, F3",
        "WHITE, E4, F4",
        "WHITE, E4, F5",
        "BLACK, E5, E6",
        "BLACK, D5, E5",
        "BLACK, D5, E4",
        "BLACK, D5, D6",
        "BLACK, D5, D4",
        "BLACK, D5, C6",
        "BLACK, D5, C5",
        "BLACK, D5, C4",
    })
    public void king_can_capture_an_opponent_piece_that_obstruct_its_way(
        String color,
        String from,
        String to
    ) {
        stage
            .given().a_$_king_in_$(color, from)
            .and().an_opponent_piece_is_in_$(to)
            .when().the_king_is_moved_to_$(to)
            .and().the_king_captures_the_opponent_piece();
    }

}
