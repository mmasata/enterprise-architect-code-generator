package ea.code.generator.processor;

import ea.code.generator.annotations.GenerateCode;
import ea.code.generator.annotations.RunGenerator;
import ea.code.generator.context.GeneratorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

@Slf4j
@Component
@RequiredArgsConstructor
@Scope("singleton")
public class FrameworkProcessor {

    private final GeneratorContext generatorContext;

    private final ApplicationContext springContext;

    private final EaProcessor eaProcessor;

    public void run() {

        var enabledCodeGenerators = generatorContext.getConfiguration().getEnabledGenerators();
        eaProcessor.run();

        if (enabledCodeGenerators == null) {
            log.error("Parameter enabledGenerators is missing in config file!");
            System.exit(0);
        }

        var codeGenBeans = springContext.getBeansWithAnnotation(GenerateCode.class);
        var relevantBeans = codeGenBeans.keySet().stream()
                .map(codeGenBeans::get)
                .filter(bean -> enabledCodeGenerators.contains(bean.getClass().getAnnotation(GenerateCode.class).name()))
                .toList();

        relevantBeans.forEach(bean -> Arrays.stream(bean.getClass().getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RunGenerator.class))
                .forEach(method -> {
                    try {
                        log.info("Running {}.{}() code generator.", bean.getClass().getName(), method.getName());
                        method.invoke(bean);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                }));
    }

}
