package ea.code.generator.api.rest;

import ea.code.generator.api.rest.enums.HttpMessageType;
import lombok.Data;

@Data
public class HttpMessage {

    private DTOPropertyWrapper property;

    private HttpMessageType httpMessageType;
}
