package com.mmasata.eagenerator.validator;

import com.mmasata.eagenerator.api.rest.ApiEndpoint;
import com.mmasata.eagenerator.api.rest.ApiResource;
import com.mmasata.eagenerator.context.GeneratorContext;
import com.mmasata.eagenerator.exception.GeneratorException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Validation component for finding badly mapped attributes in common-api.
 */
@Slf4j
public class CommonApiValidator {

    @Getter
    private boolean fatalError = false;

    /**
     * The method starts the validation component.
     * @param generatorContext Framework generator context (contains common-api).
     */
    public void validate(GeneratorContext generatorContext) {

        var apiResources = generatorContext.getApiResources();
        var apiEndpoints = generatorContext.getApiResources().stream()
                .filter(Objects::nonNull)
                .map(ApiResource::getEndpoints)
                .flatMap(Collection::stream)
                .filter(Objects::nonNull)
                .toList();

        if(!CollectionUtils.isEmpty(apiResources)) {
            validateApiResources(apiResources);
        }

        if(!CollectionUtils.isEmpty(apiEndpoints)) {
            validateApiEndpoints(apiEndpoints);
        }

        if (fatalError) {
            throw new GeneratorException("Generator validation fatal error!");
        }
    }

    private void validateApiResources(List<ApiResource> apiResources) {
        apiResources.forEach(apiResource -> {

            if (apiResource == null) {
                log.error("[validator] - Api resources cannot be null!");
                fatalError = true;
                return;
            }

            if (apiResource.getName() == null) {
                log.error("[validator] - Api resource name is missing!");
                fatalError = true;
            }

            if (apiResource.getPath() == null) {
                log.error("[validator] - Api resource path is missing!");
                fatalError = true;
            }

            if (CollectionUtils.isEmpty(apiResource.getEndpoints())) {
                log.warn("[validator] - Api resource is without endpoints!");
            }
        });
    }

    private void validateApiEndpoints(List<ApiEndpoint> apiEndpoints) {
        apiEndpoints.forEach(apiEndpoint -> {

            if (apiEndpoint.getHttpMethod() == null) {
                log.error("[validator] - Api endpoint HTTP method is missing!");
                fatalError = true;
            }

            if (apiEndpoint.getName() == null) {
                log.error("[validator] - Api endpoint name is missing!");
                fatalError = true;
            }

            if (apiEndpoint.getResponses().isEmpty()) {
                log.error("[validator] - Api endpoint responses are missing!");
                fatalError = true;
            }
        });
    }

}
