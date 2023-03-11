package ea.code.generator.embedded.service;

import ea.code.generator.context.GeneratorContext;
import ea.code.generator.annotations.GenerateCode;
import ea.code.generator.annotations.RunGenerator;
import lombok.RequiredArgsConstructor;

@GenerateCode(name = "swagger")
@RequiredArgsConstructor
public class SwaggerService {

    private final GeneratorContext generatorContext;

    @RunGenerator
    public void run() {

        var restApiResources = generatorContext.getApiResources();
        var company = generatorContext.getConfiguration().getCompany();
        //TODO
        System.out.println("Generating Swagger");
    }

}
