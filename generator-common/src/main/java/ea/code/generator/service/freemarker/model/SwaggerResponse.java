package ea.code.generator.service.freemarker.model;

import lombok.Data;

@Data
public class SwaggerResponse {

    public int code;

    public String contentType;
    public String schemaName;
    public String description;
}
