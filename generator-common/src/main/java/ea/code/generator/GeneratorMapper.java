package ea.code.generator;

import ea.code.generator.api.DTOProperty;
import ea.code.generator.api.rest.ApiResource;

import java.util.List;

public interface GeneratorMapper {

    List<ApiResource> mapApiResources();

    List<DTOProperty> mapDtoObjects();
}
