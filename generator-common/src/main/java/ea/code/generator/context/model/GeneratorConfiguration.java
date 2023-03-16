package ea.code.generator.context.model;

import lombok.Data;

import java.util.Set;

@Data
public final class GeneratorConfiguration {

    private String eaStartPackage;
    private String company;
    private String project;
    private String version;

    private DatabaseConnection databaseConnection;

    private Set<String> enabledGenerators;
    //TODO another params
}
