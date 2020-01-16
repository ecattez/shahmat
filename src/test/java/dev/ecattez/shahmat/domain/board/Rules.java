package dev.ecattez.shahmat.domain.board;

import com.tngtech.jgiven.annotation.IsTag;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IsTag(name = "Rules")
@Retention( RetentionPolicy.RUNTIME )
public @interface Rules {}
