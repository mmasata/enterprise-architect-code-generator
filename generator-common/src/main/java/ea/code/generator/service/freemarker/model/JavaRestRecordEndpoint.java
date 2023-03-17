package ea.code.generator.service.freemarker.model;

import lombok.Data;

import java.util.List;

@Data
public class JavaRestRecordEndpoint {

    private String httpMethod;
    private String path;
    private String returnType;
    private String methodName;

    private List<String> params;

}
