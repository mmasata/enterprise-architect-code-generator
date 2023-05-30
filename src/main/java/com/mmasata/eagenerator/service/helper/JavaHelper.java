package com.mmasata.eagenerator.service.helper;

import com.mmasata.eagenerator.context.GeneratorContext;
import com.mmasata.eagenerator.context.model.Pom;
import com.mmasata.eagenerator.processor.FileProcessor;
import com.mmasata.eagenerator.service.constants.JavaConstants;
import com.mmasata.eagenerator.service.freemarker.model.PomRepository;
import com.mmasata.eagenerator.service.model.JavaFileDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

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
        var packageName = generatorContext.getConfiguration().getJavaSpring().getPackageName();

        if (StringUtils.isNotEmpty(packageName)) {
            return packageName + "." + JavaConstants.JAVA_MODEL_PACKAGE;
        }

        return JavaConstants.JAVA_MODEL_PACKAGE;
    }

    public String getModelFolder() {
        return getModelPackage().replaceAll("\\.", "/");
    }

    public String getRestPackage() {
        var packageName = generatorContext.getConfiguration().getJavaSpring().getPackageName();

        if (StringUtils.isNotEmpty(packageName)) {
            return packageName + "." + JavaConstants.JAVA_REST_CONTROLLER_PACKAGE;
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
        var projectBase = generatorContext.getConfiguration().getJavaSpring().getPom().getName();

        var baseProjectPath = JavaConstants.JAVA_PROJECT_MAIN_PATTERN.formatted(projectBase);
        var javaFilePath = JavaConstants.JAVA_FILE_OUTPUT_PATTERN.formatted(baseProjectPath, javaFileDTO.getFolder(), javaFileDTO.getFileName());

        fileProcessor.generate(Map.of(javaFilePath, data));
    }

    public void createPom(Pom pom, String appVersion) {
        Map<String, Object> pomVariables = new HashMap<>();
        pomVariables.put("groupId", pom.getGroupId());
        pomVariables.put("artifactId", pom.getArtifactId());
        pomVariables.put("version", appVersion);
        pomVariables.put("name", pom.getName());
        pomVariables.put("javaVersion", pom.getJavaVersion());

        pomVariables.put("distributionManagement", pom.getDistributionManagement().stream().map(in -> new PomRepository(in.getType(),
                in.getName(),
                in.getId(),
                in.getUrl())).toList());

        pomVariables.put("repositories", pom.getRepositories().stream().map(in -> new PomRepository(in.getType(),
                in.getName(),
                in.getId(),
                in.getUrl())).toList());

        pomVariables.put("pluginRepositories", pom.getPluginRepositories().stream().map(in -> new PomRepository(in.getType(),
                in.getName(),
                in.getId(),
                in.getUrl())).toList());

        var data = fileProcessor.processFreemarkerTemplate(JavaConstants.JAVA_POM_FREEMARKER_TEMPLATE_FILE, pomVariables);
        fileProcessor.generate(Map.of(pom.getName() + "/pom.xml", data));
    }

    public void createPackageInfo(String projectName) {

        var baseProjectPath = JavaConstants.JAVA_PROJECT_MAIN_PATTERN.formatted(projectName);
        var javaFilePath = JavaConstants.JAVA_FILE_OUTPUT_PATTERN.formatted(baseProjectPath, getModelFolder(), "package-info");
        var data = fileProcessor.processFreemarkerTemplate(JavaConstants.JAVA_PACKAGE_INFO_FREEMARKER_TEMPLATE_FILE,
                Map.of("modelPackage", getModelPackage()));

        fileProcessor.generate(Map.of(javaFilePath, data));
    }

}
