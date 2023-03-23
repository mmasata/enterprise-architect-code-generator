package ea.code.generator.context.model;

import ea.code.generator.context.model.enums.MappingType;
import lombok.Data;

@Data
public class MappingConfiguration {

    private String profile;

    private MappingType type;

    //TODO configuration for generic MappingType
}
