package com.mmasata.eagenerator.service.mapper;

import com.mmasata.eagenerator.api.DTOProperty;
import com.mmasata.eagenerator.context.GeneratorContext;
import com.mmasata.eagenerator.service.constants.AvroSchemaConstants;
import com.mmasata.eagenerator.service.freemarker.model.AvroSchema;
import com.mmasata.eagenerator.service.model.AvroSchemaDTO;
import com.mmasata.eagenerator.service.model.enums.AvroDataType;

import java.util.List;
import java.util.function.Function;

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
        avroSchemaDTO.setFileName(dtoProperty.getName() + AvroSchemaConstants.AVRO_FILE_SUFFIX);
        avroSchemaDTO.addVariable("root", mapToAvroSchema(dtoProperty));

        return avroSchemaDTO;
    }

    private AvroSchema mapToAvroSchema(DTOProperty dtoProperty) {

        var avroDataType = AvroSchemaConstants.AVRO_DATA_TYPE_MAPPER.get(dtoProperty.getDataType());

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
