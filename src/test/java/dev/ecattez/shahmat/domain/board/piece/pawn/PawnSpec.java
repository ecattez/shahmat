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
@MoveTag
@PawnTag
public class PawnSpec {

    @ScenarioStage
    private PawnStage stage;

    @TestTemplate
    @DataProvider(value = {
        "WHITE, A2, A3",
        "BLACK, A7, A6",
    })
    public void a_pawn_can_only_move_directly_forward_one_square(
        String color,
        String from,
        String to
    ) {
        stage
            .given().a_$_pawn_in_$(color, from)
            .and().$_is_vacant(to)
            .when().the_pawn_is_moved_forward()
            .then().the_pawn_is_in_$(to);
    }

    @TestTemplate
    @DataProvider(value = {
        "WHITE, A2, A3",
        "BLACK, A7, A6",
    })
    public void a_pawn_is_stuck_when_the_square_is_not_vacant(
        String color,
        String from,
        String to
    ) {
        stage
            .given().a_$_pawn_in_$(color, from)
            .and().$_is_not_vacant(to)
            .when().the_pawn_is_moved_forward()
            .then().the_pawn_stays_in_$(from);
    }

    @TestTemplate
    @DataProvider(value = {
        "WHITE, A2, A3, A4",
        "WHITE, B2, B3, B4",
        "WHITE, C2, C3, C4",
        "WHITE, D2, D3, D4",
        "WHITE, E2, E3, E4",
        "WHITE, F2, F3, F4",
        "WHITE, G2, G3, G4",
        "WHITE, H2, H3, H4",
        "BLACK, A7, A6, A5",
        "BLACK, B7, B6, B5",
        "BLACK, C7, C6, C5",
        "BLACK, D7, D6, D5",
        "BLACK, E7, E6, E5",
        "BLACK, F7, F6, F5",
        "BLACK, G7, G6, G5",
        "BLACK, H7, H6, H5",
    })
    public void a_pawn_can_move_directly_forward_two_squares_on_their_first_move_only(
        String color,
        String from,
        String forward,
        String to
    ) {
        stage
            .given().a_$_pawn_in_$(color, from)
            .and().$_is_vacant(forward)
            .and().$_is_vacant(to)
            .when().the_pawn_is_moved_forward_two_squares()
            .then().the_pawn_is_in_$(to);
    }

    @TestTemplate
    @DataProvider(value = {
        "WHITE, B3, A4",
        "WHITE, B3, C4",
        "BLACK, B5, C4",
        "BLACK, B5, A4",
    })
    public void a_pawn_can_move_diagonally_forward_when_capturing_an_opposing_piece(
        String color,
        String from,
        String opponentLocation
    ) {
        stage
            .given().a_$_pawn_in_$(color, from)
            .and().an_opposing_piece_in_$(opponentLocation)
            .when().the_pawn_is_moved_to_$(opponentLocation)
            .then().the_pawn_captures_the_opposing_piece();
    }

}
