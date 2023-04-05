package ea.code.generator.service;

import ea.code.generator.annotations.GenerateCode;
import ea.code.generator.annotations.RunGenerator;
import ea.code.generator.context.GeneratorContext;
import ea.code.generator.service.helper.JavaHelper;
import ea.code.generator.service.mapper.JavaDTOMapper;
import ea.code.generator.service.mapper.JavaRestControllerMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static ea.code.generator.service.constants.JavaConstants.JAVA_ENUM_FREEMARKER_TEMPLATE_FILE;
import static ea.code.generator.service.constants.JavaConstants.JAVA_LOMBOK_DTO_FREEMARKER_TEMPLATE_FILE;
import static ea.code.generator.service.constants.JavaConstants.JAVA_RECORDS_FREEMARKER_TEMPLATE_FILE;
import static ea.code.generator.service.constants.JavaConstants.JAVA_REST_CONTROLLER_FREEMARKER_TEMPLATE_FILE;

@Slf4j
@GenerateCode(name = "java-spring")
@RequiredArgsConstructor
public class JavaGeneratorService {

    private final GeneratorContext generatorContext;

    private final JavaRestControllerMapper javaRestControllerMapper;

    private final JavaDTOMapper javaDTOMapper;

    private final JavaHelper javaHelper;

    @RunGenerator
    public void run() {

        var params = generatorContext.getConfiguration().getParameters();
        var DTOFreemarkerTemplate = "RECORDS".equalsIgnoreCase((String) params.get("javaDtoType"))
                ? JAVA_RECORDS_FREEMARKER_TEMPLATE_FILE
                : JAVA_LOMBOK_DTO_FREEMARKER_TEMPLATE_FILE;

        javaRestControllerMapper.apply(generatorContext)
                .forEach(restController -> javaHelper.createJavaFile(JAVA_REST_CONTROLLER_FREEMARKER_TEMPLATE_FILE, restController));

        javaDTOMapper.apply(generatorContext)
                .forEach(javaDTO -> {

                    if (javaDTO.getFreemarkerVariables().containsKey("isEnum")
                            && javaDTO.getFreemarkerVariables().get("isEnum").equals(Boolean.TRUE)) {
                        javaHelper.createJavaFile(JAVA_ENUM_FREEMARKER_TEMPLATE_FILE, javaDTO);
                    } else {
                        javaHelper.createJavaFile(DTOFreemarkerTemplate, javaDTO);
                    }
                });

        javaHelper.createPom();
        javaHelper.createPackageInfo();
    }

}