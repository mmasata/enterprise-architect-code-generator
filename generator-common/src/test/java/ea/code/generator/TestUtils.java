package ea.code.generator;

import ea.code.generator.api.rest.ApiEndpoint;
import ea.code.generator.api.rest.ApiResource;
import ea.code.generator.api.rest.DTOProperty;
import ea.code.generator.api.rest.DTOPropertyWrapper;
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

    public static GeneratorConfiguration mockGeneratorConfiguration() {

        var config = new GeneratorConfiguration();
        config.setCompany("SomeCompany");
        config.setProject("Dummy");
        config.setEaStartPackage("Api/TestPath");

        return config;
    }

    public static ApiResource mockRestApiResource() {

        var errorIdProperty = new DTOProperty("errorId", DataType.INTEGER, Collections.emptyMap());
        var errorMessageProperty = new DTOProperty("message", DataType.STRING, Collections.emptyMap());
        var descriptionProperty = new DTOProperty("description", DataType.STRING, Collections.emptyMap());
        var nameProperty = new DTOProperty("name", DataType.STRING, Collections.emptyMap());
        var idProperty = new DTOProperty("id", DataType.INTEGER, Collections.emptyMap());
        var validFromProperty = new DTOProperty("validFrom", DataType.DATE, Collections.emptyMap());
        var validToProperty = new DTOProperty("validTo", DataType.DATE, Collections.emptyMap());
        var createdTimeProperty = new DTOProperty("createdTime", DataType.DATETIME, Collections.emptyMap());

        var property3LevelTest = new DTOProperty("SubMyDTO", DataType.OBJECT,
                Map.of("id", new DTOPropertyWrapper(idProperty, true, false),
                        "names", new DTOPropertyWrapper(nameProperty, true, true)));

        var propertyInPropertyTest = new DTOProperty("MyDTO", DataType.OBJECT,
                Map.of("from", new DTOPropertyWrapper(validFromProperty, true, false),
                        "to", new DTOPropertyWrapper(validToProperty, true, false),
                        "name", new DTOPropertyWrapper(nameProperty, true, true),
                        "child", new DTOPropertyWrapper(property3LevelTest, true, true)));

        var propertyInPropertyDTO = new DTOProperty("DTORoot", DataType.OBJECT,
                Map.of("myDTO", new DTOPropertyWrapper(propertyInPropertyTest, true, true)));

        var unauthorizedResponseDTO = new DTOProperty("UnauthorizedResponse", DataType.OBJECT,
                Map.of("errorId", new DTOPropertyWrapper(errorIdProperty, true, false),
                        "message", new DTOPropertyWrapper(errorMessageProperty, true, false)));

        var getProductResponseDTO = new DTOProperty("GetProductResponse", DataType.OBJECT,
                Map.of("id", new DTOPropertyWrapper(idProperty, true, false),
                        "name", new DTOPropertyWrapper(nameProperty, true, false),
                        "description", new DTOPropertyWrapper(descriptionProperty, false, false)));

        var getProductDetailResponseDTO = new DTOProperty("GetProductDetailResponse", DataType.OBJECT,
                Map.of("id", new DTOPropertyWrapper(idProperty, true, false),
                        "name", new DTOPropertyWrapper(nameProperty, true, false),
                        "description", new DTOPropertyWrapper(descriptionProperty, false, false),
                        "validFrom", new DTOPropertyWrapper(validFromProperty, true, false),
                        "validTo", new DTOPropertyWrapper(validToProperty, true, false)));

        var createProductResponseDTO = new DTOProperty("CreateProductResponse", DataType.OBJECT,
                Map.of("createdTime", new DTOPropertyWrapper(createdTimeProperty, true, false)));

        var createProductRequestDTO = new DTOProperty("CreateProductRequest", DataType.OBJECT,
                Map.of("name", new DTOPropertyWrapper(nameProperty, true, false),
                        "description", new DTOPropertyWrapper(descriptionProperty, false, false)));


        var apiResource = new ApiResource();
        apiResource.setPath("/api/v1/products");

        var getProductsEndpoint = mockApiEndpoint(
                "getProducts",
                "Get products V1 Api Endpoint",
                "",
                HttpMethod.GET,
                null,
                Map.of(OK, mockHttpMessage(new DTOPropertyWrapper(getProductResponseDTO, false, true), RESPONSE),
                        Unauthorized, mockHttpMessage(new DTOPropertyWrapper(unauthorizedResponseDTO, false, false), RESPONSE)),
                List.of(mockParameter("Content-Type", DataType.STRING, null, false)),
                Collections.emptyList(),
                List.of(mockParameter("dateFrom", DataType.DATE, null, true), mockParameter("dateTo", DataType.DATE, null, true)));

        var getProductDetailEndpoint = mockApiEndpoint(
                "getProductById",
                "Get product by ID V1 Api Endpoint",
                "/{id}",
                HttpMethod.GET,
                null,
                Map.of(OK, mockHttpMessage(new DTOPropertyWrapper(getProductDetailResponseDTO, false, false), RESPONSE),
                        Unauthorized, mockHttpMessage(new DTOPropertyWrapper(unauthorizedResponseDTO, false, false), RESPONSE)),
                Collections.emptyList(),
                List.of(mockParameter("id", DataType.INTEGER, null, true)),
                Collections.emptyList());

        var createProductEndpoint = mockApiEndpoint(
                "createProduct",
                "Create product V1 Api Endpoint",
                "",
                HttpMethod.POST,
                mockHttpMessage(new DTOPropertyWrapper(propertyInPropertyDTO, true, false), REQUEST),
                Map.of(OK, mockHttpMessage(new DTOPropertyWrapper(createProductResponseDTO, false, false), RESPONSE),
                        Unauthorized, mockHttpMessage(new DTOPropertyWrapper(unauthorizedResponseDTO, false, false), RESPONSE)),
                List.of(mockParameter("Content-Type", DataType.STRING, null, true)),
                Collections.emptyList(),
                Collections.emptyList());

        apiResource.setEndpoints(List.of(getProductsEndpoint, getProductDetailEndpoint, createProductEndpoint));
        return apiResource;
    }

    private static ApiEndpoint mockApiEndpoint(String name,
                                               String description,
                                               String path,
                                               HttpMethod httpMethod,
                                               HttpMessage request,
                                               Map<HttpStatus, HttpMessage> responses,
                                               List<Parameter> httpHeaders,
                                               List<Parameter> pathParams,
                                               List<Parameter> queryParams) {

        var apiEndpoint = new ApiEndpoint();
        apiEndpoint.setName(name);
        apiEndpoint.setDescription(description);
        apiEndpoint.setPath(path);
        apiEndpoint.setHttpMethod(httpMethod);
        apiEndpoint.setRequest(request);
        apiEndpoint.setResponses(responses);
        apiEndpoint.setHttpHeaders(httpHeaders);
        apiEndpoint.setPathParams(pathParams);
        apiEndpoint.setQueryParams(queryParams);

        return apiEndpoint;
    }

    private static HttpMessage mockHttpMessage(DTOPropertyWrapper dtoPropertyWrapper,
                                               HttpMessageType httpMessageType) {

        var httpMessage = new HttpMessage();
        httpMessage.setProperty(dtoPropertyWrapper);
        httpMessage.setHttpMessageType(httpMessageType);

        return httpMessage;
    }

    private static Parameter mockParameter(String name,
                                           DataType dataType,
                                           String example,
                                           boolean required) {

        var parameter = new Parameter();
        parameter.setName(name);
        parameter.setDataType(dataType);
        parameter.setExample(example);
        parameter.setRequired(required);

        return parameter;
    }

}
