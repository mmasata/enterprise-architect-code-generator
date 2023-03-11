package ea.code.generator.api.rest;

import lombok.Data;

@Data
public class Parameter {

    private String name;
    private String dataType;
    private String format;
    private String example;

    private boolean required;

}
