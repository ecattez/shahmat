package dev.ecattez.shahmat.infra.controller;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class RootResource {

    @GetMapping(produces = "application/prs.hal-forms+json")
    public RepresentationModel<?> home() {
        return new RepresentationModel<>(
            linkTo(
                methodOn(RootResource.class).home()
            ).withSelfRel()
        ).add(
            linkTo(
                methodOn(HalBoardResource.class).listBoards()
            ).withRel("boards")
        );
    }

}
