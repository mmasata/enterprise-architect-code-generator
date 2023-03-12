package ea.code.generator.service;

import ea.code.generator.annotations.GenerateCode;
import ea.code.generator.annotations.RunGenerator;
import ea.code.generator.api.rest.ApiEndpoint;
import ea.code.generator.api.rest.Parameter;
import ea.code.generator.context.GeneratorContext;
import ea.code.generator.processor.FileProcessor;
import ea.code.generator.service.freemarker.model.SwaggerEndpoint;
import ea.code.generator.service.freemarker.model.SwaggerParameter;
import ea.code.generator.service.freemarker.model.SwaggerPath;
import ea.code.generator.service.freemarker.model.SwaggerResponse;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.stream.Stream;

@GenerateCode(name = "swagger")
@RequiredArgsConstructor
public class SwaggerService {

    private static final String TITLE_FORMATTED = "%s - %s";

    private final GeneratorContext generatorContext;

    private final FileProcessor fileProcessor;

    @RunGenerator
    public void run() {

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
                    }
                });
                swaggerPaths.add(swaggerPathDTO);
            });
        });

        var data = fileProcessor.processFreemarkerTemplate(
                "swagger.ftlh",
                Map.of("title", TITLE_FORMATTED.formatted(configuration.getCompany(), configuration.getProject()), "version", configuration.getVersion(), "endpoints", swaggerPaths));
        fileProcessor.generate(Map.of("export/swagger.yaml", data));
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
        }

        var responses = new ArrayList<SwaggerResponse>();
        apiEndpoint.getResponses().forEach((httpCode, httpMessage) -> {

            //TODO co kdyz je to ve formatu List<atd... >
            var swaggerResponse = new SwaggerResponse();
            swaggerResponse.setCode(httpCode);
            swaggerResponse.setSchemaName(httpMessage.getModelName());
            swaggerResponse.setContentType(httpMessage.getContentType());
            swaggerResponse.setDescription("TEST"); //TODO

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
                                       String dataType) {

        var dataTypeMapper = Map.of(
                "int", "integer",
                "String", "string",
                "Date", "string",
                "Datetime", "string",
                "boolean", "boolean"
        );

        var formatMapper = Map.of(
                "Date", "date",
                "Datetime", "date-time"
        );

        swaggerParameter.setType(dataTypeMapper.get(dataType));

        if (formatMapper.containsKey(dataType)) {
            swaggerParameter.setFormat(formatMapper.get(dataType));
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

