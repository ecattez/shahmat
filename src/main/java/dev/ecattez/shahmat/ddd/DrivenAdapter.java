package dev.ecattez.shahmat.ddd;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * The Primary or Driver Adapters wrap around a Port and use it to tell the Application Core what to do.
 * They translate whatever comes from a delivery mechanism into a method call in the Application Core.
 *
 * In other words, our Driving Adapters are Controllers or Console Commands who are injected in their constructor
 * with some object whose class implements the interface (Port) that the controller or console command requires.
 *
 * @see <a href="https://herbertograca.com/2017/11/16/explicit-architecture-01-ddd-hexagonal-onion-clean-cqrs-how-i-put-it-all-together/#ports">Driving Adapter</a>
 */
@Retention(RetentionPolicy.CLASS)
@Inherited
@Documented
public @interface DrivingAdapter {
}
