package ea.code.generator.service.freemarker.model;

import lombok.Data;

@Data
public class SwaggerParameter {

    public String name;
    public String in;
    public String type;
    public String format;

    public boolean required;
}
