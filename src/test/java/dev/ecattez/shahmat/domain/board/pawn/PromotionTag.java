package dev.ecattez.shahmat.domain.board.pawn;

import com.tngtech.jgiven.annotation.IsTag;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IsTag(name = "Promotion")
@Retention( RetentionPolicy.RUNTIME )
public @interface PromotionTag {}
