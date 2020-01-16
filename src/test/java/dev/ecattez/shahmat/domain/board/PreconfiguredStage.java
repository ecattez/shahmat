package dev.ecattez.shahmat.domain.board;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.AfterScenario;
import com.tngtech.jgiven.annotation.BeforeStage;
import dev.ecattez.shahmat.board.PieceBox;
import dev.ecattez.shahmat.board.PieceFactory;
import dev.ecattez.shahmat.board.violation.RulesViolation;
import dev.ecattez.shahmat.event.BoardEvent;
import dev.ecattez.shahmat.game.ChessGame;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class PreconfiguredStage<T> {

    protected PieceFactory pieceFactory;
    protected List<BoardEvent> returnedEvents;
    protected List<BoardEvent> history;
    protected RulesViolation violation;

    @BeforeStage
    public void init() {
        this.pieceFactory = PieceBox.getInstance();
        this.history = new LinkedList<>();
        this.returnedEvents = new LinkedList<>();
    }

    @AfterScenario
    public void after() {
        System.out.println("Before command");
        System.out.println(
            ChessGame.replay(
                history
            ).toString()
        );
        System.out.println();

        System.out.println("After command");
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

}
