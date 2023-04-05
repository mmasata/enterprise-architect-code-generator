package ea.code.generator.service.helper;

import ea.code.generator.context.GeneratorContext;
import ea.code.generator.processor.FileProcessor;
import ea.code.generator.service.model.JavaFileDTO;
import ea.code.generator.utils.ConfigUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static ea.code.generator.service.constants.JavaConstants.JAVA_FILE_OUTPUT_PATTERN;
import static ea.code.generator.service.constants.JavaConstants.JAVA_MODEL_PACKAGE;
import static ea.code.generator.service.constants.JavaConstants.JAVA_PACKAGE_INFO_FREEMARKER_TEMPLATE_FILE;
import static ea.code.generator.service.constants.JavaConstants.JAVA_PACKAGE_PARAM;
import static ea.code.generator.service.constants.JavaConstants.JAVA_POM_FREEMARKER_TEMPLATE_FILE;
import static ea.code.generator.service.constants.JavaConstants.JAVA_PROJECT_MAIN_PATTERN;
import static ea.code.generator.service.constants.JavaConstants.JAVA_PROJECT_NAME;
import static ea.code.generator.service.constants.JavaConstants.JAVA_REST_CONTROLLER_PACKAGE;
import static ea.code.generator.service.constants.JavaConstants.JAVA_VERSION_PARAM;

@Slf4j
@Component
@RequiredArgsConstructor
public class JavaHelper {

    private final FileProcessor fileProcessor;

    private final GeneratorContext generatorContext;

    public String objectNameToVariableName(String objectName) {
        return Character.toLowerCase(objectName.charAt(0)) + (objectName.length() > 1
                ? objectName.substring(1)
                : "");
    }

    public String getModelPackage() {
        var params = generatorContext.getConfiguration().getParameters();

        if (params.containsKey(JAVA_PACKAGE_PARAM)) {
            return params.get(JAVA_PACKAGE_PARAM) + "." + JAVA_MODEL_PACKAGE;
        }

        return JAVA_MODEL_PACKAGE;
    }

    public String getModelFolder() {
        return getModelPackage().replaceAll("\\.", "/");
    }

    public String getRestPackage() {
        var params = generatorContext.getConfiguration().getParameters();

        if (params.containsKey(JAVA_PACKAGE_PARAM)) {
            return params.get(JAVA_PACKAGE_PARAM) + "." + JAVA_REST_CONTROLLER_PACKAGE;
        }

        return JAVA_REST_CONTROLLER_PACKAGE;
    }

    public String getRestFolder() {
        return getRestPackage().replaceAll("\\.", "/");
    }

    public void createJavaFile(String template,
                               JavaFileDTO javaFileDTO) {

        var variables = javaFileDTO.getFreemarkerVariables();
        var data = fileProcessor.processFreemarkerTemplate(template, variables);
        var projectBase = generatorContext.getConfiguration().getParameters().get(JAVA_PROJECT_NAME);

        var baseProjectPath = JAVA_PROJECT_MAIN_PATTERN.formatted(projectBase);
        var javaFilePath = JAVA_FILE_OUTPUT_PATTERN.formatted(baseProjectPath, javaFileDTO.getFolder(), javaFileDTO.getFileName());

        fileProcessor.generate(Map.of(javaFilePath, data));
    }

    public void createPom() {

        var config = generatorContext.getConfiguration();
        var params = config.getParameters();

        var javaProjectName = ConfigUtils.getConfigParameterOrNull(params, JAVA_PROJECT_NAME);
        var javaVersion = ConfigUtils.getConfigParameterOrNull(params, JAVA_VERSION_PARAM);
        var javaGroupId = ConfigUtils.getConfigParameterOrNull(params, "javaGroupId");
        var version = config.getVersion();

        if (ObjectUtils.anyNull(javaProjectName, javaGroupId, javaVersion, version)) {
            log.error("[java-generator] - javaProjectName, javaGroupId, javaVersion, version and javaPackage are required fields! Some of them is missing");
            return;
        }

        Map<String, Object> pomVariables = new HashMap<>();
        pomVariables.put("groupId", javaGroupId);
        pomVariables.put("artifactId", javaProjectName);
        pomVariables.put("version", version);
        pomVariables.put("name", javaProjectName);
        pomVariables.put("javaVersion", javaVersion);
        pomVariables.put("repoId", ConfigUtils.getConfigParameterOrNull(params, "javaDistributionManagementRepoId"));
        pomVariables.put("repoUrl", ConfigUtils.getConfigParameterOrNull(params, "javaDistributionManagementRepoUrl"));

        var data = fileProcessor.processFreemarkerTemplate(JAVA_POM_FREEMARKER_TEMPLATE_FILE, pomVariables);
        fileProcessor.generate(Map.of(javaProjectName + "/pom.xml", data));
    }

    public void createPackageInfo() {

        var projectBase = generatorContext.getConfiguration().getParameters().get("javaProjectName");
        var baseProjectPath = JAVA_PROJECT_MAIN_PATTERN.formatted(projectBase);

        var javaFilePath = JAVA_FILE_OUTPUT_PATTERN.formatted(baseProjectPath, getModelFolder(), "package-info");
        var data = fileProcessor.processFreemarkerTemplate(JAVA_PACKAGE_INFO_FREEMARKER_TEMPLATE_FILE,
                Map.of("modelPackage", getModelPackage()));

        fileProcessor.generate(Map.of(javaFilePath, data));
    }

}
