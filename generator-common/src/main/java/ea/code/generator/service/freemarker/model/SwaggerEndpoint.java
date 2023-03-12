package ea.code.generator.service.freemarker.model;

import lombok.Data;

import java.util.List;

@Data
public class SwaggerEndpoint {

    public String operationId;

    public String requestSchemaName;
    public String requestContentType;

    public List<SwaggerParameter> swaggerParameters;

    public List<SwaggerResponse> swaggerResponses;
}
