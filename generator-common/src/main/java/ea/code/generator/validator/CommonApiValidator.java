package ea.code.generator.validator;

import ea.code.generator.api.rest.ApiEndpoint;
import ea.code.generator.api.rest.ApiResource;
import ea.code.generator.context.GeneratorContext;
import ea.code.generator.validator.exception.GeneratorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class CommonApiValidator {

    private boolean fatalError = false;

    public void validate(GeneratorContext generatorContext) {

        var apiResources = generatorContext.getApiResources();
        var apiEndpoints = generatorContext.getApiResources().stream()
                .filter(Objects::nonNull)
                .map(ApiResource::getEndpoints)
                .flatMap(Collection::stream)
                .filter(Objects::nonNull)
                .toList();

        validateApiResources(apiResources);
        validateApiEndpoints(apiEndpoints);

        if (fatalError) {
            throw new GeneratorException();
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
