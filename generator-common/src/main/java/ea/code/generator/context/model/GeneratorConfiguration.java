package ea.code.generator.context.model;

import lombok.Data;

import java.util.Map;
import java.util.Set;

@Data
public final class GeneratorConfiguration {

    private String eaStartPackage;
    private String version;

    private MappingConfiguration mappingConfiguration;

    private DatabaseConnection databaseConnection;

    private Set<String> enabledGenerators;

    private Map<String, Object> parameters;
}
