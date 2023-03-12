package ea.code.generator.config.model;

import lombok.Data;

import java.util.Set;

@Data
public final class GeneratorConfiguration {

    private String eaStartPackage;
    private String company;
    private String project;

    private DatabaseConnection databaseConnection;

    private Set<String> enabledGenerators;
    //TODO dalsi
}
