package ea.code.generator.service.mapper;

import ea.code.generator.api.rest.ApiEndpoint;
import ea.code.generator.api.rest.ApiResource;
import ea.code.generator.api.rest.DTOProperty;
import ea.code.generator.api.rest.HttpMessage;
import ea.code.generator.api.rest.enums.DataType;
import ea.code.generator.api.rest.enums.HttpStatus;
import ea.code.generator.context.GeneratorContext;
import ea.code.generator.context.model.GeneratorConfiguration;
import ea.code.generator.service.freemarker.model.JavaEndpoint;
import ea.code.generator.service.freemarker.model.JavaParam;
import ea.code.generator.service.model.JavaRestRecordDTO;
import ea.code.generator.service.model.JavaRestRecordVariableDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;

import static ea.code.generator.service.constants.JavaRestRecordsConstants.ERROR_STATUSES;
import static ea.code.generator.service.constants.JavaRestRecordsConstants.HEADER_PARAM_WRAPPER;
import static ea.code.generator.service.constants.JavaRestRecordsConstants.IMPORT_FLUX;
import static ea.code.generator.service.constants.JavaRestRecordsConstants.IMPORT_LIST;
import static ea.code.generator.service.constants.JavaRestRecordsConstants.IMPORT_MONO;
import static ea.code.generator.service.constants.JavaRestRecordsConstants.IMPORT_PATH_PARAM;
import static ea.code.generator.service.constants.JavaRestRecordsConstants.IMPORT_REQUEST_HEADER;
import static ea.code.generator.service.constants.JavaRestRecordsConstants.IMPORT_REQUEST_PARAM;
import static ea.code.generator.service.constants.JavaRestRecordsConstants.JAVA_DATA_TYPES_MAPPER;
import static ea.code.generator.service.constants.JavaRestRecordsConstants.JAVA_MODEL_PACKAGE;
import static ea.code.generator.service.constants.JavaRestRecordsConstants.JAVA_REST_CONTROLLER_PACKAGE;
import static ea.code.generator.service.constants.JavaRestRecordsConstants.METHOD_REACTIVE_OR_ARR_WRAPPER;
import static ea.code.generator.service.constants.JavaRestRecordsConstants.METHOD_RETURN_TYPE_WRAPPER;
import static ea.code.generator.service.constants.JavaRestRecordsConstants.NON_REACTIVE_ARRAY;
import static ea.code.generator.service.constants.JavaRestRecordsConstants.PATH_PARAM_WRAPPER;
import static ea.code.generator.service.constants.JavaRestRecordsConstants.REACTIVE_ARRAY;
import static ea.code.generator.service.constants.JavaRestRecordsConstants.REACTIVE_OBJECT;
import static ea.code.generator.service.constants.JavaRestRecordsConstants.REQUEST_BODY_WRAPPER;
import static ea.code.generator.service.constants.JavaRestRecordsConstants.REQUEST_PARAM_WRAPPER;

@Component
public class JavaRestRecordsMapper implements Function<GeneratorContext, JavaRestRecordVariableDTO> {

    @Override
    public JavaRestRecordVariableDTO apply(GeneratorContext generatorContext) {

        var params = generatorContext.getConfiguration().getParameters();
        var basePackage = params.containsKey("javaPackage")
                ? params.get("javaPackage") + "."
                : StringUtils.EMPTY;
        var restPackage = basePackage + JAVA_REST_CONTROLLER_PACKAGE;
        var modelPackage = basePackage + JAVA_MODEL_PACKAGE;

        return new JavaRestRecordVariableDTO(mapToRestControllers(restPackage, generatorContext),
                mapToJavaRecords(modelPackage, generatorContext));
    }

    private List<JavaRestRecordDTO> mapToRestControllers(String restPackage,
                                                         GeneratorContext generatorContext) {

        return generatorContext.getApiResources().stream()
                .map(apiResource -> mapToRestController(restPackage, generatorContext, apiResource))
                .toList();
    }

    private List<JavaRestRecordDTO> mapToJavaRecords(String modelPackage,
                                                     GeneratorContext generatorContext) {

        var records = new ArrayList<JavaRestRecordDTO>();
        var createdRecords = new HashSet<String>();

        var endpoints = generatorContext.getApiResources().stream()
                .map(ApiResource::getEndpoints)
                .flatMap(Collection::stream)
                .toList();

        endpoints.forEach(endpoint -> {
            if (endpoint.getRequest() != null) {
                handleRestRecord(modelPackage,
                        records,
                        createdRecords,
                        endpoint.getRequest().getProperty().getProperty());
            }

            endpoint.getResponses().forEach((httpStatus, httpMessage) -> handleRestRecord(modelPackage,
                    records,
                    createdRecords,
                    httpMessage.getProperty().getProperty()));
        });
        return records;
    }

    private void handleRestRecord(String modelPackage,
                                  List<JavaRestRecordDTO> records,
                                  Set<String> createdRecords,
                                  DTOProperty property) {

        if (createdRecords.contains(property.getName())) { //redundant
            return;
        }

        createdRecords.add(property.getName());

        var record = new JavaRestRecordDTO();
        record.setFolder(modelPackage.replaceAll("\\.", "/"));
        record.setFileName(property.getName());
        record
                .addVariable("package", modelPackage)
                .addVariable("recordName", property.getName());

        var recordParams = new ArrayList<JavaParam>();
        var imports = new TreeSet<String>();
        property.getChildProperties().forEach((name, wrapper) -> {

            var childProperty = wrapper.getProperty();
            var childJavaDataType = JAVA_DATA_TYPES_MAPPER.get(childProperty.getDataType());

            var isObject = childProperty.getDataType() == DataType.OBJECT;
            var attributeName = isObject
                    ? childProperty.getName()
                    : childJavaDataType.getDataType();

            if (wrapper.isArray()) {
                imports.add(IMPORT_LIST);
                attributeName = METHOD_REACTIVE_OR_ARR_WRAPPER.formatted(NON_REACTIVE_ARRAY, attributeName);
            }

            if (isObject) {
                handleRestRecord(modelPackage, records, createdRecords, childProperty);
            }

            if (childJavaDataType != null && childJavaDataType.getImportName() != null) {
                imports.add(childJavaDataType.getImportName());
            }

            recordParams.add(new JavaParam(attributeName, name));
        });

        record
                .addVariable("recordParams", recordParams)
                .addVariable("imports", imports);
        records.add(record);
    }

    private JavaRestRecordDTO mapToRestController(String restPackage,
                                                  GeneratorContext generatorContext,
                                                  ApiResource apiResource) {

        var config = generatorContext.getConfiguration();
        var imports = new TreeSet<String>();
        var endpoints = apiResource.getEndpoints().stream()
                .map(apiEndpoint -> mapToRestEndpoint(apiEndpoint, config, imports))
                .toList();

        var dto = new JavaRestRecordDTO();
        dto.setFileName(apiResource.getName());
        dto.setFolder(restPackage.replaceAll("\\.", "/"));

        dto
                .addVariable("package", restPackage)
                .addVariable("basePath", apiResource.getPath())
                .addVariable("controllerName", apiResource.getName())
                .addVariable("imports", imports)
                .addVariable("endpoints", endpoints);

        return dto;
    }

    private JavaEndpoint mapToRestEndpoint(ApiEndpoint apiEndpoint,
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

        var endpoint = new JavaEndpoint();
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
        endpoint.setParams(mapEndpointParams(apiEndpoint, imports));

        return endpoint;
    }

    private List<String> mapEndpointParams(ApiEndpoint apiEndpoint,
                                           Set<String> imports) {

        var params = new ArrayList<String>();

        if (apiEndpoint.getRequest() != null) {
            var objectName = apiEndpoint.getRequest().getProperty().getProperty().getName();
            params.add(REQUEST_BODY_WRAPPER.formatted(objectName, objectNameToVariableName(objectName)));
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

    private String objectNameToVariableName(String objectName) {

        return Character.toLowerCase(objectName.charAt(0)) + (objectName.length() > 1
                ? objectName.substring(1)
                : "");
    }

}
