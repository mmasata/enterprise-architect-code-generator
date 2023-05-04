package com.mmasata.eagenerator.service.mapper;

import com.mmasata.eagenerator.api.rest.ApiEndpoint;
import com.mmasata.eagenerator.api.rest.ApiResource;
import com.mmasata.eagenerator.api.rest.HttpMessage;
import com.mmasata.eagenerator.api.rest.enums.HttpStatus;
import com.mmasata.eagenerator.context.GeneratorContext;
import com.mmasata.eagenerator.service.constants.JavaConstants;
import com.mmasata.eagenerator.service.freemarker.model.JavaEndpoint;
import com.mmasata.eagenerator.service.helper.JavaHelper;
import com.mmasata.eagenerator.service.model.JavaFileDTO;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;

@RequiredArgsConstructor
public class JavaRestControllerMapper implements Function<GeneratorContext, List<JavaFileDTO>> {

    private final JavaHelper javaHelper;

    @Override
    public List<JavaFileDTO> apply(GeneratorContext generatorContext) {

        var controllerType = (String) generatorContext.getConfiguration().getParameters().get("javaControllerType");
        return generatorContext.getApiResources().stream()
                .map(apiResource -> mapToRestController(controllerType, apiResource))
                .toList();
    }

    private JavaFileDTO mapToRestController(String controllerType,
                                            ApiResource apiResource) {

        var imports = new TreeSet<String>();
        var isReactive = JavaConstants.CONTROLLER_MODE_REACTIVE.equalsIgnoreCase(controllerType);
        var endpoints = apiResource.getEndpoints().stream()
                .map(apiEndpoint -> mapToRestEndpoint(apiEndpoint, imports, controllerType))
                .toList();

        var name = isReactive
                ? apiResource.getName() + "Reactive"
                : apiResource.getName() + "Standard";
        var dto = new JavaFileDTO();
        dto.setFileName(name);
        dto.setFolder(javaHelper.getRestFolder());

        dto
                .addVariable("package", javaHelper.getRestPackage())
                .addVariable("basePath", apiResource.getPath())
                .addVariable("controllerName", name)
                .addVariable("imports", imports)
                .addVariable("endpoints", endpoints);

        return dto;
    }

    private JavaEndpoint mapToRestEndpoint(ApiEndpoint apiEndpoint,
                                           Set<String> imports,
                                           String controllerMode) {

        var path = (StringUtils.isEmpty(apiEndpoint.getPath()))
                ? null
                : apiEndpoint.getPath();
        var isReactive = JavaConstants.CONTROLLER_MODE_REACTIVE.equalsIgnoreCase(controllerMode);
        var response = getRelevantResponse(apiEndpoint.getResponses());

        var endpoint = new JavaEndpoint();
        endpoint.setHttpMethod(apiEndpoint.getHttpMethod().name());
        endpoint.setPath(path);
        endpoint.setMethodName(apiEndpoint.getName());

        var returnType = "Void";
        if (response != null) {

            var isArray = response.getProperty().isArray();
            var type = response.getProperty().getProperty().getName();
            imports.add(javaHelper.getModelPackage() + "." + type);

            if (isReactive && isArray) {
                returnType = JavaConstants.METHOD_REACTIVE_OR_ARR_WRAPPER.formatted(JavaConstants.REACTIVE_ARRAY, type);
                imports.add(JavaConstants.IMPORT_FLUX);

            } else if (isReactive && !isArray) {
                returnType = JavaConstants.METHOD_REACTIVE_OR_ARR_WRAPPER.formatted(JavaConstants.REACTIVE_OBJECT, type);
                imports.add(JavaConstants.IMPORT_MONO);

            } else if (!isReactive && isArray) {
                returnType = JavaConstants.METHOD_REACTIVE_OR_ARR_WRAPPER.formatted(JavaConstants.NON_REACTIVE_ARRAY, type);
                imports.add(JavaConstants.IMPORT_LIST);

            } else {
                returnType = type;
            }
        }
        returnType = JavaConstants.METHOD_RETURN_TYPE_WRAPPER.formatted(returnType);

        endpoint.setReturnType(returnType);
        endpoint.setParams(mapEndpointParams(apiEndpoint, imports));

        return endpoint;
    }

    private List<String> mapEndpointParams(ApiEndpoint apiEndpoint,
                                           Set<String> imports) {

        var params = new ArrayList<String>();

        if (apiEndpoint.getRequest() != null) {
            var objectName = apiEndpoint.getRequest().getProperty().getProperty().getName();
            params.add(JavaConstants.REQUEST_BODY_WRAPPER.formatted(objectName, javaHelper.objectNameToVariableName(objectName)));

            imports.add(JavaConstants.IMPORT_REQUEST_BODY);
            imports.add(javaHelper.getModelPackage() + "." + objectName);
        }

        //TODO nejak zobecnit
        //queryParams
        apiEndpoint.getQueryParams().forEach(queryParam -> {
            var dataType = JavaConstants.JAVA_DATA_TYPES_MAPPER.get(queryParam.getDataType());
            params.add(JavaConstants.REQUEST_PARAM_WRAPPER.formatted(dataType.getDataType(), queryParam.getName()));

            imports.add(JavaConstants.IMPORT_REQUEST_PARAM);
            if (dataType.getImportName() != null) {
                imports.add(dataType.getImportName());
            }
        });

        //pathParams
        apiEndpoint.getPathParams().forEach(pathParam -> {
            var dataType = JavaConstants.JAVA_DATA_TYPES_MAPPER.get(pathParam.getDataType());
            params.add(JavaConstants.PATH_PARAM_WRAPPER.formatted(dataType.getDataType(), pathParam.getName()));

            imports.add(JavaConstants.IMPORT_PATH_PARAM);
            if (dataType.getImportName() != null) {
                imports.add(dataType.getImportName());
            }
        });

        //headers
        apiEndpoint.getHttpHeaders().forEach(header -> {
            var dataType = JavaConstants.JAVA_DATA_TYPES_MAPPER.get(header.getDataType());
            params.add(JavaConstants.HEADER_PARAM_WRAPPER.formatted(header.getName(), dataType.getDataType(), header.getName()));
            imports.add(JavaConstants.IMPORT_REQUEST_HEADER);

            if (dataType.getImportName() != null) {
                imports.add(dataType.getImportName());
            }
        });

        return params;
    }

    @Nullable
    private HttpMessage getRelevantResponse(Map<HttpStatus, HttpMessage> responses) {

        for (var response : responses.entrySet()) {
            if (!JavaConstants.ERROR_STATUSES.contains(response.getKey())) {
                return response.getValue();
            }
        }

        return null;
    }

}