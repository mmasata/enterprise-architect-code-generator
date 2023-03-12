package ea.code.generator.service;

import ea.code.generator.TestUtils;
import ea.code.generator.context.GeneratorContext;
import ea.code.generator.processor.FileProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class SwaggerServiceTest {

    private SwaggerService swaggerService;

    @Mock
    private FileProcessor fileProcessor;

    @BeforeEach
    void setup() {

        var context = new GeneratorContext();
        context.setConfiguration(TestUtils.mockGeneratorConfiguration());
        context.setApiResources(List.of(TestUtils.mockRestApiResource()));

        swaggerService = new SwaggerService(context, fileProcessor);
    }

    @Test
    void runTest() {

        //TODO
    }

}
