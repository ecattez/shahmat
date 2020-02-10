package dev.ecattez.shahmat.domain.board.init;

import com.tngtech.jgiven.annotation.IsTag;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IsTag(name = "Game")
@Retention( RetentionPolicy.RUNTIME )
public @interface GameTag {}
