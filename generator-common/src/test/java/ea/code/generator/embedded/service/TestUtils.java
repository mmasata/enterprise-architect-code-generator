package ea.code.generator.embedded.service;

import ea.code.generator.api.rest.ApiEndpoint;
import ea.code.generator.api.rest.ApiResource;
import ea.code.generator.api.rest.HttpMessage;
import ea.code.generator.api.rest.Parameter;
import ea.code.generator.api.rest.enums.HttpMessageType;
import ea.code.generator.api.rest.enums.HttpMethod;
import ea.code.generator.config.model.GeneratorConfiguration;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static ea.code.generator.api.rest.enums.HttpMessageType.REQUEST;
import static ea.code.generator.api.rest.enums.HttpMessageType.RESPONSE;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
final class TestUtils {

    private static final HttpMessage UNAUTHORIZED_RESPONSE = mockHttpMessage("UnauthorizedResponse", "application/json", RESPONSE);

    static GeneratorConfiguration mockGeneratorConfiguration() {

        var config = new GeneratorConfiguration();
        config.setCompany("SomeCompany");
        config.setProject("Dummy");
        config.setEaStartPackage("Api/TestPath");

        return config;
    }

    static ApiResource mockRestApiResource() {

        var apiResource = new ApiResource();
        apiResource.setPath("/api/v1/products");

        var getProductsEndpoint = mockApiEndpoint(
                "getProducts"
                , null,
                HttpMethod.GET,
                null,
                Map.of(200, mockHttpMessage("List<GetProductResponse>", "application/json", RESPONSE), 401, UNAUTHORIZED_RESPONSE),
                List.of(mockParameter("Content-Type", "String", null, null, false)),
                Collections.emptyList(),
                List.of(mockParameter("dateFrom", "Date", null, null, true), mockParameter("dateTo", "Date", null, null, true)));

        var getProductDetailEndpoint = mockApiEndpoint(
                "getProductById"
                , "/{id}",
                HttpMethod.GET,
                null,
                Map.of(200, mockHttpMessage("GetProductDetailResponse", "application/json", RESPONSE), 401, UNAUTHORIZED_RESPONSE),
                Collections.emptyList(),
                List.of(mockParameter("id", "int", null, null, true)),
                Collections.emptyList());

        var createProductEndpoint = mockApiEndpoint(
                "createProduct"
                , null,
                HttpMethod.POST,
                mockHttpMessage("CreateProductRequest", "application/json", REQUEST),
                Map.of(201, mockHttpMessage("CreateProductResponse", "application/json", RESPONSE), 401, UNAUTHORIZED_RESPONSE),
                List.of(mockParameter("Content-Type", "String", null, null, true)),
                Collections.emptyList(),
                Collections.emptyList());

        apiResource.setEndpoints(List.of(getProductsEndpoint, getProductDetailEndpoint, createProductEndpoint));
        return apiResource;
    }

    private static ApiEndpoint mockApiEndpoint(String name,
                                               String path,
                                               HttpMethod httpMethod,
                                               HttpMessage request,
                                               Map<Integer, HttpMessage> responses,
                                               List<Parameter> httpHeaders,
                                               List<Parameter> pathParams,
                                               List<Parameter> queryParams) {

        var apiEndpoint = new ApiEndpoint();
        apiEndpoint.setName(name);
        apiEndpoint.setPath(path);
        apiEndpoint.setHttpMethod(httpMethod);
        apiEndpoint.setRequest(request);
        apiEndpoint.setResponses(responses);
        apiEndpoint.setHttpHeaders(httpHeaders);
        apiEndpoint.setPathParams(pathParams);
        apiEndpoint.setQueryParams(queryParams);

        return apiEndpoint;
    }

    private static HttpMessage mockHttpMessage(String modelName,
                                               String contentType,
                                               HttpMessageType httpMessageType) {

        var httpMessage = new HttpMessage();
        httpMessage.setModelName(modelName);
        httpMessage.setContentType(contentType);
        httpMessage.setHttpMessageType(httpMessageType);

        return httpMessage;
    }

    private static Parameter mockParameter(String name,
                                           String dataType,
                                           String format,
                                           String example,
                                           boolean required) {

        var parameter = new Parameter();
        parameter.setName(name);
        parameter.setDataType(dataType);
        parameter.setFormat(format);
        parameter.setExample(example);
        parameter.setRequired(required);

        return parameter;
    }

}
