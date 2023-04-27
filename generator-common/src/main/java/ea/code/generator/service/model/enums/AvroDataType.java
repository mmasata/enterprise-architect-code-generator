package ea.code.generator.service.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AvroDataType {

    INT("int", null),
    LONG("long", null),
    FLOAT("float", null),
    DOUBLE("double", null),
    BYTES("bytes", null),
    STRING("string", null),
    BOOLEAN("boolean", null),
    RECORD("record", null),
    DATE("int", "date"),
    ENUM("enum", null),
    ;

    private final String dataType;
    private final String logicalType;
}
