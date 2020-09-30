package fr.leroymerlin.configurators.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Defines a command to execute into the domaon.
 * Usually use a verb in the imperative tense.
 */
@Retention(RetentionPolicy.SOURCE)
@Documented
public @interface DomainCommand {
}
