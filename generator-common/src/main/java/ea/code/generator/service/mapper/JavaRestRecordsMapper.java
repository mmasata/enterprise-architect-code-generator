package ea.code.generator.service.mapper;

import ea.code.generator.api.rest.ApiEndpoint;
import ea.code.generator.api.rest.ApiResource;
import ea.code.generator.api.rest.HttpMessage;
import ea.code.generator.api.rest.enums.HttpStatus;
import ea.code.generator.context.GeneratorContext;
import ea.code.generator.context.model.GeneratorConfiguration;
import ea.code.generator.service.freemarker.model.JavaRestRecordEndpoint;
import ea.code.generator.service.model.JavaRestRecordDTO;
import ea.code.generator.service.model.JavaRestRecordVariableDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;

import static ea.code.generator.service.constants.JavaRestRecordsConstants.*;

@Component
public class JavaRestRecordsMapper implements Function<GeneratorContext, JavaRestRecordVariableDTO> {

    @Override
    public JavaRestRecordVariableDTO apply(GeneratorContext generatorContext) {

        return new JavaRestRecordVariableDTO(mapToRestControllers(generatorContext),
                mapToJavaRecords(generatorContext));
    }

    private List<JavaRestRecordDTO> mapToRestControllers(GeneratorContext generatorContext) {

        return generatorContext.getApiResources().stream()
                .map(apiResource -> mapToRestController(generatorContext, apiResource))
                .toList();
    }

    private List<JavaRestRecordDTO> mapToJavaRecords(GeneratorContext generatorContext) {

        //TODO create DTO classes
        return Collections.emptyList();
    }

    private JavaRestRecordDTO mapToRestController(GeneratorContext generatorContext,
                                                  ApiResource apiResource) {

        var config = generatorContext.getConfiguration();
        var javaPackage = (String) config.getParameters().get("javaPackage");

        javaPackage = (StringUtils.isEmpty(javaPackage))
                ? JAVA_REST_CONTROLLER_PACKAGE
                : javaPackage + "." + JAVA_REST_CONTROLLER_PACKAGE;

        var imports = new TreeSet<String>();
        var endpoints = apiResource.getEndpoints().stream()
                .map(apiEndpoint -> mapToRestEndpoint(apiEndpoint, config, imports))
                .toList();

        var dto = new JavaRestRecordDTO();
        dto.setFileName(apiResource.getName());
        dto.setFolder(javaPackage.replaceAll("\\.", "/"));

        dto
                .addVariable("package", javaPackage)
                .addVariable("basePath", apiResource.getPath())
                .addVariable("controllerName", apiResource.getName())
                .addVariable("imports", imports)
                .addVariable("endpoints", endpoints);

        return dto;
    }

    private JavaRestRecordEndpoint mapToRestEndpoint(ApiEndpoint apiEndpoint,
                                                     GeneratorConfiguration config,
                                                     Set<String> imports) {

        var javaModelPackage = (String) config.getParameters().get("javaPackage");
        javaModelPackage = (StringUtils.isEmpty(javaModelPackage))
                ? JAVA_MODEL_PACKAGE
                : javaModelPackage + "." + JAVA_MODEL_PACKAGE;

        var path = (StringUtils.isEmpty(apiEndpoint.getPath()))
                ? null
                : apiEndpoint.getPath();
        var isReactive = (Boolean) config.getParameters().get("reactiveJava");
        var response = getRelevantResponse(apiEndpoint.getResponses());

        var endpoint = new JavaRestRecordEndpoint();
        endpoint.setHttpMethod(apiEndpoint.getHttpMethod().name());
        endpoint.setPath(path);
        endpoint.setMethodName(apiEndpoint.getName());

        var returnType = "Void";
        if (response != null) {

            var isArray = response.getProperty().isArray();
            var type = response.getProperty().getProperty().getName();
            imports.add(javaModelPackage + "." + type);

            if (isReactive && isArray) {
                returnType = METHOD_REACTIVE_OR_ARR_WRAPPER.formatted(REACTIVE_ARRAY, type);
                imports.add(IMPORT_FLUX);

            } else if (isReactive && !isArray) {
                returnType = METHOD_REACTIVE_OR_ARR_WRAPPER.formatted(REACTIVE_OBJECT, type);
                imports.add(IMPORT_MONO);

            } else if (!isReactive && isArray) {
                returnType = METHOD_REACTIVE_OR_ARR_WRAPPER.formatted(NON_REACTIVE_ARRAY, type);
                imports.add(IMPORT_LIST);

            } else {
                returnType = type;
            }
        }
        returnType = METHOD_RETURN_TYPE_WRAPPER.formatted(returnType);

        endpoint.setReturnType(returnType);
        endpoint.setParams(mapEndpointParams(apiEndpoint));

        return endpoint;
    }

    private List<String> mapEndpointParams(ApiEndpoint apiEndpoint) {

        var params = new ArrayList<String>();

        if (apiEndpoint.getRequest() != null) {
            var objectName = apiEndpoint.getRequest().getProperty().getProperty().getName();
            params.add(REQUEST_BODY_WRAPPER.formatted(objectName, objectNameToVariableName(objectName)));
        }

        //queryParams
        apiEndpoint.getQueryParams().forEach(queryParam -> {
            params.add(REQUEST_PARAM_WRAPPER.formatted("void", queryParam.getName())); //TODO convertor to Java data types
        });

        //pathParams
        apiEndpoint.getPathParams().forEach(pathParam -> {
            params.add(PATH_PARAM_WRAPPER.formatted("void", pathParam.getName())); //TODO convertor to Java data types
        });

        //headers
        apiEndpoint.getHttpHeaders().forEach(header -> {
            params.add(HEADER_PARAM_WRAPPER.formatted(header.getName(), "void", header.getName())); //TODO convertor to Java data types
        });

        return params;
    }

    @Nullable
    private HttpMessage getRelevantResponse(Map<HttpStatus, HttpMessage> responses) {

        for (var response : responses.entrySet()) {

            //find first which is not error
            if (response.getKey().getCode() >= 100
                    && response.getKey().getCode() < 400) {

                return response.getValue();
            }
        }

        return null;
    }

    private String objectNameToVariableName(String objectName) {

        return Character.toLowerCase(objectName.charAt(0)) + (objectName.length() > 1
                ? objectName.substring(1)
                : "");
    }

}
