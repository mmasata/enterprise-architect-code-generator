package com.mmasata.eagenerator.service.constants;

import com.mmasata.eagenerator.api.rest.enums.DataType;
import com.mmasata.eagenerator.service.model.enums.AvroDataType;
import lombok.NoArgsConstructor;

import java.util.Map;

import static java.util.Map.entry;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class AvroSchemaConstants {

    public static final String AVRO_FILE_SUFFIX = ".avsc";

    public static final String AVRO_FREEMARKER_TEMPLATE_FILE = "avroSchemaDTO.ftlh";

    public static final Map<DataType, AvroDataType> AVRO_DATA_TYPE_MAPPER = Map.ofEntries(
            entry(DataType.INTEGER, AvroDataType.INT),
            entry(DataType.LONG, AvroDataType.LONG),
            entry(DataType.FLOAT, AvroDataType.FLOAT),
            entry(DataType.DOUBLE, AvroDataType.DOUBLE),
            entry(DataType.STRING, AvroDataType.STRING),
            entry(DataType.BOOLEAN, AvroDataType.BOOLEAN),
            entry(DataType.DATE, AvroDataType.DATE),
            entry(DataType.DATETIME, AvroDataType.DATE),
            entry(DataType.OBJECT, AvroDataType.RECORD),
            entry(DataType.ENUM, AvroDataType.ENUM)
    );

}
