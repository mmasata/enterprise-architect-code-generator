package ea.code.generator.service.model;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class JavaRestRecordDTO {

    private String folder;
    private String fileName;

    private Map<String, Object> freemarkerVariables = new HashMap<>();

    public JavaRestRecordDTO addVariable(String key,
                                           Object value) {

        freemarkerVariables.put(key, value);
        return this;
    }

}
