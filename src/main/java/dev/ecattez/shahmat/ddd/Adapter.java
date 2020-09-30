package dev.ecattez.shahmat.ddd;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Adapters act as a layer which serve the purpose of transforming the communication between various external actors
 * and application logic in such a way that both remain independent.
 * In hexagonal architecture all the primary and secondary actors interact with the application ports through adapters.
 *
 * @see <a href="http://codingcanvas.com/hexagonal-architecture/">Adapter</a>
 */
@Retention(RetentionPolicy.CLASS)
@Inherited
@Documented
public @interface Adapter {
}
