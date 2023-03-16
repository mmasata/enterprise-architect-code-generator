package ea.code.generator.service.mapper;

import ea.code.generator.api.rest.ApiEndpoint;
import ea.code.generator.api.rest.Parameter;
import ea.code.generator.api.rest.enums.DataType;
import ea.code.generator.context.GeneratorContext;
import ea.code.generator.service.freemarker.model.SwaggerEndpoint;
import ea.code.generator.service.freemarker.model.SwaggerParameter;
import ea.code.generator.service.freemarker.model.SwaggerPath;
import ea.code.generator.service.freemarker.model.SwaggerResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import static ea.code.generator.service.constants.SwaggerConstants.SWAGGER_DATA_TYPE_MAPPER;
import static ea.code.generator.service.constants.SwaggerConstants.SWAGGER_TITLE_FORMATTED;

@Component
public class SwaggerMapper implements Function<GeneratorContext, Map<String, Object>> {

    @Override
    public Map<String, Object> apply(GeneratorContext generatorContext) {

        var swaggerPaths = new ArrayList<SwaggerPath>();
        var restApiResources = generatorContext.getApiResources();
        var configuration = generatorContext.getConfiguration();

        restApiResources.forEach(restApiResource -> {

            var groupedEndpoints = groupEndpointsByPath(restApiResource.getEndpoints());
            groupedEndpoints.forEach((path, endpoints) -> {

                var swaggerPathDTO = new SwaggerPath();
                swaggerPathDTO.setPath(restApiResource.getPath() + path);

                endpoints.forEach(endpoint -> {
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

        return Map.of("title", SWAGGER_TITLE_FORMATTED.formatted(configuration.getCompany(), configuration.getProject()),
                "version", configuration.getVersion(),
                "endpoints", swaggerPaths);
        //TODO components.schemas
    }

    private SwaggerEndpoint mapSwaggerEndpoint(ApiEndpoint apiEndpoint) {

        var path = apiEndpoint.getPathParams().stream().map(pathParam -> mapSwaggerParameter(pathParam, "path")).toList();
        var query = apiEndpoint.getQueryParams().stream().map(queryParam -> mapSwaggerParameter(queryParam, "query")).toList();
        var header = apiEndpoint.getHttpHeaders().stream().map(headerParam -> mapSwaggerParameter(headerParam, "header")).toList();

        var swaggerEndpoint = new SwaggerEndpoint();
        swaggerEndpoint.setOperationId(apiEndpoint.getName());
        swaggerEndpoint.setSwaggerParameters(Stream.of(path, query, header).flatMap(Collection::stream).toList());

        if (apiEndpoint.getRequest() != null) {
            swaggerEndpoint.setRequestSchemaName(apiEndpoint.getRequest().getModelName());
            swaggerEndpoint.setRequestContentType(apiEndpoint.getRequest().getContentType());
            swaggerEndpoint.setRequestSchemaArray(apiEndpoint.getRequest().isArray());
        }

        var responses = new ArrayList<SwaggerResponse>();
        apiEndpoint.getResponses().forEach((httpStatus, httpMessage) -> {

            var swaggerResponse = new SwaggerResponse();
            swaggerResponse.setCode(httpStatus.getCode());
            swaggerResponse.setSchemaName(httpMessage.getModelName());
            swaggerResponse.setContentType(httpMessage.getContentType());
            swaggerResponse.setDescription(httpStatus.getDescription());
            swaggerResponse.setArray(httpMessage.isArray());

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


        handleSwaggerDataType(swaggerParameter, parameter.getDataType());
        return swaggerParameter;
    }

    private void handleSwaggerDataType(SwaggerParameter swaggerParameter,
                                       DataType dataType) {

        var swaggerDataType = SWAGGER_DATA_TYPE_MAPPER.get(dataType);
        swaggerParameter.setType(swaggerDataType.getDataType());

        if(swaggerDataType.getFormat() != null) {
            swaggerParameter.setFormat(swaggerDataType.getFormat());
        }

        if(swaggerDataType.getExample() != null) {
            swaggerParameter.setExample(swaggerDataType.getExample());
        }
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
