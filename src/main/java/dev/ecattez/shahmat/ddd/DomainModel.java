package dev.ecattez.shahmat.ddd;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Represents an Aggregate, i.e. a cluster of associated objects that are
 * treated as a unit for the purpose of data changes. External references are
 * restricted to one member of the Aggregate, designated as the root
 *
 * @see <a href="http://domaindrivendesign.org/node/88">Aggregate</a>
 */
@Retention(RetentionPolicy.CLASS)
@Inherited
@Documented
public @interface Aggregate {

}
