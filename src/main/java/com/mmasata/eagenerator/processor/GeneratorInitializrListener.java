package com.mmasata.eagenerator.processor;

import com.mmasata.eagenerator.GeneratorHandler;
import com.mmasata.eagenerator.annotations.Generator;
import com.mmasata.eagenerator.context.GeneratorContext;
import com.mmasata.eagenerator.exception.GeneratorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.util.CollectionUtils;

/**
 * A listener that, after initializing the Bean and SpringContext, traverses the application context to find the generator components and execute the logic.
 */
@Slf4j
public class GeneratorInitializrListener implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private GeneratorContext generatorContext;

    @Autowired
    private MapperProcessor mapperProcessor;

    /**
     * The method executes the framework generator logic.
     * @param event Event published as late as conceivably possible to indicate that the application is ready to service requests.
     */
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.trace("GeneratorProcess has been invoked");

        var enabledCodeGenerators = generatorContext.getConfiguration().getEnabledGenerators();
        mapperProcessor.run();

        if (enabledCodeGenerators == null) {
            log.error("Parameter enabledGenerators is missing in config file!");
            throw new GeneratorException("No enabled generators!");
        }

        var codeGenBeans = event.getApplicationContext().getBeansWithAnnotation(Generator.class);
        var relevantBeans = codeGenBeans.keySet().stream()
                .map(codeGenBeans::get)
                .filter(bean -> enabledCodeGenerators.contains(bean.getClass().getAnnotation(Generator.class).name()))
                .toList();

        if (CollectionUtils.isEmpty(relevantBeans)) {
            log.error("No relevant generators was found!");
            throw new GeneratorException("No enabled generators!");
        }

        relevantBeans.forEach(relevantBean -> {
            if (relevantBean instanceof GeneratorHandler) {

                log.info("Running generator {}", relevantBean.getClass().getName());
                var startTime = System.currentTimeMillis();
                ((GeneratorHandler) relevantBean).run();

                var endTime = System.currentTimeMillis();
                var timeElapsed = endTime - startTime;
                log.info("[{}] Took {}ms.", relevantBean.getClass().getName(), timeElapsed);
            } else {
                throw new GeneratorException("Runner isn't implementations of GeneratorRunner class");
            }
        });
    }

}
