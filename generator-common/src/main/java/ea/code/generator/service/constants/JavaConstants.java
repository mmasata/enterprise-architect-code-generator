package ea.code.generator.service.constants;

import ea.code.generator.api.rest.enums.DataType;
import ea.code.generator.api.rest.enums.HttpStatus;
import ea.code.generator.service.model.enums.JavaDataType;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Set;

import static ea.code.generator.api.rest.enums.HttpStatus.BadGateway;
import static ea.code.generator.api.rest.enums.HttpStatus.BadRequest;
import static ea.code.generator.api.rest.enums.HttpStatus.Conflict;
import static ea.code.generator.api.rest.enums.HttpStatus.ExpectationFailed;
import static ea.code.generator.api.rest.enums.HttpStatus.Forbidden;
import static ea.code.generator.api.rest.enums.HttpStatus.GatewayTimeout;
import static ea.code.generator.api.rest.enums.HttpStatus.GenericClientError;
import static ea.code.generator.api.rest.enums.HttpStatus.GenericServerError;
import static ea.code.generator.api.rest.enums.HttpStatus.Gone;
import static ea.code.generator.api.rest.enums.HttpStatus.HttpVersionNotSupported;
import static ea.code.generator.api.rest.enums.HttpStatus.ImATeapot;
import static ea.code.generator.api.rest.enums.HttpStatus.InternalServerError;
import static ea.code.generator.api.rest.enums.HttpStatus.LengthRequired;
import static ea.code.generator.api.rest.enums.HttpStatus.MethodNotAllowed;
import static ea.code.generator.api.rest.enums.HttpStatus.NotAcceptable;
import static ea.code.generator.api.rest.enums.HttpStatus.NotFound;
import static ea.code.generator.api.rest.enums.HttpStatus.NotImplemented;
import static ea.code.generator.api.rest.enums.HttpStatus.PaymentRequired;
import static ea.code.generator.api.rest.enums.HttpStatus.PreconditionFailed;
import static ea.code.generator.api.rest.enums.HttpStatus.ProxyAuthenticationRequired;
import static ea.code.generator.api.rest.enums.HttpStatus.RequestEntityTooLarge;
import static ea.code.generator.api.rest.enums.HttpStatus.RequestRangeNotSatisfiable;
import static ea.code.generator.api.rest.enums.HttpStatus.RequestTimeout;
import static ea.code.generator.api.rest.enums.HttpStatus.RequestedURITooLong;
import static ea.code.generator.api.rest.enums.HttpStatus.ServiceUnavailable;
import static ea.code.generator.api.rest.enums.HttpStatus.Unauthorized;
import static ea.code.generator.api.rest.enums.HttpStatus.UnsupportedMediaType;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class JavaConstants {

    public static final String JAVA_RECORDS_FREEMARKER_TEMPLATE_FILE = "javaRecordDTO.ftlh";
    public static final String JAVA_LOMBOK_DTO_FREEMARKER_TEMPLATE_FILE = "javaLombokDTO.ftlh";
    public static final String JAVA_REST_CONTROLLER_FREEMARKER_TEMPLATE_FILE = "javaRestController.ftlh";
    public static final String JAVA_POM_FREEMARKER_TEMPLATE_FILE = "pom.ftlh";
    public static final String JAVA_PACKAGE_INFO_FREEMARKER_TEMPLATE_FILE = "package-info.ftlh";

    public static final String JAVA_PACKAGE_PARAM = "javaPackage";
    public static final String JAVA_PROJECT_NAME = "javaProjectName";
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

    public static final String CONTROLLER_MODE_REACTIVE = "REACTIVE";
    public static final String CONTROLLER_MODE_STANDARD = "STANDARD";

    public static final Set<HttpStatus> ERROR_STATUSES = Set.of(BadRequest, Unauthorized, PaymentRequired,
            Forbidden, NotFound, MethodNotAllowed, NotAcceptable, ProxyAuthenticationRequired, RequestTimeout,
            Conflict, Gone, LengthRequired, PreconditionFailed, RequestEntityTooLarge, RequestedURITooLong, UnsupportedMediaType,
            RequestRangeNotSatisfiable, ExpectationFailed, ImATeapot, InternalServerError, NotImplemented, BadGateway, ServiceUnavailable,
            GatewayTimeout, HttpVersionNotSupported, GenericClientError, GenericServerError);

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
