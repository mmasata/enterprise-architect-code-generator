package com.mmasata.eagenerator.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmasata.eagenerator.context.GeneratorContext;
import com.mmasata.eagenerator.context.model.GeneratorConfiguration;
import com.mmasata.eagenerator.processor.FileProcessor;
import com.mmasata.eagenerator.processor.MapperProcessor;
import com.mmasata.eagenerator.validator.CommonApiValidator;
import freemarker.template.Configuration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

@Slf4j
@AutoConfiguration
@AutoConfigureAfter(value = {FreeMarkerAutoConfiguration.class})
public class GeneratorProcessorAutoConfiguration {

    @Bean
    @DependsOn("commonApiValidator")
    public MapperProcessor mapperProcessor(ApplicationContext applicationContext,
                                           GeneratorContext generatorContext,
                                           CommonApiValidator commonApiValidator) {

        return new MapperProcessor(generatorContext,
                applicationContext,
                commonApiValidator);
    }

    @Bean
    @DependsOn("dataSource")
    CommonApiValidator commonApiValidator() {
        return new CommonApiValidator();
    }

    @Bean
    @DependsOn("generatorContext")
    public DataSource dataSource(GeneratorContext generatorContext) {
        log.trace("Register DataSource bean");

        var databaseConnection = generatorContext.getConfiguration().getDatabaseConnection();
        var datasource = DataSourceBuilder.create()
                .url(databaseConnection.getUrl())
                .username(databaseConnection.getUser())
                .password(databaseConnection.getPassword())
                .build();

        try {
            log.info("Database connection valid = {}", datasource.getConnection().isValid(1000));
        } catch (SQLException e) {
            log.error("Invalid database connection", e);
            throw new RuntimeException(e);
        }
        return datasource;
    }

    @Bean
    public GeneratorContext generatorContext(ApplicationArguments args) {
        log.trace("Register GeneratorContext bean");

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

    @Bean
    public FileProcessor fileProcessor(Configuration configuration) {
        return new FileProcessor(configuration);
    }

    private GeneratorConfiguration getGeneratorConfiguration(String inputFilePath) {
        log.trace("Invoke getGeneratorConfiguration()");

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
