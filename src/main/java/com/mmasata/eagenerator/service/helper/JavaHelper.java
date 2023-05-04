package com.mmasata.eagenerator.service.helper;

import com.mmasata.eagenerator.context.GeneratorContext;
import com.mmasata.eagenerator.processor.FileProcessor;
import com.mmasata.eagenerator.service.constants.JavaConstants;
import com.mmasata.eagenerator.service.model.JavaFileDTO;
import com.mmasata.eagenerator.utils.ConfigUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;

import java.util.HashMap;
import java.util.Map;

@Slf4j
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

        if (params.containsKey(JavaConstants.JAVA_PACKAGE_PARAM)) {
            return params.get(JavaConstants.JAVA_PACKAGE_PARAM) + "." + JavaConstants.JAVA_MODEL_PACKAGE;
        }

        return JavaConstants.JAVA_MODEL_PACKAGE;
    }

    public String getModelFolder() {
        return getModelPackage().replaceAll("\\.", "/");
    }

    public String getRestPackage() {
        var params = generatorContext.getConfiguration().getParameters();

        if (params.containsKey(JavaConstants.JAVA_PACKAGE_PARAM)) {
            return params.get(JavaConstants.JAVA_PACKAGE_PARAM) + "." + JavaConstants.JAVA_REST_CONTROLLER_PACKAGE;
        }

        return JavaConstants.JAVA_REST_CONTROLLER_PACKAGE;
    }

    public String getRestFolder() {
        return getRestPackage().replaceAll("\\.", "/");
    }

    public void createJavaFile(String template,
                               JavaFileDTO javaFileDTO) {

        var variables = javaFileDTO.getFreemarkerVariables();
        var data = fileProcessor.processFreemarkerTemplate(template, variables);
        var projectBase = generatorContext.getConfiguration().getParameters().get(JavaConstants.JAVA_PROJECT_NAME);

        var baseProjectPath = JavaConstants.JAVA_PROJECT_MAIN_PATTERN.formatted(projectBase);
        var javaFilePath = JavaConstants.JAVA_FILE_OUTPUT_PATTERN.formatted(baseProjectPath, javaFileDTO.getFolder(), javaFileDTO.getFileName());

        fileProcessor.generate(Map.of(javaFilePath, data));
    }

    public void createPom() {

        var config = generatorContext.getConfiguration();
        var params = config.getParameters();

        var javaProjectName = ConfigUtils.getConfigParameterOrNull(params, JavaConstants.JAVA_PROJECT_NAME);
        var javaVersion = ConfigUtils.getConfigParameterOrNull(params, JavaConstants.JAVA_VERSION_PARAM);
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

        var data = fileProcessor.processFreemarkerTemplate(JavaConstants.JAVA_POM_FREEMARKER_TEMPLATE_FILE, pomVariables);
        fileProcessor.generate(Map.of(javaProjectName + "/pom.xml", data));
    }

    public void createPackageInfo() {

        var projectBase = generatorContext.getConfiguration().getParameters().get("javaProjectName");
        var baseProjectPath = JavaConstants.JAVA_PROJECT_MAIN_PATTERN.formatted(projectBase);

        var javaFilePath = JavaConstants.JAVA_FILE_OUTPUT_PATTERN.formatted(baseProjectPath, getModelFolder(), "package-info");
        var data = fileProcessor.processFreemarkerTemplate(JavaConstants.JAVA_PACKAGE_INFO_FREEMARKER_TEMPLATE_FILE,
                Map.of("modelPackage", getModelPackage()));

        fileProcessor.generate(Map.of(javaFilePath, data));
    }

}
