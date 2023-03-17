package ea.code.generator.api.rest;

import ea.code.generator.api.rest.enums.DataType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DTOProperty {

    private String name;

    private DataType dataType;

    private Map<String, DTOPropertyWrapper> childProperties;

}
