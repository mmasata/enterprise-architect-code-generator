package ea.code.generator.api.rest;

import lombok.Data;

import java.util.List;

@Data
public class ApiResource {

    private String path;

    private List<ApiEndpoint> endpoints;
}
