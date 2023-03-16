package ea.code.generator.service.constants;

import ea.code.generator.api.rest.enums.DataType;
import ea.code.generator.service.model.SwaggerDataType;
import lombok.NoArgsConstructor;

import java.util.Map;

import static ea.code.generator.api.rest.enums.DataType.*;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class SwaggerConstants {

    public static final Map<DataType, SwaggerDataType> SWAGGER_DATA_TYPE_MAPPER = Map.of(
            INTEGER, new SwaggerDataType("integer", "int32", "1234"),
            LONG, new SwaggerDataType("integer", "int64", null),
            FLOAT, new SwaggerDataType("number", "float", null),
            DOUBLE, new SwaggerDataType("number", "double", null),
            STRING, new SwaggerDataType("string", null, null),
            DATE, new SwaggerDataType("string", "date", null),
            DATETIME, new SwaggerDataType("string", "date-time", null),
            BOOLEAN, new SwaggerDataType("boolean", null, null)
    );

    public static final String SWAGGER_TITLE_FORMATTED = "%s - %s";
    public static final String SWAGGER_FREEMARKER_TEMPLATE_FILE = "swagger.ftlh";

}
