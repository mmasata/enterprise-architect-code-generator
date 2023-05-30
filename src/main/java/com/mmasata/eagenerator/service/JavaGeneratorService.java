package com.mmasata.eagenerator.service;

import com.mmasata.eagenerator.GeneratorHandler;
import com.mmasata.eagenerator.annotations.Generator;
import com.mmasata.eagenerator.context.GeneratorContext;
import com.mmasata.eagenerator.context.model.enums.DtoType;
import com.mmasata.eagenerator.service.constants.JavaConstants;
import com.mmasata.eagenerator.service.helper.JavaHelper;
import com.mmasata.eagenerator.service.mapper.JavaDTOMapper;
import com.mmasata.eagenerator.service.mapper.JavaRestControllerMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Generator(name = "java-spring")
@RequiredArgsConstructor
public class JavaGeneratorService implements GeneratorHandler {

    private final GeneratorContext generatorContext;

    private final JavaRestControllerMapper javaRestControllerMapper;

    private final JavaDTOMapper javaDTOMapper;

    private final JavaHelper javaHelper;

    @Override
    public void run() {
        var config = generatorContext.getConfiguration().getJavaSpring();
        var DTOFreemarkerTemplate = config.getDtoType() == DtoType.RECORDS
                ? JavaConstants.JAVA_RECORDS_FREEMARKER_TEMPLATE_FILE
                : JavaConstants.JAVA_LOMBOK_DTO_FREEMARKER_TEMPLATE_FILE;

        if (config.getDtoType() == DtoType.RECORDS && config.getPom().getJavaVersion() < 14) {
            log.warn("[java-spring] - Records are available from Java 14. Current Java version not support Record yes. Generator will create Lombok models.");
            DTOFreemarkerTemplate = JavaConstants.JAVA_LOMBOK_DTO_FREEMARKER_TEMPLATE_FILE;
        }

        var finalDTOFreemarkerTemplate = DTOFreemarkerTemplate;
        javaRestControllerMapper.apply(config.getControllerType(), generatorContext.getApiResources())
                .forEach(restController -> javaHelper.createJavaFile(JavaConstants.JAVA_REST_CONTROLLER_FREEMARKER_TEMPLATE_FILE, restController));

        javaDTOMapper.apply(generatorContext)
                .forEach(javaDTO -> {

                    if (javaDTO.getFreemarkerVariables().containsKey("isEnum")
                            && javaDTO.getFreemarkerVariables().get("isEnum").equals(Boolean.TRUE)) {
                        javaHelper.createJavaFile(JavaConstants.JAVA_ENUM_FREEMARKER_TEMPLATE_FILE, javaDTO);
                    } else {
                        javaHelper.createJavaFile(finalDTOFreemarkerTemplate, javaDTO);
                    }
                });

        javaHelper.createPom(config.getPom(), generatorContext.getConfiguration().getVersion());
        javaHelper.createPackageInfo(config.getPom().getName());
    }

}
