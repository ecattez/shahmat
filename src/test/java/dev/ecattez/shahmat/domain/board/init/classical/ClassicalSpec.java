package dev.ecattez.shahmat.domain.board.init.classical;

import com.tngtech.jgiven.annotation.ScenarioStage;
import com.tngtech.jgiven.junit5.JGivenExtension;
import com.tngtech.junit.dataprovider.DataProviderExtension;
import com.tngtech.junit.dataprovider.UseDataProviderExtension;
import dev.ecattez.shahmat.domain.board.Rules;
import dev.ecattez.shahmat.domain.board.init.BoardTag;
import dev.ecattez.shahmat.domain.board.init.GameTag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({
    JGivenExtension.class,
    DataProviderExtension.class,
    UseDataProviderExtension.class
})
@Rules
@BoardTag
@GameTag
@ClassicalTag
public class ClassicalSpec {

    @ScenarioStage
    private ClassicalStage stage;

    @Test
    public void a_classical_board_game_initialization() {
        stage
            .given().a_classical_chess_game()
            .when().the_board_is_initialized()
            .then().a_$_$_is_in_$("WHITE", "ROOK", "A1")
            .and().a_$_$_is_in_$("WHITE", "KNIGHT", "B1")
            .and().a_$_$_is_in_$("WHITE", "BISHOP", "C1")
            .and().a_$_$_is_in_$("WHITE", "QUEEN", "D1")
            .and().a_$_$_is_in_$("WHITE", "KING", "E1")
            .and().a_$_$_is_in_$("WHITE", "BISHOP", "F1")
            .and().a_$_$_is_in_$("WHITE", "KNIGHT", "G1")
            .and().a_$_$_is_in_$("WHITE", "ROOK", "H1")
            .and().a_$_$_is_in_$("WHITE", "PAWN", "A2")
            .and().a_$_$_is_in_$("WHITE", "PAWN", "B2")
            .and().a_$_$_is_in_$("WHITE", "PAWN", "C2")
            .and().a_$_$_is_in_$("WHITE", "PAWN", "D2")
            .and().a_$_$_is_in_$("WHITE", "PAWN", "E2")
            .and().a_$_$_is_in_$("WHITE", "PAWN", "F2")
            .and().a_$_$_is_in_$("WHITE", "PAWN", "G2")
            .and().a_$_$_is_in_$("WHITE", "PAWN", "H2")
            .and().a_$_$_is_in_$("BLACK", "ROOK", "A8")
            .and().a_$_$_is_in_$("BLACK", "KNIGHT", "B8")
            .and().a_$_$_is_in_$("BLACK", "BISHOP", "C8")
            .and().a_$_$_is_in_$("BLACK", "QUEEN", "D8")
            .and().a_$_$_is_in_$("BLACK", "KING", "E8")
            .and().a_$_$_is_in_$("BLACK", "BISHOP", "F8")
            .and().a_$_$_is_in_$("BLACK", "KNIGHT", "G8")
            .and().a_$_$_is_in_$("BLACK", "ROOK", "H8")
            .and().a_$_$_is_in_$("BLACK", "PAWN", "A7")
            .and().a_$_$_is_in_$("BLACK", "PAWN", "B7")
            .and().a_$_$_is_in_$("BLACK", "PAWN", "C7")
            .and().a_$_$_is_in_$("BLACK", "PAWN", "D7")
            .and().a_$_$_is_in_$("BLACK", "PAWN", "E7")
            .and().a_$_$_is_in_$("BLACK", "PAWN", "F7")
            .and().a_$_$_is_in_$("BLACK", "PAWN", "G7")
            .and().a_$_$_is_in_$("BLACK", "PAWN", "H7")
            .and().the_board_is_ready_to_play_with();
    }

    @Test
    public void a_board_already_initialized_can_not_be_initialized_again() {
        stage
            .given().a_classical_chess_game()
            .and().the_board_is_already_initialized()
            .when().the_board_is_initialized()
            .then().the_initialization_is_refused();
    }

}
