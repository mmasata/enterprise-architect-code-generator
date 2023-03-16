package ea.code.generator.api.rest;

import ea.code.generator.api.rest.enums.DataType;
import lombok.Data;

@Data
public class Parameter {

    private String name;
    private String format;
    private String example;

    private boolean required;

    private DataType dataType;

}
