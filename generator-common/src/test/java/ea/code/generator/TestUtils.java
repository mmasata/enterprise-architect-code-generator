package ea.code.generator;

import ea.code.generator.api.rest.ApiEndpoint;
import ea.code.generator.api.rest.ApiResource;
import ea.code.generator.api.rest.HttpMessage;
import ea.code.generator.api.rest.Parameter;
import ea.code.generator.api.rest.enums.DataType;
import ea.code.generator.api.rest.enums.HttpMessageType;
import ea.code.generator.api.rest.enums.HttpMethod;
import ea.code.generator.api.rest.enums.HttpStatus;
import ea.code.generator.context.model.GeneratorConfiguration;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static ea.code.generator.api.rest.enums.HttpMessageType.REQUEST;
import static ea.code.generator.api.rest.enums.HttpMessageType.RESPONSE;
import static ea.code.generator.api.rest.enums.HttpStatus.*;
import static ea.code.generator.api.rest.enums.HttpStatus.Unauthorized;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class TestUtils {

    private static final HttpMessage UNAUTHORIZED_RESPONSE = mockHttpMessage("UnauthorizedResponse", "application/json", RESPONSE, false);

    public static GeneratorConfiguration mockGeneratorConfiguration() {

        var config = new GeneratorConfiguration();
        config.setCompany("SomeCompany");
        config.setProject("Dummy");
        config.setEaStartPackage("Api/TestPath");

        return config;
    }

    public static ApiResource mockRestApiResource() {

        var apiResource = new ApiResource();
        apiResource.setPath("/api/v1/products");

        var getProductsEndpoint = mockApiEndpoint(
                "getProducts"
                , "",
                HttpMethod.GET,
                null,
                Map.of(OK, mockHttpMessage("GetProductResponse", "application/json", RESPONSE, true), Unauthorized, UNAUTHORIZED_RESPONSE),
                List.of(mockParameter("Content-Type", DataType.STRING, null, null, false)),
                Collections.emptyList(),
                List.of(mockParameter("dateFrom", DataType.DATE, null, null, true), mockParameter("dateTo", DataType.DATE, null, null, true)));

        var getProductDetailEndpoint = mockApiEndpoint(
                "getProductById"
                , "/{id}",
                HttpMethod.GET,
                null,
                Map.of(OK, mockHttpMessage("GetProductDetailResponse", "application/json", RESPONSE, false), Unauthorized, UNAUTHORIZED_RESPONSE),
                Collections.emptyList(),
                List.of(mockParameter("id", DataType.INTEGER, null, null, true)),
                Collections.emptyList());

        var createProductEndpoint = mockApiEndpoint(
                "createProduct"
                , "",
                HttpMethod.POST,
                mockHttpMessage("CreateProductRequest", "application/json", REQUEST, false),
                Map.of(Created, mockHttpMessage("CreateProductResponse", "application/json", RESPONSE, false), Unauthorized, UNAUTHORIZED_RESPONSE),
                List.of(mockParameter("Content-Type", DataType.STRING, null, null, true)),
                Collections.emptyList(),
                Collections.emptyList());

        var createProductsEndpoint = mockApiEndpoint(
                "createProducts"
                , "/test",
                HttpMethod.POST,
                mockHttpMessage("CreateProductRequest", "application/json", REQUEST, true),
                Map.of(Created, mockHttpMessage("CreateProductResponse", "application/json", RESPONSE, true), Unauthorized, UNAUTHORIZED_RESPONSE),
                List.of(mockParameter("Content-Type", DataType.STRING, null, null, true)),
                Collections.emptyList(),
                Collections.emptyList());

        apiResource.setEndpoints(List.of(getProductsEndpoint, getProductDetailEndpoint, createProductEndpoint, createProductsEndpoint));
        return apiResource;
    }

    private static ApiEndpoint mockApiEndpoint(String name,
                                               String path,
                                               HttpMethod httpMethod,
                                               HttpMessage request,
                                               Map<HttpStatus, HttpMessage> responses,
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
                                               HttpMessageType httpMessageType,
                                               boolean isArray) {

        var httpMessage = new HttpMessage();
        httpMessage.setModelName(modelName);
        httpMessage.setContentType(contentType);
        httpMessage.setHttpMessageType(httpMessageType);
        httpMessage.setArray(isArray);

        return httpMessage;
    }

    private static Parameter mockParameter(String name,
                                           DataType dataType,
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
