package ea.code.generator.api;

import ea.code.generator.api.rest.enums.DataType;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class DTOProperty {

    private String name;

    private DataType dataType;

    private List<String> enumValues = new ArrayList<>();

    private Map<String, DTOPropertyWrapper> childProperties = new HashMap<>();

    public DTOProperty addProperty(String key,
                                   DTOPropertyWrapper value) {

        childProperties.put(key, value);
        return this;
    }

    public DTOProperty addEnumValue(String value) {

        enumValues.add(value);
        return this;
    }

}
