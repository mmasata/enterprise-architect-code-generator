package com.mmasata.eagenerator.processor;

import com.mmasata.eagenerator.MapperHandler;
import com.mmasata.eagenerator.annotations.Mapper;
import com.mmasata.eagenerator.context.GeneratorContext;
import com.mmasata.eagenerator.exception.GeneratorException;
import com.mmasata.eagenerator.validator.CommonApiValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;


@Slf4j
@RequiredArgsConstructor
public class MapperProcessor {

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

        log.trace("GeneratorContext has {} api resources", generatorContext.getApiResources().size());
        log.trace("GeneratorContext has {} DTO Objects", generatorContext.getDtoObjects().size());

        commonApiValidator.validate(generatorContext);
    }

    private void invokeCustomMapper(String profile) {

        var mapperBeans = springContext.getBeansWithAnnotation(Mapper.class);
        var mapperBean = mapperBeans.keySet().stream()
                .map(mapperBeans::get)
                .filter(bean -> bean.getClass().getAnnotation(Mapper.class) != null)
                .filter(bean -> bean.getClass().getAnnotation(Mapper.class).name().equalsIgnoreCase(profile))
                .findFirst()
                .orElse(null);

        if (mapperBean == null) {
            log.error("Custom mapper profile {} don't exist!", profile);
            throw new GeneratorException("Mapper doesn't exist!");
        }

        if (mapperBean instanceof MapperHandler) {

            log.info("running {} mapper", mapperBean.getClass().getName());
            var startTime = System.currentTimeMillis();
            var dtoObjects = ((MapperHandler) mapperBean).mapDtoObjects();
            var apiResources = ((MapperHandler) mapperBean).mapApiResources();

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
