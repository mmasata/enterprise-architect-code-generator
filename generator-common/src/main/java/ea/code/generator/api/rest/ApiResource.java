package ea.code.generator.api.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResource {

    private String path;
    private String name;

    private List<ApiEndpoint> endpoints;
}
