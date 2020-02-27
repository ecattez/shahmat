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
@PawnTag
@MoveTag
@PromotionTag
public class PromotionSpec {

    @ScenarioStage
    private PromotionStage stage;

    @TestTemplate
    @DataProvider(value = {
        "WHITE, 7, 8",
        "BLACK, 2, 1",
    })
    public void a_pawn_must_be_promoted_when_it_reaches_the_other_side_of_the_chess_board(
        String color,
        Integer penultimateRank,
        Integer lastRank
    ) {
        stage
            .given().a_$_pawn_in_the_penultimate_rank(color, penultimateRank)
            .when().the_pawn_reaches_the_other_side_of_the_chess_board(lastRank)
            .and().the_pawn_s_owner_does_not_promote_the_pawn()
            .then().the_move_is_refused_because_promotion_must_be_done();
    }

    @TestTemplate
    @DataProvider(value = {
        "WHITE, 7, 8, KING",
        "WHITE, 7, 8, PAWN",
        "BLACK, 2, 1, KING",
        "BLACK, 2, 1, PAWN",
    })
    public void a_pawn_can_not_be_promoted_for_a_non_king(
        String color,
        Integer penultimateRank,
        Integer lastRank,
        String promotionType
    ) {
        stage
            .given().a_$_pawn_in_the_penultimate_rank(color, penultimateRank)
            .when().the_pawn_reaches_the_other_side_of_the_chess_board(lastRank)
            .and().the_pawn_s_owner_has_chosen_to_promote_it_with_a_$(promotionType)
            .then().the_promotion_is_refused()
            .with().$_as_reason("Piece can not promote");
    }

    @TestTemplate
    @DataProvider(value = {
        "WHITE, 7, 8, QUEEN",
        "WHITE, 7, 8, ROOK",
        "WHITE, 7, 8, BISHOP",
        "WHITE, 7, 8, KNIGHT",
        "BLACK, 2, 1, QUEEN",
        "BLACK, 2, 1, ROOK",
        "BLACK, 2, 1, BISHOP",
        "BLACK, 2, 1, KNIGHT",
    })
    public void a_pawn_may_be_promote_for_a_queen_a_rook_a_bishop_or_a_knight(
        String color,
        Integer penultimateRank,
        Integer lastRank,
        String promotionType
    ) {
        stage
            .given().a_$_pawn_in_the_penultimate_rank(color, penultimateRank)
            .when().the_pawn_reaches_the_other_side_of_the_chess_board(lastRank)
            .and().the_pawn_s_owner_has_chosen_to_promote_it_with_a_$(promotionType)
            .then().a_$_of_the_same_color_replaces_the_pawn(promotionType);
    }

    @TestTemplate
    @DataProvider(value = {
        "WHITE, B7, A8",
        "BLACK, E2, F1",
    })
    public void a_pawn_is_promoted_when_capturing_an_opposing_piece(
        String color,
        String from,
        String opponentLocation
    ) {
        stage
            .given().a_$_pawn_in_$(color, from)
            .and().an_opposing_piece_in_$(opponentLocation)
            .when().the_pawn_moves_to_$(opponentLocation)
            .and().the_pawn_s_owner_has_chosen_a_valid_promotion_type()
            .then().the_pawn_captures_the_opposing_piece()
            .and().the_promotion_piece_replaces_the_pawn();
    }

    @TestTemplate
    @DataProvider(value = {
        "KING, B2, A1",
        "QUEEN, A3, A1",
        "ROOK, A3, A1",
        "BISHOP, A3, C1",
        "KNIGHT, D2, F1",
    })
    public void only_a_pawn_can_be_promoted(
        String pieceType,
        String from,
        String to
    ) {
        stage
            .when().a_$_is_promoted(pieceType, from, to)
            .then().the_promotion_is_refused()
            .with().$_as_reason("Piece can not be promoted");
    }
}
