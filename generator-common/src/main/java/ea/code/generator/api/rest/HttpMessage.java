package ea.code.generator.api.rest;

import ea.code.generator.api.rest.enums.HttpMessageType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HttpMessage {

    private DTOPropertyWrapper property;

    private HttpMessageType httpMessageType;
}
