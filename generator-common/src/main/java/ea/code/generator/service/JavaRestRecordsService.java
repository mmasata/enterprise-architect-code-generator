package ea.code.generator.service;

import ea.code.generator.annotations.GenerateCode;
import ea.code.generator.annotations.RunGenerator;
import ea.code.generator.context.GeneratorContext;
import ea.code.generator.processor.FileProcessor;
import ea.code.generator.service.mapper.JavaRestRecordsMapper;
import ea.code.generator.service.model.JavaRestRecordDTO;
import lombok.RequiredArgsConstructor;

import java.util.Map;

import static ea.code.generator.service.constants.JavaRestRecordsConstants.JAVA_POM_FREEMARKER_TEMPLATE_FILE;
import static ea.code.generator.service.constants.JavaRestRecordsConstants.JAVA_RECORDS_FREEMARKER_TEMPLATE_FILE;
import static ea.code.generator.service.constants.JavaRestRecordsConstants.JAVA_REST_CONTROLLER_FREEMARKER_TEMPLATE_FILE;

@GenerateCode(name = "java-rest-records")
@RequiredArgsConstructor
public class JavaRestRecordsService {

    private final GeneratorContext generatorContext;

    private final JavaRestRecordsMapper javaRestRecordsMapper;

    private final FileProcessor fileProcessor;

    @RunGenerator
    public void run() {

        var projectBase = generatorContext.getConfiguration().getParameters().get("javaProjectName");
        var projectBaseJava = projectBase + "/src/main/java";

        var javaRestRecordVariableDTO = javaRestRecordsMapper.apply(generatorContext);
        javaRestRecordVariableDTO.getRestControllers().forEach(restController -> processJavaFile(projectBaseJava, JAVA_REST_CONTROLLER_FREEMARKER_TEMPLATE_FILE, restController));
        javaRestRecordVariableDTO.getRecords().forEach(javaRecord -> processJavaFile(projectBaseJava, JAVA_RECORDS_FREEMARKER_TEMPLATE_FILE, javaRecord));

        processPom();
    }

    private void processPom() {

        var config = generatorContext.getConfiguration();
        var params = config.getParameters();

        Map<String, Object> pomVariables = Map.of(
                "groupId", params.get("javaGroupId"),
                "artifactId", params.get("javaProjectName"),
                "version", config.getVersion(),
                "name", params.get("javaProjectName")
        );

        var data = fileProcessor.processFreemarkerTemplate(JAVA_POM_FREEMARKER_TEMPLATE_FILE, pomVariables);
        fileProcessor.generate(Map.of(params.get("javaProjectName") + "/pom.xml", data));
    }

    private void processJavaFile(String basePath,
                                 String template,
                                 JavaRestRecordDTO javaRestRecordDTO) {

        var variables = javaRestRecordDTO.getFreemarkerVariables();
        var data = fileProcessor.processFreemarkerTemplate(template, variables);

        var outputFile = basePath + "/" + javaRestRecordDTO.getFolder() + "/" + javaRestRecordDTO.getFileName() + ".java";
        fileProcessor.generate(Map.of(outputFile, data));
    }

}
