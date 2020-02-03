package dev.ecattez.shahmat.infra;

import dev.ecattez.shahmat.infra.controller.HalBoardResource;
import dev.ecattez.shahmat.infra.store.EventStore;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class RootResource {

    private final EventStore eventStore;

    public RootResource(EventStore eventStore) {
        this.eventStore = eventStore;
    }

    @GetMapping(produces = "application/prs.hal-forms+json")
    public RepresentationModel listBoards() {
        List<Link> boardLinks = eventStore.aggregateIds()
            .stream()
            .map(chessGameId -> HalBoardResource.getBoardLink(LinkRelation.of("boards"), chessGameId.value))
            .collect(Collectors.toList());

        return new RepresentationModel(boardLinks);
    }

}
