package ea.code.generator.service.freemarker.model;

import lombok.Data;

@Data
public class SwaggerPath {

    public String path;

    public SwaggerEndpoint get;
    public SwaggerEndpoint post;
    public SwaggerEndpoint put;
    public SwaggerEndpoint delete;
    public SwaggerEndpoint patch;
}
