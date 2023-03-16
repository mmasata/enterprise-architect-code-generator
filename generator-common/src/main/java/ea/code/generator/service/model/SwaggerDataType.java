package ea.code.generator.service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SwaggerDataType {

    private String dataType;
    private String format;
    private String example;

}
