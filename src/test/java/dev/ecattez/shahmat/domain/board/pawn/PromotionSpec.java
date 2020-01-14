package dev.ecattez.shahmat.domain.board.pawn;

import com.tngtech.jgiven.annotation.ScenarioStage;
import com.tngtech.junit.dataprovider.DataProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;

public class PromotionSpec {

    @ScenarioStage
    private PromotionStage stage;

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
    public void promotion_may_be_proposed_when_a_pawn_reaches_the_other_side_of_the_chess_board(
        String color,
        String from,
        String to
    ) {
        stage
            .given().a_$_pawn_in_$(color, from)
            .when().the_pawn_is_moved_forward()
            .then().a_promotion_is_proposed_in_$(to);
    }

    @Test
    public void pawn_can_not_be_promoted_for_a_king() {
        stage
            .given().a_pawn_in_the_other_side_of_the_chess_board()
            .when().the_pawn_is_promoted_for_a_king()
            .then().the_promotion_is_refused();
    }

    @TestTemplate
    @DataProvider(value = {
        "QUEEN",
        "KNIGHT",
        "ROOK",
        "BISHOP",
        "PAWN"
    })
    public void pawn_may_be_promoted_for_a_non_king_piece(
        String promotionType
    ) {
        stage
            .given().a_pawn_in_the_other_side_of_the_chess_board()
            .when().the_pawn_is_promoted_for(promotionType)
            .then().a_$_replaces_the_pawn(promotionType);
    }

}
