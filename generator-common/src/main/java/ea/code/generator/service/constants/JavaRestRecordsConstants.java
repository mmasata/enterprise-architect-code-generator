package ea.code.generator.service.constants;

import ea.code.generator.api.rest.enums.DataType;
import ea.code.generator.service.model.JavaDataType;
import lombok.NoArgsConstructor;

import java.util.Map;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class JavaRestRecordsConstants {

    public static final String JAVA_RECORDS_FREEMARKER_TEMPLATE_FILE = "javaRecordDTO.ftlh";
    public static final String JAVA_REST_CONTROLLER_FREEMARKER_TEMPLATE_FILE = "javaRestController.ftlh";
    public static final String JAVA_REST_CONTROLLER_PACKAGE = "rest";
    public static final String JAVA_MODEL_PACKAGE = "model";
    public static final String REACTIVE_ARRAY = "Flux";
    public static final String NON_REACTIVE_ARRAY = "List";
    public static final String REACTIVE_OBJECT = "Mono";
    public static final String METHOD_REACTIVE_OR_ARR_WRAPPER = "%s<%s>";
    public static final String METHOD_RETURN_TYPE_WRAPPER = "ResponseEntity<%s>";
    public static final String REQUEST_BODY_WRAPPER = "@RequestBody %s %s";
    public static final String REQUEST_PARAM_WRAPPER = "@RequestParam %s %s";
    public static final String PATH_PARAM_WRAPPER = "@PathVariable %s %s";
    public static final String HEADER_PARAM_WRAPPER = "@RequestHeader(\"%s\") %s %s";

    public static final String IMPORT_LIST = "java.util.List";
    public static final String IMPORT_MONO = "reactor.core.publisher.Mono";
    public static final String IMPORT_FLUX = "reactor.core.publisher.Flux";
    public static final String IMPORT_REQUEST_PARAM = "org.springframework.web.bind.annotations.RequestParam";
    public static final String IMPORT_PATH_PARAM = "org.springframework.web.bind.annotations.PathVariable";
    public static final String IMPORT_REQUEST_HEADER = "org.springframework.web.bind.annotations.RequestHeader";

    public static final Map<DataType, JavaDataType> JAVA_DATA_TYPES_MAPPER = Map.of(
            DataType.INTEGER, JavaDataType.INTEGER,
            DataType.LONG, JavaDataType.LONG,
            DataType.FLOAT, JavaDataType.FLOAT,
            DataType.DOUBLE, JavaDataType.DOUBLE,
            DataType.STRING, JavaDataType.STRING,
            DataType.DATE, JavaDataType.DATE,
            DataType.DATETIME, JavaDataType.DATETIME,
            DataType.BOOLEAN, JavaDataType.BOOLEAN,
            DataType.BIG_DECIMAL, JavaDataType.BIG_DECIMAL
    );
}
