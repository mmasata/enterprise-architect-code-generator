package com.mmasata.eagenerator.annotations;

import org.springframework.stereotype.Component;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates a new generator profile that the generator application context registers at startup.
 * The annotation can only be placed above the class. <p>
 *
 * It must contain the "name" parameter, which defines the name of the generator profile.
 * The name is then used to identify which generators to run.
 * The list of generators to run comes in the input configuration file. <p>
 *
 * <strong>Along with the annotation, the generator class must implement the GeneratorHandler interface.</strong> <p>
 *
 * The empty generator profile to implement the logic looks like this:
 * <pre>
 *    &#064;Generator(name="generator-a")
 *    public class ExampleGenerator implements GeneratorHandler {
 *
 *          &#064;Override
 *          public void run() {
 *              // logic here
 *          }
 *    }
 * </pre>
 *
 * <strong>Multiple generator profiles can be enabled at once, just add to the input field.<strong>
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface Generator {

    String name() default "";

}
