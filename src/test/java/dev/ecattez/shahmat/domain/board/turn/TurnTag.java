package dev.ecattez.shahmat.domain.board.turn;

import com.tngtech.jgiven.annotation.IsTag;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IsTag(name = "Turn")
@Retention( RetentionPolicy.RUNTIME )
public @interface TurnTag {}
