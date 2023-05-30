package com.mmasata.eagenerator.service.constants;

import com.mmasata.eagenerator.api.rest.enums.DataType;
import com.mmasata.eagenerator.api.rest.enums.HttpStatus;
import com.mmasata.eagenerator.service.model.enums.JavaDataType;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Set;

import static java.util.Map.entry;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class JavaConstants {

    public static final String JAVA_RECORDS_FREEMARKER_TEMPLATE_FILE = "javaRecordDTO.ftlh";
    public static final String JAVA_LOMBOK_DTO_FREEMARKER_TEMPLATE_FILE = "javaLombokDTO.ftlh";
    public static final String JAVA_REST_CONTROLLER_FREEMARKER_TEMPLATE_FILE = "javaRestController.ftlh";
    public static final String JAVA_ENUM_FREEMARKER_TEMPLATE_FILE = "javaEnumDTO.ftlh";
    public static final String JAVA_POM_FREEMARKER_TEMPLATE_FILE = "pom.ftlh";
    public static final String JAVA_PACKAGE_INFO_FREEMARKER_TEMPLATE_FILE = "package-info.ftlh";

    public static final String JAVA_FILE_OUTPUT_PATTERN = "%s/%s/%s.java";
    public static final String JAVA_PROJECT_MAIN_PATTERN = "%s/src/main/java";
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

    public static final String IMPORT_NULLABLE = "org.springframework.lang.Nullable";
    public static final String IMPORT_LIST = "java.util.List";
    public static final String IMPORT_MONO = "reactor.core.publisher.Mono";
    public static final String IMPORT_FLUX = "reactor.core.publisher.Flux";
    public static final String IMPORT_REQUEST_PARAM = "org.springframework.web.bind.annotation.RequestParam";
    public static final String IMPORT_PATH_PARAM = "org.springframework.web.bind.annotation.PathVariable";
    public static final String IMPORT_REQUEST_HEADER = "org.springframework.web.bind.annotation.RequestHeader";
    public static final String IMPORT_REQUEST_BODY = "org.springframework.web.bind.annotation.RequestBody;";

    public static final Set<HttpStatus> ERROR_STATUSES = Set.of(HttpStatus.BadRequest, HttpStatus.Unauthorized, HttpStatus.PaymentRequired,
            HttpStatus.Forbidden, HttpStatus.NotFound, HttpStatus.MethodNotAllowed, HttpStatus.NotAcceptable, HttpStatus.ProxyAuthenticationRequired, HttpStatus.RequestTimeout,
            HttpStatus.Conflict, HttpStatus.Gone, HttpStatus.LengthRequired, HttpStatus.PreconditionFailed, HttpStatus.RequestEntityTooLarge, HttpStatus.RequestedURITooLong, HttpStatus.UnsupportedMediaType,
            HttpStatus.RequestRangeNotSatisfiable, HttpStatus.ExpectationFailed, HttpStatus.ImATeapot, HttpStatus.InternalServerError, HttpStatus.NotImplemented, HttpStatus.BadGateway, HttpStatus.ServiceUnavailable,
            HttpStatus.GatewayTimeout, HttpStatus.HttpVersionNotSupported, HttpStatus.GenericClientError, HttpStatus.GenericServerError);

    public static final Map<DataType, JavaDataType> JAVA_DATA_TYPES_MAPPER = Map.ofEntries(
            entry(DataType.INTEGER, JavaDataType.INTEGER),
            entry(DataType.LONG, JavaDataType.LONG),
            entry(DataType.FLOAT, JavaDataType.FLOAT),
            entry(DataType.DOUBLE, JavaDataType.DOUBLE),
            entry(DataType.STRING, JavaDataType.STRING),
            entry(DataType.DATE, JavaDataType.DATE),
            entry(DataType.DATETIME, JavaDataType.DATETIME),
            entry(DataType.BOOLEAN, JavaDataType.BOOLEAN),
            entry(DataType.BIG_DECIMAL, JavaDataType.BIG_DECIMAL),
            entry(DataType.ENUM, JavaDataType.ENUM),
            entry(DataType.OBJECT, JavaDataType.OBJECT)
    );
}
