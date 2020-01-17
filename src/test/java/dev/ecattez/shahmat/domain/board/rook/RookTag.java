package dev.ecattez.shahmat.domain.board.rook;

import com.tngtech.jgiven.annotation.IsTag;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IsTag(name = "Rook")
@Retention( RetentionPolicy.RUNTIME )
public @interface RookTag {}
