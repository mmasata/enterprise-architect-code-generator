package ea.code.generator.service.freemarker.model;

import lombok.Data;

import java.util.List;

@Data
public class SwaggerEndpoint {

    private String operationId;
    private String requestSchemaName;
    private String requestContentType;
    private String description;

    private boolean isRequestSchemaArray;

    public List<SwaggerParameter> swaggerParameters;

    public List<SwaggerResponse> swaggerResponses;
}
