package dev.ecattez.shahmat.domain.board;

import dev.ecattez.shahmat.domain.event.ChessEvent;
import dev.ecattez.shahmat.domain.game.BoardDecision;

import java.util.List;

public class BeforeAfterOutput {

    public static void display(Object subject, List<ChessEvent> history, List<ChessEvent> returnedEvents, Exception violation) {
        BoardStringFormatter formatter = new BoardStringFormatter();

        System.out.println("Before command: " + subject);
        System.out.println(
            formatter.format(
                BoardDecision.replay(history)
            )
        );
        System.out.println();

        System.out.println("After command: " + subject);
        System.out.println(
            formatter.format(
                BoardDecision.replay(history, returnedEvents)
            )
        );
        if (violation != null) {
            System.out.println(violation.getMessage());
        }
        history
            .forEach(System.out::println);
        returnedEvents
            .forEach(System.out::println);
        System.out.println();
    }

    private BeforeAfterOutput(){}

}
