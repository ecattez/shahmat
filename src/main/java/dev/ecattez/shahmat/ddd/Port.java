package dev.ecattez.shahmat.ddd;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Represents a port, the medium through which business logic is accessed.
 * Port is a use case boundary i.e. Ports correspond to use-cases in the application.
 *
 * @see <a
 * href="http://codingcanvas.com/hexagonal-architecture/">Port</a>
 */
@Retention(RetentionPolicy.CLASS)
@Inherited
@Documented
public @interface Port {
}
