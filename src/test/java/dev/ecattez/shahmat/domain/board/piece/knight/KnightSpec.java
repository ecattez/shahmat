package dev.ecattez.shahmat.domain.board.piece.knight;

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
@MoveTag
@KnightTag
public class KnightSpec {

    @ScenarioStage
    private KnightStage stage;

    @TestTemplate
    @DataProvider(value = {
        "WHITE, D5, FORWARD, SHIFT_LEFT, C7",
        "WHITE, D5, FORWARD, SHIFT_RIGHT, E7",
        "WHITE, D5, SHIFT_LEFT, FORWARD, B6",
        "WHITE, D5, SHIFT_RIGHT, FORWARD, F6",
        "WHITE, D5, SHIFT_LEFT, BACKWARD, B4",
        "WHITE, D5, SHIFT_RIGHT, BACKWARD, F4",
        "WHITE, D5, BACKWARD, SHIFT_LEFT, C3",
        "WHITE, D5, BACKWARD, SHIFT_RIGHT, E3",
        "BLACK, E4, FORWARD, SHIFT_LEFT, F2",
        "BLACK, E4, FORWARD, SHIFT_RIGHT, D2",
        "BLACK, E4, SHIFT_LEFT, FORWARD, G3",
        "BLACK, E4, SHIFT_RIGHT, FORWARD, C3",
        "BLACK, E4, SHIFT_LEFT, BACKWARD, G5",
        "BLACK, E4, SHIFT_RIGHT, BACKWARD, C5",
        "BLACK, E4, BACKWARD, SHIFT_LEFT, F6",
        "BLACK, E4, BACKWARD, SHIFT_RIGHT, D6",
    })
    public void a_knight_can_move_forward_backward_left_or_right_two_squares_and_must_then_move_one_square_in_either_perpendicular_direction(
        String color,
        String from,
        String direction,
        String perpendicular,
        String to
    ) {
        stage
            .given().a_$_knight_in_$(color, from)
            .when().the_knight_is_moved_to_$_two_times(direction)
            .and().then_moved_$_one_square(perpendicular)
            .then().the_knight_is_in_$(to);
    }

    @TestTemplate
    @DataProvider(value = {
        "WHITE, D5, C7",
        "WHITE, D5, E7",
        "WHITE, D5, B6",
        "WHITE, D5, F6",
        "WHITE, D5, B4",
        "WHITE, D5, F4",
        "WHITE, D5, C3",
        "WHITE, D5, E3",
        "BLACK, E4, D6",
        "BLACK, E4, F6",
        "BLACK, E4, C5",
        "BLACK, E4, G5",
        "BLACK, E4, C3",
        "BLACK, E4, G3",
        "BLACK, E4, D2",
        "BLACK, E4, F2",
    })
    public void a_knight_can_not_take_the_place_of_any_of_its_allies_on_the_board_that_is_within_its_bounds_of_movement(
        String color,
        String from,
        String to
    ) {
        stage
            .given().a_$_knight_in_$(color, from)
            .and().an_allied_piece_in_$(to)
            .when().the_knight_is_moved_to_$(to)
            .and().the_move_is_refused();
    }

    @TestTemplate
    @DataProvider(value = {
        "WHITE, D5, C7",
        "WHITE, D5, E7",
        "WHITE, D5, B6",
        "WHITE, D5, F6",
        "WHITE, D5, B4",
        "WHITE, D5, F4",
        "WHITE, D5, C3",
        "WHITE, D5, E3",
        "BLACK, E4, D6",
        "BLACK, E4, F6",
        "BLACK, E4, C5",
        "BLACK, E4, G5",
        "BLACK, E4, C3",
        "BLACK, E4, G3",
        "BLACK, E4, D2",
        "BLACK, E4, F2",
    })
    public void a_knight_can_take_any_opposing_piece_on_the_board_that_is_within_its_bounds_of_movement(
        String color,
        String from,
        String to
    ) {
        stage
            .given().a_$_knight_in_$(color, from)
            .and().an_opposing_piece_is_in_$(to)
            .when().the_knight_is_moved_to_$(to)
            .and().the_knight_captures_the_opposing_piece();
    }

    @TestTemplate
    @DataProvider(value = {
        "WHITE, D5, D6, C7",
        "WHITE, D5, D7, E7",
        "WHITE, D5, C5, B6",
        "WHITE, D5, F5, F6",
        "WHITE, D5, B5, B4",
        "WHITE, D5, E5, F4",
        "WHITE, D5, D4, C3",
        "WHITE, D5, D3, E3",
        "BLACK, E4, E5, D6",
        "BLACK, E4, E6, F6",
        "BLACK, E4, D4, C5",
        "BLACK, E4, F4, G5",
        "BLACK, E4, D4, C3",
        "BLACK, E4, G4, G3",
        "BLACK, E4, E3, D2",
        "BLACK, E4, E2, F2",
    })
    public void a_knight_can_skip_over_any_other_pieces_to_reach_its_destination_position(
        String color,
        String from,
        String obstructed,
        String to
    ) {
        stage
            .given().a_$_knight_in_$(color, from)
            .and().the_square_$_is_not_vacant(obstructed)
            .when().the_knight_is_moved_to_$(to)
            .and().the_knight_is_in_$(to);
    }

}
