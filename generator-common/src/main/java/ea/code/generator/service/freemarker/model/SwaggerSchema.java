package ea.code.generator.service.freemarker.model;

import lombok.Data;

import java.util.List;

@Data
public class SwaggerSchema {

    private String name;
    private String type;
    private String format;
    private String example;

    private boolean isArray;

    private List<String> required;

    private List<SwaggerSchema> childs;
}
