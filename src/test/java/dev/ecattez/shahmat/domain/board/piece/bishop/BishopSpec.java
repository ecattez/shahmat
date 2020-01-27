package dev.ecattez.shahmat.domain.board.piece.bishop;

import com.tngtech.jgiven.annotation.ScenarioStage;
import com.tngtech.jgiven.junit5.JGivenExtension;
import com.tngtech.junit.dataprovider.DataProvider;
import com.tngtech.junit.dataprovider.DataProviderExtension;
import com.tngtech.junit.dataprovider.UseDataProviderExtension;
import dev.ecattez.shahmat.domain.board.square.Square;
import dev.ecattez.shahmat.domain.board.Rules;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({
    JGivenExtension.class,
    DataProviderExtension.class,
    UseDataProviderExtension.class
})
@Rules
@BishopTag
public class BishopSpec {

    @ScenarioStage
    private BishopStage stage;

    @TestTemplate
    @DataProvider(value = {
        "WHITE, E5, 3, FORWARD_LEFT, B8",
        "WHITE, B8, 2, BACKWARD_RIGHT, D6",
        "WHITE, D6, 1, FORWARD_RIGHT, E7",
        "WHITE, E7, 4, BACKWARD_LEFT, A3",
        "BLACK, D5, 3, FORWARD_LEFT, G2",
        "BLACK, G2, 2, BACKWARD_RIGHT, E4",
        "BLACK, E4, 2, FORWARD_RIGHT, C2",
        "BLACK, C2, 4, BACKWARD_LEFT, G6",
    })
    public void bishop_can_move_in_any_direction_diagonally_so_long_as_it_is_obstructed_by_another_piece(
        String color,
        String from,
        int times,
        String direction,
        String to
    ) {
        stage
            .given().a_$_bishop_in_$(color, from)
            .and().the_squares_between_$_and_$_are_vacant(from, to)
            .when().the_bishop_is_moved_$_$(times, direction)
            .then().the_bishop_is_in_$(to);
    }

    @TestTemplate
    @DataProvider(value = {
        "WHITE, A1, C3, 4, FORWARD_RIGHT",
        "WHITE, E6, B3, 4, BACKWARD_LEFT",
        "WHITE, C3, B4, 1, FORWARD_LEFT",
        "WHITE, B6, C5, 2, BACKWARD_RIGHT",
        "BLACK, E5, C3, 3, FORWARD_RIGHT",
        "BLACK, D5, E6, 2, BACKWARD_LEFT",
        "BLACK, E5, G3, 3, FORWARD_LEFT",
        "BLACK, D5, B7, 2, BACKWARD_RIGHT",
    })
    public void bishop_can_not_move_beyond_an_obstructed_path(
        String color,
        String from,
        Square obstructedSquare,
        int times,
        String direction
    ) {
        stage
            .given().a_$_bishop_in_$(color, from)
            .and().the_square_$_is_not_vacant(obstructedSquare)
            .when().the_bishop_is_moved_$_$(times, direction)
            .then().the_move_is_refused();
    }

    @TestTemplate
    @DataProvider(value = {
        "WHITE, E5, B2",
        "WHITE, C4, D3",
        "WHITE, C6, E8",
        "WHITE, H1, A8",
        "BLACK, E5, B2",
        "BLACK, C4, D3",
        "BLACK, C6, E8",
        "BLACK, H1, A8",
    })
    public void bishop_can_capture_an_opponent_piece_that_obstruct_its_way(
        String color,
        String from,
        String to
    ) {
        stage
            .given().a_$_bishop_in_$(color, from)
            .and().an_opponent_piece_is_in_$(to)
            .when().the_bishop_is_moved_to_$(to)
            .and().the_bishop_captures_the_opponent_piece();
    }

}
