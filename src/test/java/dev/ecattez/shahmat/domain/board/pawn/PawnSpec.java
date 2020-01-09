package dev.ecattez.shahmat.domain.board.pawn;

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
@PawnTag
public class PawnSpec {

    @ScenarioStage
    private PawnStage stage;

    @TestTemplate
    @DataProvider(value = {
        "WHITE, A2, A3",
        "BLACK, A7, A6",
    })
    public void pawn_can_only_move_directly_forward_one_square(
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
    public void pawn_is_stuck_when_the_square_is_not_vacant(
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
    public void pawn_can_move_directly_forward_two_squares_on_their_first_move_only(
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
    public void pawn_can_move_diagonally_forward_when_capturing_an_opponent_piece(
        String color,
        String from,
        String opponentLocation
    ) {
        stage
            .given().a_$_pawn_in_$(color, from)
            .and().an_opponent_piece_in_$(opponentLocation)
            .when().the_opponent_piece_is_captured_by_the_pawn()
            .then().the_pawn_is_in_$(opponentLocation)
            .and().the_opponent_piece_is_removed_from_the_game();
    }

    @TestTemplate
    @DataProvider(value = {
        "WHITE, A7, A8",
        "WHITE, B7, B8",
        "WHITE, C7, C8",
        "WHITE, D7, D8",
        "WHITE, E7, E8",
        "WHITE, F7, F8",
        "WHITE, G7, G8",
        "WHITE, H7, H8",
        "BLACK, A2, A1",
        "BLACK, B2, B1",
        "BLACK, C2, C1",
        "BLACK, D2, D1",
        "BLACK, E2, E1",
        "BLACK, F2, F1",
        "BLACK, G2, G1",
        "BLACK, H2, H1",
    })
    public void trade_may_be_proposed_when_a_pawn_reaches_the_other_side_of_the_chess_board(
        String color,
        String from,
        String to
    ) {
        stage
            .given().a_$_pawn_in_$(color, from)
            .when().the_pawn_is_moved_forward()
            .then().a_trade_is_proposed_in_$(to);
    }

    @Test
    public void pawn_can_not_be_traded_for_a_king() {
        stage
            .given().a_pawn_in_the_other_side_of_the_chess_board()
            .when().the_pawn_is_traded_for_a_king()
            .then().the_trade_is_refused();
    }

    @TestTemplate
    @DataProvider(value = {
        "QUEEN",
        "KNIGHT",
        "ROOK",
        "BISHOP",
        "PAWN"
    })
    public void pawn_may_be_trade_for_a_non_king_piece(
        String tradeType
    ) {
        stage
            .given().a_pawn_in_the_other_side_of_the_chess_board()
            .when().the_pawn_is_traded_for(tradeType)
            .then().a_$_replace_the_pawn(tradeType);
    }

}
