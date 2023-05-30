package com.mmasata.eagenerator.service.mapper;

import com.mmasata.eagenerator.api.DTOProperty;
import com.mmasata.eagenerator.api.rest.ApiEndpoint;
import com.mmasata.eagenerator.api.rest.Parameter;
import com.mmasata.eagenerator.context.GeneratorContext;
import com.mmasata.eagenerator.context.model.SwaggerConfiguration;
import com.mmasata.eagenerator.service.constants.SwaggerConstants;
import com.mmasata.eagenerator.service.freemarker.model.SwaggerEndpoint;
import com.mmasata.eagenerator.service.freemarker.model.SwaggerParameter;
import com.mmasata.eagenerator.service.freemarker.model.SwaggerPath;
import com.mmasata.eagenerator.service.freemarker.model.SwaggerResponse;
import com.mmasata.eagenerator.service.freemarker.model.SwaggerSchema;
import com.mmasata.eagenerator.service.model.enums.SwaggerDataType;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

public class SwaggerMapper implements Function<GeneratorContext, Map<String, Object>> {

    @Override
    public Map<String, Object> apply(GeneratorContext generatorContext) {

        var mainSchemasProperties = new HashSet<DTOProperty>();
        var swaggerPaths = new ArrayList<SwaggerPath>();

        var restApiResources = generatorContext.getApiResources();
        var config = generatorContext.getConfiguration();
        var swaggerConfig = (SwaggerConfiguration) config.getSwagger();

        restApiResources.forEach(restApiResource -> {

            var groupedEndpoints = groupEndpointsByPath(restApiResource.getEndpoints());
            groupedEndpoints.forEach((path, endpoints) -> {

                var swaggerPathDTO = new SwaggerPath();
                swaggerPathDTO.setPath(restApiResource.getPath() + path);

                endpoints.forEach(endpoint -> {

                    if (endpoint.getRequest() != null) {
                        mainSchemasProperties.add(endpoint.getRequest().getProperty().getProperty());
                    }

                    endpoint.getResponses().forEach(((httpStatus, httpMessage) -> mainSchemasProperties.add(httpMessage.getProperty().getProperty())));

                    switch (endpoint.getHttpMethod()) {
                        case GET -> swaggerPathDTO.setGet(mapSwaggerEndpoint(endpoint));
                        case POST -> swaggerPathDTO.setPost(mapSwaggerEndpoint(endpoint));
                        case PUT -> swaggerPathDTO.setPut(mapSwaggerEndpoint(endpoint));
                        case DELETE -> swaggerPathDTO.setDelete(mapSwaggerEndpoint(endpoint));
                        case PATCH -> swaggerPathDTO.setPatch(mapSwaggerEndpoint(endpoint));
                        case OPTIONS -> swaggerPathDTO.setOptions(mapSwaggerEndpoint(endpoint));
                    }
                });
                swaggerPaths.add(swaggerPathDTO);
            });
        });

        var swaggerSchemas = mainSchemasProperties.stream()
                .map(this::mapSwaggerSchema)
                .toList();

        return Map.of("title", swaggerConfig.getTitle(),
                "version", config.getVersion(),
                "endpoints", swaggerPaths,
                "schemas", swaggerSchemas);
    }

    private SwaggerSchema mapSwaggerSchema(DTOProperty dtoProperty) {

        var swaggerDataType = SwaggerConstants.SWAGGER_DATA_TYPE_MAPPER.get(dtoProperty.getDataType());
        var swaggerSchema = new SwaggerSchema();

        var requiredChilds = new ArrayList<String>();
        var childs = new ArrayList<SwaggerSchema>();

        swaggerSchema.setName(dtoProperty.getName());
        swaggerSchema.setType(swaggerDataType.getDataType());

        if (!CollectionUtils.isEmpty(dtoProperty.getChildProperties())) {
            dtoProperty.getChildProperties().forEach((name, childProperty) -> {

                if (childProperty.isRequired()) {
                    requiredChilds.add(name);
                }

                var child = mapSwaggerSchema(childProperty.getProperty());
                child.setName(name);
                child.setArray(childProperty.isArray());

                childs.add(child);
            });
        }

        if (swaggerDataType == SwaggerDataType.ENUM) {
            swaggerSchema.setEnumValues(dtoProperty.getEnumValues());
        }

        if (!CollectionUtils.isEmpty(requiredChilds)) {
            swaggerSchema.setRequired(requiredChilds);
        }

        if (!CollectionUtils.isEmpty(childs)) {
            swaggerSchema.setChilds(childs);
        }

        if (swaggerDataType.getFormat() != null) {
            swaggerSchema.setFormat(swaggerDataType.getFormat());
        }

        if (swaggerDataType.getExample() != null) {
            swaggerSchema.setExample(swaggerDataType.getExample());
        }

        return swaggerSchema;
    }

    private SwaggerEndpoint mapSwaggerEndpoint(ApiEndpoint apiEndpoint) {

        var path = apiEndpoint.getPathParams().stream().map(pathParam -> mapSwaggerParameter(pathParam, "path")).toList();
        var query = apiEndpoint.getQueryParams().stream().map(queryParam -> mapSwaggerParameter(queryParam, "query")).toList();
        var header = apiEndpoint.getHttpHeaders().stream().map(headerParam -> mapSwaggerParameter(headerParam, "header")).toList();

        var swaggerEndpoint = new SwaggerEndpoint();
        swaggerEndpoint.setOperationId(apiEndpoint.getName());
        swaggerEndpoint.setSwaggerParameters(Stream.of(path, query, header).flatMap(Collection::stream).toList());

        if (apiEndpoint.getDescription() != null) {
            swaggerEndpoint.setDescription(apiEndpoint.getDescription());
        }

        if (apiEndpoint.getRequest() != null) {
            swaggerEndpoint.setRequestSchemaName(apiEndpoint.getRequest().getProperty().getProperty().getName());
            swaggerEndpoint.setRequestSchemaArray(apiEndpoint.getRequest().getProperty().isArray());
        }

        var responses = new ArrayList<SwaggerResponse>();
        apiEndpoint.getResponses().forEach((httpStatus, httpMessage) -> {

            var swaggerResponse = new SwaggerResponse();
            swaggerResponse.setCode(httpStatus.getCode());
            swaggerResponse.setDescription(httpStatus.getDescription());
            swaggerResponse.setSchemaName(httpMessage.getProperty().getProperty().getName());
            swaggerResponse.setArray(httpMessage.getProperty().isArray());
            responses.add(swaggerResponse);
        });
        swaggerEndpoint.setSwaggerResponses(responses);


        return swaggerEndpoint;
    }

    private SwaggerParameter mapSwaggerParameter(Parameter parameter,
                                                 String type) {

        var swaggerParameter = new SwaggerParameter();
        swaggerParameter.setName(parameter.getName());
        swaggerParameter.setIn(type);
        swaggerParameter.setRequired(parameter.isRequired());

        var swaggerDataType = SwaggerConstants.SWAGGER_DATA_TYPE_MAPPER.get(parameter.getDataType());
        swaggerParameter.setType(swaggerDataType.getDataType());

        if (swaggerDataType.getFormat() != null) {
            swaggerParameter.setFormat(swaggerDataType.getFormat());
        }

        if (swaggerDataType.getExample() != null) {
            swaggerParameter.setExample(swaggerDataType.getExample());
        }
        return swaggerParameter;
    }

    private Map<String, List<ApiEndpoint>> groupEndpointsByPath(List<ApiEndpoint> apiEndpoints) {

        var map = new HashMap<String, List<ApiEndpoint>>();

        apiEndpoints.forEach(endpoint -> {

            if (!map.containsKey(endpoint.getPath())) {
                map.put(endpoint.getPath(), new ArrayList<>());
            }

            map.get(endpoint.getPath()).add(endpoint);
        });

        return map;
    }

}
