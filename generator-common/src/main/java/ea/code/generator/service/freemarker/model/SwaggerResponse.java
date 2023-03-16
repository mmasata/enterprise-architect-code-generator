package ea.code.generator.service.freemarker.model;

import lombok.Data;

@Data
public class SwaggerResponse {

    private int code;

    private String contentType;
    private String schemaName;
    private String description;

    private boolean isArray;
}
