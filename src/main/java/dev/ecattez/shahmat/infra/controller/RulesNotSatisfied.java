package dev.ecattez.shahmat.infra.controller;

import dev.ecattez.shahmat.domain.board.violation.RulesViolation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RulesNotSatisfied extends RuntimeException {

    public RulesNotSatisfied(RulesViolation violation) {
        super(violation.getMessage());
    }

}
