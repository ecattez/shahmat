package dev.ecattez.shahmat.domain.board.move;

import com.tngtech.jgiven.annotation.IsTag;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IsTag(name = "Move")
@Retention( RetentionPolicy.RUNTIME )
public @interface MoveTag {}
