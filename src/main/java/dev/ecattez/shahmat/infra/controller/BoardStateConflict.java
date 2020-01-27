package dev.ecattez.shahmat.infra.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class BoardStateConflict extends RuntimeException {

    public BoardStateConflict() {
        super("Board has changed since your last command");
    }

}
