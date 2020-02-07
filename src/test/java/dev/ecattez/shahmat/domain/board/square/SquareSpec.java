package dev.ecattez.shahmat.domain.board.square;

import com.tngtech.jgiven.junit5.JGivenExtension;
import com.tngtech.junit.dataprovider.DataProvider;
import com.tngtech.junit.dataprovider.DataProviderExtension;
import com.tngtech.junit.dataprovider.UseDataProviderExtension;
import dev.ecattez.shahmat.domain.board.Direction;
import dev.ecattez.shahmat.domain.board.Orientation;
import dev.ecattez.shahmat.domain.board.Rules;
import dev.ecattez.shahmat.domain.board.violation.FileOutsideOfBoard;
import dev.ecattez.shahmat.domain.board.violation.InvalidPosition;
import dev.ecattez.shahmat.domain.board.violation.RankOutsideOfBoard;
import dev.ecattez.shahmat.domain.board.violation.RulesViolation;
import dev.ecattez.shahmat.domain.board.violation.SquareOutsideOfBoard;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.stream.Stream;

@ExtendWith({
    JGivenExtension.class,
    DataProviderExtension.class,
    UseDataProviderExtension.class
})
@Rules
@SquareTag
public class SquareSpec {

    private Square square;
    private Square.File file;
    private Square.Rank rank;
    private RulesViolation violation;

    @Test
    public void file_goes_from_a_to_h() {
        Stream.of(new String[]{"A", "B", "C", "D", "E", "F", "G", "H"})
            .map(Square.File::valueOf)
            .forEach(file -> {
                Assertions
                    .assertThat(file)
                    .isNotNull();
            });
    }

    @Test
    public void rank_goes_from_1_to_8() {
        Stream.of(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8})
            .map(Square.Rank::valueOf)
            .forEach(rank -> {
                Assertions
                    .assertThat(rank)
                    .isNotNull();
            });
    }

    @Test
    public void square_is_composed_with_a_file_and_a_rank() {
        try {
            square = new Square(Square.File.A, Square.Rank.ONE);
        } catch (RulesViolation e) {
            violation = e;
        }

        Assertions
            .assertThat(square)
            .isNotNull();
    }

    @Test
    public void square_instanciation_fails_when_file_is_missing() {
        try {
            square = new Square(null, Square.Rank.ONE);
        } catch (RulesViolation e) {
            violation = e;
        }

        Assertions
            .assertThat(violation)
            .isInstanceOf(InvalidPosition.class);
    }

    @Test
    public void square_instanciation_fails_when_rank_is_missing() {
        try {
            square = new Square(Square.File.A, null);
        } catch (RulesViolation e) {
            violation = e;
        }

        Assertions
            .assertThat(violation)
            .isInstanceOf(InvalidPosition.class);
    }

    @Test
    public void square_is_file_first() {
        try {
            square = new Square("a1");
        } catch (RulesViolation e) {
            violation = e;
        }

        Assertions
            .assertThat(square)
            .isNotNull();
    }

    @Test
    public void square_instanciation_fails_when_rank_is_first() {
        try {
            square = new Square("1a");
        } catch (RulesViolation e) {
            violation = e;
        }

        Assertions
            .assertThat(violation)
            .isInstanceOf(InvalidPosition.class);
    }

    @Test
    public void previous_file_fails_when_outside_of_board() {
        try {
            file = Square.File.first().previous();
        } catch (RulesViolation e) {
            violation = e;
        }

        Assertions
            .assertThat(violation)
            .isInstanceOf(FileOutsideOfBoard.class);
    }

    @Test
    public void next_file_fails_when_outside_of_board() {
        try {
            file = Square.File.last().next();
        } catch (RulesViolation e) {
            violation = e;
        }

        Assertions
            .assertThat(violation)
            .isInstanceOf(FileOutsideOfBoard.class);
    }

    @Test
    public void previous_rank_fails_when_outside_of_board() {
        try {
            rank = Square.Rank.first().previous();
        } catch (RulesViolation e) {
            violation = e;
        }

        Assertions
            .assertThat(violation)
            .isInstanceOf(RankOutsideOfBoard.class);
    }

    @Test
    public void next_rank_fails_when_outside_of_board() {
        try {
            rank = Square.Rank.last().next();
        } catch (RulesViolation e) {
            violation = e;
        }

        Assertions
            .assertThat(violation)
            .isInstanceOf(RankOutsideOfBoard.class);
    }

    @TestTemplate
    @DataProvider(value = {
        "UPWARD, RANK, 1, BACKWARD",
        "UPWARD, RANK, 8, FORWARD",
        "UPWARD, FILE, A, SHIFT_LEFT",
        "UPWARD, FILE, H, SHIFT_RIGHT",
        "DOWNWARD, RANK, 1, FORWARD",
        "DOWNWARD, RANK, 8, BACKWARD",
        "DOWNWARD, FILE, A, SHIFT_RIGHT",
        "DOWNWARD, FILE, H, SHIFT_LEFT",
    })
    public void get_square_fails_when_outside_of_board(
        String orientation,
        String boardPart,
        String boardPartValue,
        String direction
    ) {
        try {
            Square square = new Square(
                "FILE".equals(boardPart)
                    ? Square.File.valueOf(boardPartValue)
                    : Square.File.A,
                "RANK".equals(boardPart)
                    ? Square.Rank.valueOf(Integer.parseInt(boardPartValue))
                    : Square.Rank.ONE
            );
            square.getNeighbour(
                Direction.valueOf(direction),
                Orientation.valueOf(orientation)
            );
        } catch (RulesViolation e) {
            violation = e;
        }

        Assertions
            .assertThat(violation)
            .isInstanceOf(SquareOutsideOfBoard.class);
    }

    @Test
    public void get_next_square_fails_when_outside_of_board() {
        Square.SquareIterator iterator = new Square.SquareIterator();
        while (iterator.hasNext()) {
            iterator.next();
        }
        try {
            iterator.next();
        } catch (SquareOutsideOfBoard e) {
            violation = e;
        }

        Assertions
            .assertThat(violation)
            .isInstanceOf(SquareOutsideOfBoard.class);
    }

}
