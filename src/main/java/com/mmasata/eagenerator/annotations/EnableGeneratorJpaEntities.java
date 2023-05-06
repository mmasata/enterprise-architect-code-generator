package com.mmasata.eagenerator.annotations;

import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the user is to use the JPA entities supplied by the generator and to start scanning over them.
 * The scan will arrange for the JPA entities to get into the application context.
 * The annotation can only be placed above the class.
 * <p>
 * The annotation should be used above the application class, where it will look like this:
 * <pre>
 *    &#064;SpringBootApplication
 *    &#064;EnableGeneratorJpaEntities
 *    public class ExampleApplication {
 *
 *          public static void main(String[] args) {
 *              SpringApplication.run(ExampleApplication.class, args);
 *          }
 *    }
 * </pre>
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@EntityScan("com.mmasata.eagenerator.database.entity")
public @interface EnableGeneratorJpaEntities {
}
