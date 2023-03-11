package ea.code.generator;

import com.fasterxml.jackson.databind.ObjectMapper;
import ea.code.generator.annotations.GenerateCode;
import ea.code.generator.annotations.RunGenerator;
import ea.code.generator.config.GeneratorConfiguration;
import ea.code.generator.context.GeneratorContext;
import ea.code.generator.processor.EaProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
@SpringBootApplication
public class EaGeneratorApplication {

    private static final String GENERATOR_CONTEXT_NAME = "GENERATOR_CONTEXT_BEAN";

    private static ApplicationContext applicationContext;

    public static void run(Class mainClass, String[] args) {

        applicationContext = SpringApplication.run(mainClass, args);
        runCodeGenerator();
    }

    @Bean(name = GENERATOR_CONTEXT_NAME)
    public GeneratorContext registerGeneratorContext(ApplicationArguments args) {

        if (args.getSourceArgs().length < 1) {
            log.error("Path to input file is missing!");
            System.exit(0);
        }

        var inputFilePath = args.getSourceArgs()[0];
        log.trace("GeneratorConfiguration input file is {}", inputFilePath);

        var context = new GeneratorContext();
        context.setConfiguration(getGeneratorConfiguration(inputFilePath));

        EaProcessor.getInstance().run(context);
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

    private static void runCodeGenerator() {

        var generatorContext = (GeneratorContext) applicationContext.getBean(GENERATOR_CONTEXT_NAME);
        var enabledCodeGenerators = generatorContext.getConfiguration().getEnabledGenerators();

        if (enabledCodeGenerators == null) {
            log.error("Parameter enabledGenerators is missing in config file!");
            System.exit(0);
        }

        var codeGenBeans = applicationContext.getBeansWithAnnotation(GenerateCode.class);
        var relevantBeans = codeGenBeans.keySet().stream()
                .map(key -> codeGenBeans.get(key))
                .filter(bean -> enabledCodeGenerators.contains(bean.getClass().getAnnotation(GenerateCode.class).name()))
                .toList();

        relevantBeans.forEach(bean -> Arrays.stream(bean.getClass().getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RunGenerator.class))
                .forEach(method -> {
                    try {
                        log.info("Running {}.{}() code generator.", bean.getClass().getName(), method.getName());
                        method.invoke(bean);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                }));
    }

}