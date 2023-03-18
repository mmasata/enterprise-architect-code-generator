package ea.code.generator.service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JavaDataType {

    INTEGER("Integer", null),
    LONG("Long", null),
    FLOAT("Float", null),
    DOUBLE("Double", null),
    STRING("String", null),
    DATE("LocalDate", "java.time.LocalDate"),
    DATETIME("LocalDateTime", "java.time.LocalDateTime"),
    BOOLEAN("Boolean", null),
    ;

    private final String dataType;
    private final String importName;
}
