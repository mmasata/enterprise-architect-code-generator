package ea.code.generator.context;

import ea.code.generator.api.DTOProperty;
import ea.code.generator.api.rest.ApiResource;
import ea.code.generator.context.model.GeneratorConfiguration;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class GeneratorContext {

    private GeneratorConfiguration configuration;

    private List<ApiResource> apiResources = Collections.emptyList();

    private List<DTOProperty> dtoObjects = Collections.emptyList();

}
