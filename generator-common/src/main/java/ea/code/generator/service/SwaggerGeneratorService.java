package ea.code.generator.service;

import ea.code.generator.GeneratorRunner;
import ea.code.generator.annotations.GenerateCode;
import ea.code.generator.context.GeneratorContext;
import ea.code.generator.processor.FileProcessor;
import ea.code.generator.service.mapper.SwaggerMapper;
import lombok.RequiredArgsConstructor;

import java.util.Map;

import static ea.code.generator.service.constants.SwaggerConstants.SWAGGER_FREEMARKER_TEMPLATE_FILE;

@GenerateCode(name = "swagger")
@RequiredArgsConstructor
public class SwaggerGeneratorService implements GeneratorRunner {

    private final GeneratorContext generatorContext;

    private final SwaggerMapper swaggerMapper;

    private final FileProcessor fileProcessor;

    @Override
    public void run() {

        var swaggerVariables = swaggerMapper.apply(generatorContext);
        var data = fileProcessor.processFreemarkerTemplate(SWAGGER_FREEMARKER_TEMPLATE_FILE, swaggerVariables);

        fileProcessor.generate(Map.of("swagger.yaml", data));
    }

}

