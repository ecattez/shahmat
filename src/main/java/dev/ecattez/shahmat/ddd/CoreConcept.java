package fr.leroymerlin.configurators.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Represents the central parts of the application.
 * It is used in the domain layer, on aggregates and value objects.
 */
@Retention(RetentionPolicy.CLASS)
@Inherited
@Documented
public @interface CoreConcept {
}
