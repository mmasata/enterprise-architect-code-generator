package com.mmasata.eagenerator.processor;

import com.mmasata.eagenerator.api.rest.ApiEndpoint;
import com.mmasata.eagenerator.api.rest.ApiResource;
import com.mmasata.eagenerator.api.rest.HttpMessage;
import com.mmasata.eagenerator.api.rest.enums.HttpMethod;
import com.mmasata.eagenerator.api.rest.enums.HttpStatus;
import com.mmasata.eagenerator.context.GeneratorContext;
import com.mmasata.eagenerator.context.model.enums.MediaType;
import com.mmasata.eagenerator.exception.GeneratorException;
import com.mmasata.eagenerator.validator.CommonApiValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CommonApiValidatorTest {

    private CommonApiValidator commonApiValidator;

    @BeforeEach
    void setup() {
        commonApiValidator = new CommonApiValidator();
    }

    @ParameterizedTest
    @MethodSource("validateApiResourceProvider")
    void validate_apiResource(ApiResource apiResource, boolean fatalError) {
        var list = new ArrayList<ApiResource>();
        list.add(apiResource);

        var context = mockGeneratorContext(list);

        assertFalse(commonApiValidator.isFatalError());

        if (fatalError) {
            assertThrows(GeneratorException.class, () -> commonApiValidator.validate(context));
        } else {
            commonApiValidator.validate(context);
            assertFalse(commonApiValidator.isFatalError());
        }
    }

    @ParameterizedTest
    @MethodSource("validateApiEndpointProvider")
    void validate_apiEndpoint(ApiEndpoint apiEndpoint, boolean fatalError) {
        var validApiResource = new ApiResource("/path", "controller", "some_description", new ArrayList<>());
        validApiResource.getEndpoints().add(apiEndpoint);

        var context = mockGeneratorContext(List.of(validApiResource));

        assertFalse(commonApiValidator.isFatalError());
        if (fatalError) {
            assertThrows(GeneratorException.class, () -> commonApiValidator.validate(context));
        } else {
            commonApiValidator.validate(context);
            assertFalse(commonApiValidator.isFatalError());
        }
    }

    private static Stream<Arguments> validateApiEndpointProvider() {
        return Stream.of(
                Arguments.of(mockApiEndpoint(null, HttpMethod.GET, Map.of(HttpStatus.OK, new HttpMessage())), true),
                Arguments.of(mockApiEndpoint("name", null, Map.of(HttpStatus.OK, new HttpMessage())), true),
                Arguments.of(mockApiEndpoint(null, HttpMethod.GET, Collections.emptyMap()), true),
                Arguments.of(mockApiEndpoint("name", HttpMethod.POST, Map.of(HttpStatus.OK, new HttpMessage())), false)
        );
    }

    private static Stream<Arguments> validateApiResourceProvider() {
        return Stream.of(
                Arguments.of(null, true),
                Arguments.of(new ApiResource(null, "controller", "some_description", Collections.emptyList()), true),
                Arguments.of(new ApiResource("/path", null,"some_description", Collections.emptyList()), true),
                Arguments.of(new ApiResource("/path", "controller","some_description", Collections.emptyList()), false)
        );
    }

    private static GeneratorContext mockGeneratorContext(List<ApiResource> apiResources) {
        var context = new GeneratorContext();
        context.setApiResources(apiResources);

        return context;
    }

    private static ApiEndpoint mockApiEndpoint(String name,
                                               HttpMethod httpMethod,
                                               Map<HttpStatus, HttpMessage> responses) {
        return new ApiEndpoint(name,
                "/path",
                "description",
                MediaType.JSON.getValue(),
                MediaType.JSON.getValue(),
                httpMethod,
                null,
                responses,
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList());
    }

}
