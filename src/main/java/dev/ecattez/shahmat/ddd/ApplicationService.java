package dev.ecattez.shahmat.ddd;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * It has the role of receiving a set of entities and performing some business logic on them.
 *
 * A Domain Service belongs to the Domain Layer, and therefore it knows nothing about the classes in the Application Layer,
 * like the Application Services or the Repositories.
 *
 * In the other hand, it can use other Domain Services and, of course, the Domain Model objects.
 *
 * @see <a href="https://herbertograca.com/2017/11/16/explicit-architecture-01-ddd-hexagonal-onion-clean-cqrs-how-i-put-it-all-together/#domain-services">DomainService</a>
 */
@Retention(RetentionPolicy.CLASS)
@Inherited
@Documented
public @interface DomainService {

}
