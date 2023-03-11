package ea.code.generator.embedded.service;

import ea.code.generator.context.GeneratorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static ea.code.generator.embedded.service.TestUtils.mockGeneratorConfiguration;
import static ea.code.generator.embedded.service.TestUtils.mockRestApiResource;

@ExtendWith(MockitoExtension.class)
class SwaggerServiceTest {

    private SwaggerService swaggerService;

    @BeforeEach
    void setup() {

        var context = new GeneratorContext();
        context.setConfiguration(mockGeneratorConfiguration());
        context.setApiResources(List.of(mockRestApiResource()));

        swaggerService = new SwaggerService(context);
    }

    @Test
    void runTest() {

        //TODO
    }

}
