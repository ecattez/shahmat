package dev.ecattez.shahmat.domain.board.square;

import com.tngtech.jgiven.annotation.IsTag;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IsTag(name = "Square")
@Retention( RetentionPolicy.RUNTIME )
public @interface SquareTag {}
