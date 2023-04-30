package ea.code.generator.processor;

import ea.code.generator.GeneratorMapper;
import ea.code.generator.annotations.CustomGeneratorMapper;
import ea.code.generator.context.GeneratorContext;
import ea.code.generator.validator.CommonApiValidator;
import ea.code.generator.validator.exception.GeneratorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


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
                .filter(bean -> bean.getClass().getAnnotation(CustomGeneratorMapper.class) != null)
                .filter(bean -> bean.getClass().getAnnotation(CustomGeneratorMapper.class).name().equalsIgnoreCase(profile))
                .findFirst()
                .orElse(null);

        if (mapperBean == null) {
            log.error("Custom mapper profile {} don't exist!", profile);
            throw new GeneratorException("Mapper doesn't exist!");
        }

        if (mapperBean instanceof GeneratorMapper) {

            log.info("running {} mapper", mapperBean.getClass().getName());
            var startTime = System.currentTimeMillis();
            var dtoObjects = ((GeneratorMapper) mapperBean).mapDtoObjects();
            var apiResources = ((GeneratorMapper) mapperBean).mapApiResources();

            if (dtoObjects != null) {
                generatorContext.setDtoObjects(dtoObjects);
            }

            if (apiResources != null) {
                generatorContext.setApiResources(apiResources);
            }

            var endTime = System.currentTimeMillis();
            var timeElapsed = endTime - startTime;
            log.info("[{}] Took {}ms.", mapperBean.getClass().getName(), timeElapsed);
        } else {
            throw new GeneratorException("Mapper isn't implementations of GeneratorMapper class");
        }
    }

    private void invokeGenericMapper() {
        //NOT IMPLEMENTED - will be extended in the future
        throw new GeneratorException("Generic mapper is not implemented yet!");
    }

}
