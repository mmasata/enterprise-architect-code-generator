package ea.code.generator.service;

import ea.code.generator.GeneratorRunner;
import ea.code.generator.annotations.GenerateCode;
import ea.code.generator.context.GeneratorContext;
import ea.code.generator.service.helper.JavaHelper;
import ea.code.generator.service.mapper.JavaDTOMapper;
import ea.code.generator.service.mapper.JavaRestControllerMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static ea.code.generator.service.constants.JavaConstants.*;

@Slf4j
@GenerateCode(name = "java-spring")
@RequiredArgsConstructor
public class JavaGeneratorService implements GeneratorRunner {

    private final GeneratorContext generatorContext;

    private final JavaRestControllerMapper javaRestControllerMapper;

    private final JavaDTOMapper javaDTOMapper;

    private final JavaHelper javaHelper;

    @Override
    public void run() {

        var params = generatorContext.getConfiguration().getParameters();
        var DTOFreemarkerTemplate = "RECORDS".equalsIgnoreCase((String) params.get("javaDtoType"))
                ? JAVA_RECORDS_FREEMARKER_TEMPLATE_FILE
                : JAVA_LOMBOK_DTO_FREEMARKER_TEMPLATE_FILE;

        if ("RECORDS".equalsIgnoreCase((String) params.get("javaDtoType")) && Integer.valueOf((String) params.get("javaVersion")) < 14) {
            log.warn("[java-spring] - Records are available from Java 14. Current Java version not support Record yes. Generator will create Lombok models.");
            DTOFreemarkerTemplate = JAVA_LOMBOK_DTO_FREEMARKER_TEMPLATE_FILE;
        }

        var finalDTOFreemarkerTemplate = DTOFreemarkerTemplate;
        javaRestControllerMapper.apply(generatorContext)
                .forEach(restController -> javaHelper.createJavaFile(JAVA_REST_CONTROLLER_FREEMARKER_TEMPLATE_FILE, restController));
        javaDTOMapper.apply(generatorContext)
                .forEach(javaDTO -> {

                    if (javaDTO.getFreemarkerVariables().containsKey("isEnum")
                            && javaDTO.getFreemarkerVariables().get("isEnum").equals(Boolean.TRUE)) {
                        javaHelper.createJavaFile(JAVA_ENUM_FREEMARKER_TEMPLATE_FILE, javaDTO);
                    } else {
                        javaHelper.createJavaFile(finalDTOFreemarkerTemplate, javaDTO);
                    }
                });

        javaHelper.createPom();
        javaHelper.createPackageInfo();
    }

}
