package dev.ecattez.shahmat.domain.board.piece.rook;

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
@RookTag
public class RookSpec {

    @ScenarioStage
    private RookStage stage;

    @TestTemplate
    @DataProvider(value = {
        "WHITE, A1, 4, FORWARD, A5",
        "WHITE, A5, 2, SHIFT_RIGHT, C5",
        "WHITE, C5, 2, SHIFT_LEFT, A5",
        "WHITE, A5, 4, BACKWARD, A1",
        "BLACK, A8, 4, FORWARD, A4",
        "BLACK, A4, 2, SHIFT_LEFT, C4",
        "BLACK, C4, 2, SHIFT_RIGHT, A4",
        "BLACK, A4, 4, BACKWARD, A8",
    })
    public void a_rook_can_move_horizontally_and_vertically_while_it_is_not_obstructed(
        String color,
        String from,
        int times,
        String direction,
        String to
    ) {
        stage
            .given().a_$_rook_in_$(color, from)
            .and().the_squares_between_$_and_$_are_vacant(from, to)
            .when().the_rook_is_moved_$_$(times, direction)
            .then().the_rook_is_in_$(to);
    }

    @TestTemplate
    @DataProvider(value = {
        "WHITE, A1, A3, 4, FORWARD",
        "WHITE, A5, C5, 2, SHIFT_RIGHT",
        "WHITE, E5, C5, 2, SHIFT_LEFT",
        "WHITE, A5, A3, 4, BACKWARD",
        "BLACK, A8, A6, 4, FORWARD",
        "BLACK, A4, C4, 2, SHIFT_LEFT",
        "BLACK, E4, C4, 2, SHIFT_RIGHT",
        "BLACK, A4, A6, 4, BACKWARD",
    })
    public void a_rook_can_not_move_beyond_an_obstructed_path(
        String color,
        String from,
        String obstructed,
        int times,
        String direction
    ) {
        stage
            .given().a_$_rook_in_$(color, from)
            .and().the_square_$_is_not_vacant(obstructed)
            .when().the_rook_is_moved_$_$(times, direction)
            .then().the_move_is_refused();
    }

    @TestTemplate
    @DataProvider(value = {
        "WHITE, A1, A3",
        "WHITE, A5, C5",
        "WHITE, E5, C5",
        "WHITE, A5, A3",
        "BLACK, A8, A6",
        "BLACK, A4, C4",
        "BLACK, E4, C4",
        "BLACK, A4, A6",
    })
    public void a_rook_can_capture_an_opposing_piece_that_obstruct_its_way(
        String color,
        String from,
        String to
    ) {
        stage
            .given().a_$_rook_in_$(color, from)
            .and().an_opposing_piece_is_in_$(to)
            .when().the_rook_is_moved_to_$(to)
            .then().the_rook_captures_the_opposing_piece();
    }

}
