package dev.ecattez.shahmat.domain.board.check;

import com.tngtech.jgiven.annotation.ScenarioStage;
import com.tngtech.jgiven.junit5.JGivenExtension;
import com.tngtech.junit.dataprovider.DataProvider;
import com.tngtech.junit.dataprovider.DataProviderExtension;
import com.tngtech.junit.dataprovider.UseDataProviderExtension;
import dev.ecattez.shahmat.domain.board.Rules;
import dev.ecattez.shahmat.domain.board.init.BoardTag;
import dev.ecattez.shahmat.domain.board.move.MoveTag;
import dev.ecattez.shahmat.domain.board.piece.king.KingTag;
import org.junit.jupiter.api.TestTemplate;
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
public class CheckSpec {

    @ScenarioStage
    private CheckStage stage;

    @TestTemplate
    @DataProvider(value = {
        "G4, QUEEN, D8, D4",
        "F4, BISHOP, F8, D6",
        "E4, ROOK, A8, A4",
        "D5, KNIGHT, G8, F6",
        "D5, PAWN, C7, C6",
    })
    public void a_king_becomes_threatened(
        String kingLocation,
        String opponentType,
        String opponentLocation,
        String to
    ) {
        stage
            .given().a_king_in_$(kingLocation)
            .and().an_opposing_$_in_$(opponentType, opponentLocation)
            .when().the_opposing_piece_is_moved_to_$(to)
            .then().the_king_is_in_the_bounds_of_movement_of_the_opposing_piece()
            .and().the_king_is_in_check();
    }

    @TestTemplate
    @DataProvider(value = {
        "D3, QUEEN, D8, D4",
        "D5, BISHOP, F8, D6",
        "B3, ROOK, A8, A4",
        "E7, KNIGHT, G8, F6",
        "D4, PAWN, C6, D5",
    })
    public void a_king_can_not_move_to_a_square_where_it_could_be_in_check(
        String kingLocation,
        String opponentType,
        String opponentLocation,
        String to
    ) {
        stage
            .given().a_king_in_$(kingLocation)
            .and().an_opposing_$_in_$(opponentType, opponentLocation)
            .when().the_king_is_moved_to_$(to)
            .then().the_move_is_refused();
    }

    @TestTemplate
    @DataProvider(value = {
        "D3, QUEEN, D8, C3",
        "E6, BISHOP, C4, F6",
        "E3, ROOK, B3, E4",
        "E5, KNIGHT, G6, E6",
        "E3, PAWN, D4, D3",
        "D7, QUEEN, D8, D8",
        "B4, BISHOP, C4, C4",
        "B2, ROOK, B3, B3",
        "E3, PAWN, D4, D4",
    })
    public void a_king_can_move_to_a_square_where_it_could_escape_from_check(
        String kingLocation,
        String opponentType,
        String opponentLocation,
        String to
    ) {
        stage
            .given().a_king_in_check_in_$(kingLocation, opponentType, opponentLocation)
            .and().an_opposing_$_in_$(opponentType, opponentLocation)
            .with().the_square_$_not_in_its_bounds_of_movement(to)
            .when().the_king_is_moved_to_$(to)
            .then().the_king_is_no_longer_in_check();
    }

    @TestTemplate
    @DataProvider(value = {
        "E1, QUEEN, E8, BISHOP, D3, E4",
        "E1, BISHOP, B4, QUEEN, D1, D2",
        "E1, ROOK, E8, KNIGHT, D2, E4",
        "E1, KNIGHT, D3, ROOK, H3, D3",
        "E2, PAWN, F3, PAWN, G2, F3",
        "E1, QUEEN, E8, BISHOP, A4, E8",
        "E1, BISHOP, B4, ROOK, B1, B4",
        "E1, ROOK, E8, KNIGHT, D6, E8",
    })
    public void king_s_allies_can_move_to_help_the_king_escaping_from_check(
        String kingLocation,
        String opponentType,
        String opponentLocation,
        String allyType,
        String allyLocation,
        String to
    ) {
        stage
            .given().a_king_in_check_in_$(kingLocation, opponentType, opponentLocation)
            .and().an_opposing_$_in_$(opponentType, opponentLocation)
            .and().a_$_ally_of_the_king_in_$(allyType, allyLocation)
            .when().the_ally_is_moved_to_$(to)
            .then().the_king_is_no_longer_in_check();
    }

    @TestTemplate
    @DataProvider(value = {
        "E1, QUEEN, E8, BISHOP, D3, C2",
        "E1, BISHOP, B4, QUEEN, D1, D6",
        "E1, ROOK, E8, KNIGHT, D2, C4",
        "E1, KNIGHT, D3, ROOK, H3, G3",
        "E2, PAWN, F3, PAWN, G2, G3",
    })
    public void king_s_allies_can_not_move_from_a_square_that_could_make_the_king_in_check_if_vacant(
        String kingLocation,
        String opponentType,
        String opponentLocation,
        String allyType,
        String allyLocation,
        String to
    ) {
        stage
            .given().a_king_in_check_in_$(kingLocation, opponentType, opponentLocation)
            .and().an_opposing_$_in_$(opponentType, opponentLocation)
            .and().a_$_ally_of_the_king_in_$(allyType, allyLocation)
            .when().the_ally_is_moved_to_$(to)
            .then().the_move_is_refused();
    }

    @TestTemplate
    @DataProvider(value = {
        "E1, B2, ROOK",
        "E1, B2, QUEEN",
        "E2, F2, BISHOP",
        "E2, G2, KNIGHT",
    })
    public void a_king_can_be_put_in_check_because_of__pawn_promotion(
        String kingLocation,
        String opponentLocation,
        String promotionType
    ) {
        stage
            .given().a_king_in_$(kingLocation)
            .and().an_opposing_pawn_in_$(opponentLocation)
            .when().the_opposing_pawn_reaches_the_other_side_of_the_chess_board()
            .and().it_is_promoted_to_$(promotionType)
            .then().the_king_is_in_check();
    }

}
