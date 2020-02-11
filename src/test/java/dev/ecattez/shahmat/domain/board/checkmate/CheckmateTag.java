package dev.ecattez.shahmat.domain.board.checkmate;

import com.tngtech.jgiven.annotation.IsTag;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IsTag(name = "Checkmate")
@Retention( RetentionPolicy.RUNTIME )
public @interface CheckmateTag {}
