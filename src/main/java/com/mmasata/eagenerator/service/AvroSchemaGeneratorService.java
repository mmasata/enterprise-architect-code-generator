package com.mmasata.eagenerator.service;

import com.mmasata.eagenerator.GeneratorHandler;
import com.mmasata.eagenerator.processor.FileProcessor;
import com.mmasata.eagenerator.service.constants.AvroSchemaConstants;
import com.mmasata.eagenerator.annotations.Generator;
import com.mmasata.eagenerator.context.GeneratorContext;
import com.mmasata.eagenerator.service.mapper.AvroSchemaMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
@Generator(name = "avro-schema")
@RequiredArgsConstructor
public class AvroSchemaGeneratorService implements GeneratorHandler {

    private final GeneratorContext generatorContext;

    private final AvroSchemaMapper avroSchemaMapper;

    private final FileProcessor fileProcessor;

    @Override
    public void run() {

        var avroSchemas = avroSchemaMapper.apply(generatorContext);
        avroSchemas.forEach(avroSchemaDTO -> {

            var data = fileProcessor.processFreemarkerTemplate(AvroSchemaConstants.AVRO_FREEMARKER_TEMPLATE_FILE, avroSchemaDTO.getFreemarkerVariables());
            fileProcessor.generate(Map.of(avroSchemaDTO.getFileName(), data));
        });
    }

}
