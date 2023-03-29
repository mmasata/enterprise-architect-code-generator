package ea.code.generator.api;

import ea.code.generator.api.rest.enums.DataType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DTOProperty {

    private String name;

    private DataType dataType;

    private Map<String, DTOPropertyWrapper> childProperties = new HashMap<>();

    public DTOProperty addProperty(String key,
                                   DTOPropertyWrapper value) {

        childProperties.put(key, value);
        return this;
    }

}
