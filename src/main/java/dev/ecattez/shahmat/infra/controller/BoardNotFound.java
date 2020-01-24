package dev.ecattez.shahmat.infra.controller;

import dev.ecattez.shahmat.infra.aggregate.ChessGameId;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BoardNotFound extends RuntimeException {

    public BoardNotFound(ChessGameId id) {
        super("Board " + id + " not found or does not exist");
    }

}
