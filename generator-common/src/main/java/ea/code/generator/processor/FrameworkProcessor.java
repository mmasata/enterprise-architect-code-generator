package ea.code.generator.processor;

import ea.code.generator.GeneratorRunner;
import ea.code.generator.annotations.GenerateCode;
import ea.code.generator.context.GeneratorContext;
import ea.code.generator.validator.exception.GeneratorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

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
            throw new GeneratorException("No enabled generators!");
        }

        var codeGenBeans = springContext.getBeansWithAnnotation(GenerateCode.class);
        var relevantBeans = codeGenBeans.keySet().stream()
                .map(codeGenBeans::get)
                .filter(bean -> enabledCodeGenerators.contains(bean.getClass().getAnnotation(GenerateCode.class).name()))
                .toList();

        if (CollectionUtils.isEmpty(relevantBeans)) {
            log.error("No relevant generators was found!");
            throw new GeneratorException("No enabled generators!");
        }

        relevantBeans.forEach(relevantBean -> {
            if (relevantBean instanceof GeneratorRunner) {

                log.info("Running generator {}", relevantBean.getClass().getName());
                var startTime = System.currentTimeMillis();
                ((GeneratorRunner) relevantBean).run();

                var endTime = System.currentTimeMillis();
                var timeElapsed = endTime - startTime;
                log.info("[{}] Took {}ms.", relevantBean.getClass().getName(), timeElapsed);
            } else {
                throw new GeneratorException("Runner isn't implementations of GeneratorRunner class");
            }
        });
    }

}
