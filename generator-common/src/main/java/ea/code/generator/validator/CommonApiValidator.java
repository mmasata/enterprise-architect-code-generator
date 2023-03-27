package ea.code.generator.validator;

import ea.code.generator.api.rest.ApiResource;
import ea.code.generator.context.GeneratorContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class CommonApiValidator {

    public void validate(GeneratorContext generatorContext) {
        validateApiResources(generatorContext.getApiResources());
        //TODO kafka atd..
    }

    private void validateApiResources(List<ApiResource> apiResources) {
        //TODO
    }

}
