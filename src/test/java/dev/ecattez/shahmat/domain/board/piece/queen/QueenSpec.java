package dev.ecattez.shahmat.domain.board.piece.queen;

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
@QueenTag
public class QueenSpec {

    @ScenarioStage
    private QueenStage stage;

    @TestTemplate
    @DataProvider(value = {
        "WHITE, A1, 4, FORWARD, A5",
        "WHITE, A5, 2, SHIFT_RIGHT, C5",
        "WHITE, C5, 2, SHIFT_LEFT, A5",
        "WHITE, A5, 4, BACKWARD, A1",
        "WHITE, E5, 3, FORWARD_LEFT, B8",
        "WHITE, B8, 2, BACKWARD_RIGHT, D6",
        "WHITE, D6, 1, FORWARD_RIGHT, E7",
        "WHITE, E7, 4, BACKWARD_LEFT, A3",
        "BLACK, A8, 4, FORWARD, A4",
        "BLACK, A4, 2, SHIFT_LEFT, C4",
        "BLACK, C4, 2, SHIFT_RIGHT, A4",
        "BLACK, A4, 4, BACKWARD, A8",
        "BLACK, D5, 3, FORWARD_LEFT, G2",
        "BLACK, G2, 2, BACKWARD_RIGHT, E4",
        "BLACK, E4, 2, FORWARD_RIGHT, C2",
        "BLACK, C2, 4, BACKWARD_LEFT, G6",
    })
    public void a_queen_can_move_in_any_direction_on_a_straight_or_diagonal_path(
        String color,
        String from,
        int times,
        String direction,
        String to
    ) {
        stage
            .given().a_$_queen_in_$(color, from)
            .and().the_squares_between_$_and_$_are_vacant(from, to)
            .when().the_queen_is_moved_$_$(times, direction)
            .then().the_queen_is_in_$(to);
    }

    @TestTemplate
    @DataProvider(value = {
        "WHITE, A1, A3, 4, FORWARD",
        "WHITE, A5, C5, 2, SHIFT_RIGHT",
        "WHITE, E5, C5, 2, SHIFT_LEFT",
        "WHITE, A5, A3, 4, BACKWARD",
        "WHITE, A1, C3, 4, FORWARD_RIGHT",
        "WHITE, E6, B3, 4, BACKWARD_LEFT",
        "WHITE, C3, B4, 1, FORWARD_LEFT",
        "WHITE, B6, C5, 2, BACKWARD_RIGHT",
        "BLACK, A8, A6, 4, FORWARD",
        "BLACK, A4, C4, 2, SHIFT_LEFT",
        "BLACK, E4, C4, 2, SHIFT_RIGHT",
        "BLACK, A4, A6, 4, BACKWARD",
        "BLACK, E5, C3, 3, FORWARD_RIGHT",
        "BLACK, D5, E6, 2, BACKWARD_LEFT",
        "BLACK, E5, G3, 3, FORWARD_LEFT",
        "BLACK, D5, B7, 2, BACKWARD_RIGHT",
    })
    public void a_queen_can_not_move_beyond_an_obstructed_path(
        String color,
        String from,
        String obstructed,
        int times,
        String direction
    ) {
        stage
            .given().a_$_queen_in_$(color, from)
            .and().the_square_$_is_not_vacant(obstructed)
            .when().the_queen_is_moved_$_$(times, direction)
            .then().the_move_is_refused();
    }

    @TestTemplate
    @DataProvider(value = {
        "WHITE, A1, A3",
        "WHITE, A5, C5",
        "WHITE, E5, C5",
        "WHITE, A5, A3",
        "WHITE, E5, B2",
        "WHITE, C4, D3",
        "WHITE, C6, E8",
        "WHITE, H1, A8",
        "BLACK, A8, A6",
        "BLACK, A4, C4",
        "BLACK, E4, C4",
        "BLACK, A4, A6",
        "BLACK, E5, B2",
        "BLACK, C4, D3",
        "BLACK, C6, E8",
        "BLACK, H1, A8",
    })
    public void a_queen_can_capture_an_opposing_piece_that_obstruct_its_way(
        String color,
        String from,
        String to
    ) {
        stage
            .given().a_$_queen_in_$(color, from)
            .and().an_opposing_piece_is_in_$(to)
            .when().the_queen_is_moved_to_$(to)
            .then().the_queen_captures_the_opposing_piece();
    }

}
