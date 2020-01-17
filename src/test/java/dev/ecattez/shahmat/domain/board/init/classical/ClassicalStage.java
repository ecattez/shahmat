package dev.ecattez.shahmat.domain.board.init.classical;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.AfterScenario;
import com.tngtech.jgiven.annotation.BeforeStage;
import dev.ecattez.shahmat.board.PieceBox;
import dev.ecattez.shahmat.board.PieceColor;
import dev.ecattez.shahmat.board.PieceFactory;
import dev.ecattez.shahmat.board.PieceType;
import dev.ecattez.shahmat.board.Square;
import dev.ecattez.shahmat.command.InitBoard;
import dev.ecattez.shahmat.event.BoardEvent;
import dev.ecattez.shahmat.event.PiecePositioned;
import dev.ecattez.shahmat.game.BoardAlreadyInitialized;
import dev.ecattez.shahmat.game.ChessGame;
import dev.ecattez.shahmat.game.GameType;
import org.assertj.core.api.Assertions;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ClassicalStage extends Stage<ClassicalStage> {

    private GameType gameType;

    private PieceFactory pieceFactory;
    private List<BoardEvent> returnedEvents;
    private List<BoardEvent> history;
    private BoardAlreadyInitialized violation;

    @BeforeStage
    public void init() {
        this.pieceFactory = PieceBox.getInstance();
        this.history = new LinkedList<>();
        this.returnedEvents = new LinkedList<>();
    }

    @AfterScenario
    public void after() {
        System.out.println("Before command: " + gameType);
        System.out.println(
            ChessGame.replay(
                history
            ).toString()
        );
        System.out.println();

        System.out.println("After command: " + gameType);
        System.out.println(
            ChessGame.replay(
                Stream.of(history, returnedEvents)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList())
            ).toString()
        );
        if (violation != null) {
            System.out.println(violation.getMessage());
        }
        System.out.println();
    }


    public ClassicalStage a_classical_chess_game() {
        this.gameType = GameType.CLASSICAL;
        return self();
    }

    public ClassicalStage the_board_is_already_initialized() {
        history.addAll(
            ChessGame.initalizeBoard(
                history,
                new InitBoard(gameType)
            )
        );
        return self();
    }

    public ClassicalStage the_board_is_initialized() {
        try {
            this.returnedEvents = ChessGame.initalizeBoard(
                history,
                new InitBoard(gameType)
            );
        } catch (BoardAlreadyInitialized e) {
            violation = e;
        }
        return self();
    }

    public ClassicalStage a_$_$_is_in_$(String color, String type, String location) {
        Assertions
            .assertThat(returnedEvents)
            .contains(
                new PiecePositioned(
                    pieceFactory.createPiece(
                        PieceType.valueOf(type),
                        PieceColor.valueOf(color)
                    ),
                    new Square(location)
                )
            );
        return self();
    }

    public ClassicalStage the_initialization_is_refused() {
        Assertions
            .assertThat(violation)
            .isInstanceOf(BoardAlreadyInitialized.class);
        return self();
    }

}
