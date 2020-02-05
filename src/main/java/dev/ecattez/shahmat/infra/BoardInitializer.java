package dev.ecattez.shahmat.infra;

import dev.ecattez.shahmat.domain.command.InitBoard;
import dev.ecattez.shahmat.domain.game.ChessGame;
import dev.ecattez.shahmat.domain.game.GameType;
import dev.ecattez.shahmat.infra.aggregate.ChessGameId;
import dev.ecattez.shahmat.infra.controller.HalBoardResource;
import dev.ecattez.shahmat.infra.publisher.PubSub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;

import javax.annotation.PostConstruct;
import java.util.Collections;

@Configuration
public class BoardInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(BoardInitializer.class);

    private final PubSub pubSub;

    public BoardInitializer(PubSub pubSub) {
        this.pubSub = pubSub;
    }

    @PostConstruct
    private void init() {
        for (int i=0; i < 10; i++) {
            ChessGameId chessGameId = ChessGameId.newInstance();

            pubSub.dispatch(
                chessGameId,
                0L,
                ChessGame.initalizeBoard(
                    Collections.emptyList(),
                    new InitBoard(
                        GameType.CLASSICAL
                    )
                )
            );

            Link urlToBoard = HalBoardResource.getBoardLink(IanaLinkRelations.SELF, chessGameId.value);

            LOGGER.info("Test board initialized - " + urlToBoard);
        }
    }
}
