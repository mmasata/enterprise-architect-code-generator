package ea.code.generator.service.constants;

import ea.code.generator.api.rest.enums.DataType;
import ea.code.generator.service.model.enums.SwaggerDataType;
import lombok.NoArgsConstructor;

import java.util.Map;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class SwaggerConstants {

    public static final Map<DataType, SwaggerDataType> SWAGGER_DATA_TYPE_MAPPER = Map.of(
            DataType.INTEGER, SwaggerDataType.INT32,
            DataType.LONG, SwaggerDataType.INT64,
            DataType.FLOAT, SwaggerDataType.FLOAT_NUMBER,
            DataType.DOUBLE, SwaggerDataType.DOUBLE_NUMBER,
            DataType.STRING, SwaggerDataType.STRING,
            DataType.DATE, SwaggerDataType.DATE,
            DataType.DATETIME, SwaggerDataType.DATETIME,
            DataType.BOOLEAN, SwaggerDataType.BOOLEAN,
            DataType.OBJECT, SwaggerDataType.OBJECT,
            DataType.BIG_DECIMAL, SwaggerDataType.BIG_DECIMAL
    );

    public static final String SWAGGER_TITLE_FORMATTED = "%s - %s";
    public static final String SWAGGER_FREEMARKER_TEMPLATE_FILE = "swagger.ftlh";

}
