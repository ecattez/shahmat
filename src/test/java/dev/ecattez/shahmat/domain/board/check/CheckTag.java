package dev.ecattez.shahmat.domain.board.check;

import com.tngtech.jgiven.annotation.IsTag;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IsTag(name = "Check")
@Retention( RetentionPolicy.RUNTIME )
public @interface CheckTag {}
