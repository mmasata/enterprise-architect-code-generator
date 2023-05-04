package com.mmasata.eagenerator.service;

import com.mmasata.eagenerator.GeneratorHandler;
import com.mmasata.eagenerator.service.constants.SwaggerConstants;
import com.mmasata.eagenerator.service.mapper.SwaggerMapper;
import com.mmasata.eagenerator.annotations.Generator;
import com.mmasata.eagenerator.context.GeneratorContext;
import com.mmasata.eagenerator.processor.FileProcessor;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Generator(name = "swagger")
@RequiredArgsConstructor
public class SwaggerGeneratorService implements GeneratorHandler {

    private final GeneratorContext generatorContext;

    private final SwaggerMapper swaggerMapper;

    private final FileProcessor fileProcessor;

    @Override
    public void run() {

        var swaggerVariables = swaggerMapper.apply(generatorContext);
        var data = fileProcessor.processFreemarkerTemplate(SwaggerConstants.SWAGGER_FREEMARKER_TEMPLATE_FILE, swaggerVariables);

        fileProcessor.generate(Map.of("swagger.yaml", data));
    }

}

