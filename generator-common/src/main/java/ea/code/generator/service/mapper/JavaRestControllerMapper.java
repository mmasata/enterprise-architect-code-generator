package ea.code.generator.service.mapper;

import ea.code.generator.api.rest.ApiEndpoint;
import ea.code.generator.api.rest.ApiResource;
import ea.code.generator.api.rest.HttpMessage;
import ea.code.generator.api.rest.enums.HttpStatus;
import ea.code.generator.context.GeneratorContext;
import ea.code.generator.service.freemarker.model.JavaEndpoint;
import ea.code.generator.service.helper.JavaHelper;
import ea.code.generator.service.model.JavaFileDTO;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;

import static ea.code.generator.service.constants.JavaConstants.CONTROLLER_MODE_REACTIVE;
import static ea.code.generator.service.constants.JavaConstants.ERROR_STATUSES;
import static ea.code.generator.service.constants.JavaConstants.HEADER_PARAM_WRAPPER;
import static ea.code.generator.service.constants.JavaConstants.IMPORT_FLUX;
import static ea.code.generator.service.constants.JavaConstants.IMPORT_LIST;
import static ea.code.generator.service.constants.JavaConstants.IMPORT_MONO;
import static ea.code.generator.service.constants.JavaConstants.IMPORT_PATH_PARAM;
import static ea.code.generator.service.constants.JavaConstants.IMPORT_REQUEST_BODY;
import static ea.code.generator.service.constants.JavaConstants.IMPORT_REQUEST_HEADER;
import static ea.code.generator.service.constants.JavaConstants.IMPORT_REQUEST_PARAM;
import static ea.code.generator.service.constants.JavaConstants.JAVA_DATA_TYPES_MAPPER;
import static ea.code.generator.service.constants.JavaConstants.METHOD_REACTIVE_OR_ARR_WRAPPER;
import static ea.code.generator.service.constants.JavaConstants.METHOD_RETURN_TYPE_WRAPPER;
import static ea.code.generator.service.constants.JavaConstants.NON_REACTIVE_ARRAY;
import static ea.code.generator.service.constants.JavaConstants.PATH_PARAM_WRAPPER;
import static ea.code.generator.service.constants.JavaConstants.REACTIVE_ARRAY;
import static ea.code.generator.service.constants.JavaConstants.REACTIVE_OBJECT;
import static ea.code.generator.service.constants.JavaConstants.REQUEST_BODY_WRAPPER;
import static ea.code.generator.service.constants.JavaConstants.REQUEST_PARAM_WRAPPER;

@Component
@RequiredArgsConstructor
public class JavaRestControllerMapper implements Function<GeneratorContext, List<JavaFileDTO>> {

    private final JavaHelper javaHelper;

    @Override
    public List<JavaFileDTO> apply(GeneratorContext generatorContext) {

        var controllers = new ArrayList<JavaFileDTO>();
        var controllerTypes = (List<String>) generatorContext.getConfiguration().getParameters().get("javaControllerTypes");

        controllerTypes.forEach(controllerMode -> {
            var typeOfControllers = generatorContext.getApiResources().stream()
                    .map(apiResource -> mapToRestController(controllerMode, apiResource))
                    .toList();

            controllers.addAll(typeOfControllers);
        });

        return controllers;
    }

    private JavaFileDTO mapToRestController(String controllerMode,
                                            ApiResource apiResource) {

        var imports = new TreeSet<String>();
        var isReactive = CONTROLLER_MODE_REACTIVE.equalsIgnoreCase(controllerMode);
        var endpoints = apiResource.getEndpoints().stream()
                .map(apiEndpoint -> mapToRestEndpoint(apiEndpoint, imports, controllerMode))
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
        var isReactive = CONTROLLER_MODE_REACTIVE.equalsIgnoreCase(controllerMode);
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
        endpoint.setParams(mapEndpointParams(apiEndpoint, imports));

        return endpoint;
    }

    private List<String> mapEndpointParams(ApiEndpoint apiEndpoint,
                                           Set<String> imports) {

        var params = new ArrayList<String>();

        if (apiEndpoint.getRequest() != null) {
            var objectName = apiEndpoint.getRequest().getProperty().getProperty().getName();
            params.add(REQUEST_BODY_WRAPPER.formatted(objectName, javaHelper.objectNameToVariableName(objectName)));

            imports.add(IMPORT_REQUEST_BODY);
            imports.add(javaHelper.getModelPackage() + "." + objectName);
        }

        //queryParams
        apiEndpoint.getQueryParams().forEach(queryParam -> {
            var dataType = JAVA_DATA_TYPES_MAPPER.get(queryParam.getDataType());
            params.add(REQUEST_PARAM_WRAPPER.formatted(dataType.getDataType(), queryParam.getName()));

            imports.add(IMPORT_REQUEST_PARAM);
            if (dataType.getImportName() != null) {
                imports.add(dataType.getImportName());
            }
        });

        //pathParams
        apiEndpoint.getPathParams().forEach(pathParam -> {
            var dataType = JAVA_DATA_TYPES_MAPPER.get(pathParam.getDataType());
            params.add(PATH_PARAM_WRAPPER.formatted(dataType.getDataType(), pathParam.getName()));

            imports.add(IMPORT_PATH_PARAM);
            if (dataType.getImportName() != null) {
                imports.add(dataType.getImportName());
            }
        });

        //headers
        apiEndpoint.getHttpHeaders().forEach(header -> {
            var dataType = JAVA_DATA_TYPES_MAPPER.get(header.getDataType());
            params.add(HEADER_PARAM_WRAPPER.formatted(header.getName(), dataType.getDataType(), header.getName()));
            imports.add(IMPORT_REQUEST_HEADER);

            if (dataType.getImportName() != null) {
                imports.add(dataType.getImportName());
            }
        });

        return params;
    }

    @Nullable
    private HttpMessage getRelevantResponse(Map<HttpStatus, HttpMessage> responses) {

        for (var response : responses.entrySet()) {
            if (!ERROR_STATUSES.contains(response.getKey())) {
                return response.getValue();
            }
        }

        return null;
    }

}
