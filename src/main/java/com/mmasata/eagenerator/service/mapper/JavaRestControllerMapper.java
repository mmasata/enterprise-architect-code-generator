package com.mmasata.eagenerator.service.mapper;

import com.mmasata.eagenerator.api.rest.ApiEndpoint;
import com.mmasata.eagenerator.api.rest.ApiResource;
import com.mmasata.eagenerator.api.rest.HttpMessage;
import com.mmasata.eagenerator.api.rest.enums.HttpMethod;
import com.mmasata.eagenerator.api.rest.enums.HttpStatus;
import com.mmasata.eagenerator.context.GeneratorContext;
import com.mmasata.eagenerator.context.model.enums.ControllerType;
import com.mmasata.eagenerator.service.constants.JavaConstants;
import com.mmasata.eagenerator.service.freemarker.model.JavaEndpoint;
import com.mmasata.eagenerator.service.helper.JavaHelper;
import com.mmasata.eagenerator.service.model.JavaFileDTO;
import com.mmasata.eagenerator.utils.FormatUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.BiFunction;

@RequiredArgsConstructor
public class JavaRestControllerMapper implements BiFunction<ControllerType, List<ApiResource>, List<JavaFileDTO>> {

    private final GeneratorContext generatorContext;

    private final JavaHelper javaHelper;

    @Override
    public List<JavaFileDTO> apply(ControllerType controllerType, List<ApiResource> apiResources) {
        return apiResources.stream()
                .map(apiResource -> mapToRestController(controllerType, apiResource))
                .toList();
    }

    private JavaFileDTO mapToRestController(ControllerType controllerType,
                                            ApiResource apiResource) {

        var imports = new TreeSet<String>();
        var isReactive = controllerType == ControllerType.REACTIVE;
        var endpoints = apiResource.getEndpoints().stream()
                .map(apiEndpoint -> mapToRestEndpoint(apiEndpoint, imports, isReactive))
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
                                           boolean isReactive) {

        var path = (StringUtils.isEmpty(apiEndpoint.getPath()))
                ? null
                : apiEndpoint.getPath();
        var response = getRelevantResponse(apiEndpoint.getResponses());
        var produces = Optional.of(apiEndpoint.getProduces())
                .orElse(generatorContext.getConfiguration().getJavaSpring().getDefaultEndpointMediaType().getValue());
        var consumes = Optional.of(apiEndpoint.getConsumes())
                .orElse(generatorContext.getConfiguration().getJavaSpring().getDefaultEndpointMediaType().getValue());

        var endpoint = new JavaEndpoint();
        endpoint.setHttpMethod(apiEndpoint.getHttpMethod().name());
        endpoint.setPath(path);
        endpoint.setMethodName(apiEndpoint.getName());
        endpoint.setProduces(produces);
        endpoint.setConsumes(handleConsumes(consumes, apiEndpoint.getHttpMethod()));
        endpoint.setReturnType(handleMethodReturnType(response, imports, isReactive));
        endpoint.setParams(mapEndpointParams(apiEndpoint, imports));

        return endpoint;
    }

    private String handleConsumes(String value,
                                  HttpMethod httpMethod) {

        return switch (httpMethod) {
            case GET, DELETE:
                yield null;
            case POST, PUT, PATCH, OPTIONS:
                yield value;
        };
    }

    private String handleMethodReturnType(HttpMessage response,
                                          Set<String> imports,
                                          boolean isReactive) {
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
        return JavaConstants.METHOD_RETURN_TYPE_WRAPPER.formatted(returnType);
    }

    private List<String> mapEndpointParams(ApiEndpoint apiEndpoint,
                                           Set<String> imports) {

        var params = new ArrayList<String>();

        if (apiEndpoint.getRequest() != null) {
            var objectName = apiEndpoint.getRequest().getProperty().getProperty().getName();
            params.add(JavaConstants.REQUEST_BODY_WRAPPER.formatted(objectName, FormatUtils.toCamelCase(objectName)));

            imports.add(JavaConstants.IMPORT_REQUEST_BODY);
            imports.add(javaHelper.getModelPackage() + "." + objectName);
        }

        //queryParams
        apiEndpoint.getQueryParams().forEach(queryParam -> {
            var dataType = JavaConstants.JAVA_DATA_TYPES_MAPPER.get(queryParam.getDataType());
            params.add(JavaConstants.REQUEST_PARAM_WRAPPER.formatted(dataType.getDataType(), FormatUtils.toCamelCase(queryParam.getName())));

            imports.add(JavaConstants.IMPORT_REQUEST_PARAM);
            if (dataType.getImportName() != null) {
                imports.add(dataType.getImportName());
            }
        });

        //pathParams
        apiEndpoint.getPathParams().forEach(pathParam -> {
            var dataType = JavaConstants.JAVA_DATA_TYPES_MAPPER.get(pathParam.getDataType());
            params.add(JavaConstants.PATH_PARAM_WRAPPER.formatted(dataType.getDataType(), FormatUtils.toCamelCase(pathParam.getName())));

            imports.add(JavaConstants.IMPORT_PATH_PARAM);
            if (dataType.getImportName() != null) {
                imports.add(dataType.getImportName());
            }
        });

        //headers
        apiEndpoint.getHttpHeaders().forEach(header -> {
            var dataType = JavaConstants.JAVA_DATA_TYPES_MAPPER.get(header.getDataType());
            params.add(JavaConstants.HEADER_PARAM_WRAPPER.formatted(header.getName(), dataType.getDataType(), FormatUtils.toCamelCase(header.getName())));
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
