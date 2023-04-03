package ea.code.generator.service.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SwaggerDataType {

    INT32("integer", "int32", "1234"),
    INT64("integer", "int64", null),
    FLOAT_NUMBER("number", "float", null),
    DOUBLE_NUMBER("number", "double", null),
    STRING("string", null, null),
    DATE("string", "date", null),
    DATETIME("string", "date-time", null),
    BOOLEAN("boolean", null, null),
    OBJECT("object", null, null),
    BIG_DECIMAL("string", "BigDecimal", null),
    ;

    private final String dataType;
    private final String format;
    private final String example;

}
