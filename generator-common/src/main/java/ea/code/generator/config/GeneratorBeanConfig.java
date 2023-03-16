package ea.code.generator.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import ea.code.generator.context.model.GeneratorConfiguration;
import ea.code.generator.context.GeneratorContext;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;

@Configuration
@Slf4j
public class GeneratorBeanConfig {

    @SneakyThrows
    @Bean
    @DependsOn("registerGeneratorContext")
    public DataSource dataSource(@Autowired GeneratorContext generatorContext) {

        var databaseConnection = generatorContext.getConfiguration().getDatabaseConnection();
        var datasource = DataSourceBuilder.create()
                .url(databaseConnection.getUrl())
                .username(databaseConnection.getUser())
                .password(databaseConnection.getPassword())
                .build();

        log.info("Database connection valid = {}", datasource.getConnection().isValid(1000));
        return datasource;
    }

    @Bean
    public GeneratorContext registerGeneratorContext(ApplicationArguments args) {

        if (args.getSourceArgs().length < 1) {
            log.error("Path to input file is missing!");
            System.exit(0);
        }

        var inputFilePath = args.getSourceArgs()[0];
        log.trace("GeneratorConfiguration input file is {}", inputFilePath);

        var context = new GeneratorContext();
        context.setConfiguration(getGeneratorConfiguration(inputFilePath));

        log.trace("GeneratorContext Bean has been created");
        return context;
    }

    private GeneratorConfiguration getGeneratorConfiguration(String inputFilePath) {

        var inputFile = new File(inputFilePath);

        if (!inputFile.exists()) {
            log.error("File doesn't exists!");
            System.exit(0);
        }

        var objectMapper = new ObjectMapper();

        try {
            var generatorConfig = objectMapper.readValue(inputFile, GeneratorConfiguration.class);
            log.trace("Load GeneratorConfiguration to GeneratorContext");
            return generatorConfig;
        } catch (IOException e) {
            log.error("Cannot write config to DTO object!");
            throw new RuntimeException(e);
        }
    }

}
