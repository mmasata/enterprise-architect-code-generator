package ea.code.generator.service.mapper;

import ea.code.generator.api.DTOProperty;
import ea.code.generator.context.GeneratorContext;
import ea.code.generator.service.freemarker.model.AvroSchema;
import ea.code.generator.service.model.AvroSchemaDTO;
import ea.code.generator.service.model.enums.AvroDataType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

import static ea.code.generator.service.constants.AvroSchemaConstants.AVRO_DATA_TYPE_MAPPER;
import static ea.code.generator.service.constants.AvroSchemaConstants.AVRO_FILE_SUFFIX;

@Component
public class AvroSchemaMapper implements Function<GeneratorContext, List<AvroSchemaDTO>> {

    @Override
    public List<AvroSchemaDTO> apply(GeneratorContext generatorContext) {

        var dtoObjects = generatorContext.getDtoObjects();
        return dtoObjects.stream()
                .map(this::mapToAvroSchemaDTO)
                .toList();
    }

    private AvroSchemaDTO mapToAvroSchemaDTO(DTOProperty dtoProperty) {

        var avroSchemaDTO = new AvroSchemaDTO();
        avroSchemaDTO.setFileName(dtoProperty.getName() + AVRO_FILE_SUFFIX);
        avroSchemaDTO.addVariable("root", mapToAvroSchema(dtoProperty));

        return avroSchemaDTO;
    }

    private AvroSchema mapToAvroSchema(DTOProperty dtoProperty) {

        var avroDataType = AVRO_DATA_TYPE_MAPPER.get(dtoProperty.getDataType());

        var avroSchema = new AvroSchema();
        avroSchema.setName(dtoProperty.getName());
        avroSchema.setType(avroDataType.getDataType());
        avroSchema.setLogicalType(avroDataType.getLogicalType());

        if (avroDataType == AvroDataType.ENUM) {
            avroSchema.setEnumValues(dtoProperty.getEnumValues());
        }

        dtoProperty.getChildProperties().forEach((key, value) -> {
            var childAvroSchema = mapToAvroSchema(value.getProperty());
            childAvroSchema.setArray(value.isArray());

            avroSchema.addField(key, childAvroSchema);
        });

        return avroSchema;
    }

}
