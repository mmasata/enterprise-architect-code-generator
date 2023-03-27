package ea.code.generator.processor;

import ea.code.generator.annotations.CustomGeneratorMapper;
import ea.code.generator.annotations.RunMapper;
import ea.code.generator.context.GeneratorContext;
import ea.code.generator.validator.CommonApiValidator;
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
public class EaProcessor {

    private final GeneratorContext generatorContext;

    private final ApplicationContext springContext;

    private final CommonApiValidator commonApiValidator;

    public void run() {

        log.trace("Running EAProcessor.");

        var mappingConfig = generatorContext.getConfiguration().getMappingConfiguration();
        switch (mappingConfig.getType()) {
            case CUSTOM -> invokeCustomMapper(mappingConfig.getProfile());
            case GENERIC -> invokeGenericMapper();
        }

        commonApiValidator.validate(generatorContext);
    }

    private void invokeCustomMapper(String profile) {

        var mapperBeans = springContext.getBeansWithAnnotation(CustomGeneratorMapper.class);
        var mapperBean = mapperBeans.keySet().stream()
                .map(mapperBeans::get)
                .filter(bean -> bean.getClass().getAnnotation(CustomGeneratorMapper.class).name().equalsIgnoreCase(profile))
                .findFirst()
                .orElse(null);

        if (mapperBean == null) {
            log.error("Custom mapper profile {} don't exist!", profile);
            System.exit(0);
        }

        Arrays.stream(mapperBean.getClass().getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RunMapper.class))
                .forEach(method -> {

                    try {
                        log.info("Running {}.{}() mapper.", mapperBean.getClass().getName(), method.getName());
                        method.invoke(mapperBean);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        log.error("Cannot invoke {}.{}", mapperBean.getClass().getName(), method.getName());
                        throw new RuntimeException(e);
                    }
                });
    }

    private void invokeGenericMapper() {
        //TODO
    }

}
