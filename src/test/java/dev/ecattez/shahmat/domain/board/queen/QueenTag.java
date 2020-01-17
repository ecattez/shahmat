package dev.ecattez.shahmat.domain.board.queen;

import com.tngtech.jgiven.annotation.IsTag;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IsTag(name = "Queen")
@Retention( RetentionPolicy.RUNTIME )
public @interface QueenTag {}
