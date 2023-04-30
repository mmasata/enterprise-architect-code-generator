package ea.code.generator.service;

import ea.code.generator.GeneratorRunner;
import ea.code.generator.annotations.GenerateCode;
import ea.code.generator.context.GeneratorContext;
import ea.code.generator.processor.FileProcessor;
import ea.code.generator.service.mapper.AvroSchemaMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import static ea.code.generator.service.constants.AvroSchemaConstants.AVRO_FREEMARKER_TEMPLATE_FILE;

@Slf4j
@GenerateCode(name = "avro-schema")
@RequiredArgsConstructor
public class AvroSchemaGeneratorService implements GeneratorRunner {

    private final GeneratorContext generatorContext;

    private final AvroSchemaMapper avroSchemaMapper;

    private final FileProcessor fileProcessor;

    @Override
    public void run() {

        var avroSchemas = avroSchemaMapper.apply(generatorContext);
        avroSchemas.forEach(avroSchemaDTO -> {

            var data = fileProcessor.processFreemarkerTemplate(AVRO_FREEMARKER_TEMPLATE_FILE, avroSchemaDTO.getFreemarkerVariables());
            fileProcessor.generate(Map.of(avroSchemaDTO.getFileName(), data));
        });
    }

}
