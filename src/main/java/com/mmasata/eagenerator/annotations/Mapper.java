package com.mmasata.eagenerator.annotations;

import org.springframework.stereotype.Component;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates the mapping profile and saves it in the application context. The annotation can only be placed above the class. <p>
 *
 * It must contain the "name" parameter, which defines the name of the mapper profile.
 * The name is then used to identify which mapper to run.
 * <p>
 * <strong>Along with the annotation, the mapper class must implement the MapperHandler interface.</strong> <p>
 *
 * The empty mapper profile to implement the logic looks like this:
 * <pre>
 *    &#064;Mapper(name="mapper-a")
 *    public class ExampleMapper implements MapperHandler {
 *
 *          &#064;Override
 *          public List&lt;ApiResources&gt; mapApiResources() {
 *              // logic here
 *          }
 *
 *          &#064;Override
 *          public List&lt;DTOProperty&gt; mapDtoObjects() {
 *              // logic here
 *          }
 *    }
 * </pre>
 *
 * <strong>Only one selected mapping profile is launched at a time!<strong>
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface Mapper {

    String name() default "";

}
