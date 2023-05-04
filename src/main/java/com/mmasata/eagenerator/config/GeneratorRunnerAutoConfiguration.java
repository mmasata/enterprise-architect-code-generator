package com.mmasata.eagenerator.config;

import com.mmasata.eagenerator.context.GeneratorContext;
import com.mmasata.eagenerator.processor.FileProcessor;
import com.mmasata.eagenerator.service.AvroSchemaGeneratorService;
import com.mmasata.eagenerator.service.JavaGeneratorService;
import com.mmasata.eagenerator.service.SwaggerGeneratorService;
import com.mmasata.eagenerator.service.helper.JavaHelper;
import com.mmasata.eagenerator.service.mapper.AvroSchemaMapper;
import com.mmasata.eagenerator.service.mapper.JavaDTOMapper;
import com.mmasata.eagenerator.service.mapper.JavaRestControllerMapper;
import com.mmasata.eagenerator.service.mapper.SwaggerMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;

@Slf4j
@AutoConfiguration
@AutoConfigureAfter(value = {GeneratorProcessorAutoConfiguration.class})
@Import(value = {GeneratorContext.class, FileProcessor.class})
@RequiredArgsConstructor
public class GeneratorRunnerAutoConfiguration {

    private final GeneratorContext generatorContext;

    private final FileProcessor fileProcessor;

    @Bean
    @DependsOn("swaggerMapper")
    public SwaggerGeneratorService swaggerGeneratorService(SwaggerMapper swaggerMapper) {
        return new SwaggerGeneratorService(generatorContext,
                swaggerMapper,
                fileProcessor);
    }

    @Bean
    @DependsOn({"javaHelper", "javaDTOMapper", "javaRestControllerMapper"})
    public JavaGeneratorService javaGeneratorService(JavaHelper javaHelper,
                                                     JavaDTOMapper javaDTOMapper,
                                                     JavaRestControllerMapper javaRestControllerMapper) {
        return new JavaGeneratorService(generatorContext,
                javaRestControllerMapper,
                javaDTOMapper,
                javaHelper);
    }

    @Bean
    @DependsOn("avroSchemaMapper")
    public AvroSchemaGeneratorService avroSchemaGeneratorService(AvroSchemaMapper avroSchemaMapper) {
        return new AvroSchemaGeneratorService(generatorContext,
                avroSchemaMapper,
                fileProcessor);
    }

    @Bean
    @DependsOn("javaHelper")
    public JavaRestControllerMapper javaRestControllerMapper(JavaHelper javaHelper) {
        return new JavaRestControllerMapper(javaHelper);
    }

    @Bean
    @DependsOn("javaHelper")
    public JavaDTOMapper javaDTOMapper(JavaHelper javaHelper) {
        return new JavaDTOMapper(javaHelper);
    }

    @Bean
    public SwaggerMapper swaggerMapper() {
        return new SwaggerMapper();
    }

    @Bean
    public AvroSchemaMapper avroSchemaMapper() {
        return new AvroSchemaMapper();
    }

    @Bean
    public JavaHelper javaHelper() {
        return new JavaHelper(fileProcessor,
                generatorContext);
    }

}
