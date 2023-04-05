package ea.code.generator.service.constants;

import ea.code.generator.api.rest.enums.DataType;
import ea.code.generator.service.model.enums.SwaggerDataType;
import lombok.NoArgsConstructor;

import java.util.Map;

import static java.util.Map.entry;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class SwaggerConstants {

    public static final Map<DataType, SwaggerDataType> SWAGGER_DATA_TYPE_MAPPER = Map.ofEntries(
            entry(DataType.INTEGER, SwaggerDataType.INT32),
            entry(DataType.LONG, SwaggerDataType.INT64),
            entry(DataType.FLOAT, SwaggerDataType.FLOAT_NUMBER),
            entry(DataType.DOUBLE, SwaggerDataType.DOUBLE_NUMBER),
            entry(DataType.STRING, SwaggerDataType.STRING),
            entry(DataType.DATE, SwaggerDataType.DATE),
            entry(DataType.DATETIME, SwaggerDataType.DATETIME),
            entry(DataType.BOOLEAN, SwaggerDataType.BOOLEAN),
            entry(DataType.OBJECT, SwaggerDataType.OBJECT),
            entry(DataType.BIG_DECIMAL, SwaggerDataType.BIG_DECIMAL),
            entry(DataType.ENUM, SwaggerDataType.ENUM)
    );

    public static final String SWAGGER_FREEMARKER_TEMPLATE_FILE = "swagger.ftlh";

}
