package ea.code.generator.context;

import ea.code.generator.api.rest.ApiResource;
import ea.code.generator.context.model.GeneratorConfiguration;
import lombok.Data;

import java.util.List;

@Data
public class GeneratorContext {

    private GeneratorConfiguration configuration;

    private List<ApiResource> apiResources;

    //TODO Kafka and DTO models at future
}
