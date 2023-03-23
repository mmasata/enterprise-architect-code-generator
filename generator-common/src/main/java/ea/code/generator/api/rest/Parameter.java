package ea.code.generator.api.rest;

import ea.code.generator.api.rest.enums.DataType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Parameter {

    private String name;
    private String example;

    private boolean required;

    private DataType dataType;

}
