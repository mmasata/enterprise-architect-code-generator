package ea.code.generator.context;

import ea.code.generator.api.rest.ApiResource;
import ea.code.generator.config.model.GeneratorConfiguration;
import lombok.Data;

import java.util.List;

@Data
public class GeneratorContext {

    private GeneratorConfiguration configuration;

    private List<ApiResource> apiResources;

    //TODO pak i kafka a modely
}
