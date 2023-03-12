package ea.code.generator.service;

import ea.code.generator.annotations.GenerateCode;
import ea.code.generator.annotations.RunGenerator;
import ea.code.generator.context.GeneratorContext;
import ea.code.generator.processor.FileProcessor;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@GenerateCode(name = "swagger")
@RequiredArgsConstructor
public class SwaggerService {

    private final GeneratorContext generatorContext;

    private final FileProcessor fileProcessor;

    @RunGenerator
    public void run() {

        var restApiResources = generatorContext.getApiResources();
        var company = generatorContext.getConfiguration().getCompany();
        //TODO
        System.out.println("-------- Generating Swagger");
        System.out.println(generatorContext.getConfiguration().getCompany());

        var data = fileProcessor.processFreemarkerTemplate("test.ftlh", Map.of("test", "myCoolSwaggerValue"));
        fileProcessor.generate(Map.of("export/myCoolSwagger.txt", data));
    }

}
