package dev.ecattez.shahmat.infra.controller;

import dev.ecattez.shahmat.infra.aggregate.ChessGameId;
import dev.ecattez.shahmat.infra.projection.BoardInfo;
import dev.ecattez.shahmat.infra.store.EventStore;
import org.springframework.hateoas.CollectionModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class RootResource {

    private final EventStore eventStore;

    public RootResource(EventStore eventStore) {
        this.eventStore = eventStore;
    }

    @GetMapping(produces = "application/prs.hal-forms+json")
    public CollectionModel<BoardInfo> listBoards() {
        List<BoardInfo> boardsInfo = eventStore.aggregateIds()
            .stream()
            .map(this::toBoardInfo)
            .collect(Collectors.toList());

        return new CollectionModel<>(
            boardsInfo,
            linkTo(
                methodOn(RootResource.class).listBoards()
            ).withSelfRel()
        );
    }

    private BoardInfo toBoardInfo(ChessGameId id) {
        return new BoardInfo(
            id.value,
            "<not_implemented>",
            "<not_implemented>",
            "<not_implemented>",
            false
        );
    }

}
