package ea.code.generator.api.rest;

import ea.code.generator.api.rest.enums.HttpMethod;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ApiEndpoint {

    private String name;
    private String path;
    private String description;

    private HttpMethod httpMethod;
    private HttpMessage request;

    private Map<Integer, HttpMessage> responses;

    private List<Parameter> httpHeaders;
    private List<Parameter> pathParams;
    private List<Parameter> queryParams;

}
