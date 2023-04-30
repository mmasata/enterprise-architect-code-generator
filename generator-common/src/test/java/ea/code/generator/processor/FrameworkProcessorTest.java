package ea.code.generator.processor;

import ea.code.generator.GeneratorRunner;
import ea.code.generator.annotations.GenerateCode;
import ea.code.generator.context.GeneratorContext;
import ea.code.generator.context.model.GeneratorConfiguration;
import ea.code.generator.validator.exception.GeneratorException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FrameworkProcessorTest {

    private static final String PROFILE_NAME = "profile";

    @Mock
    private GeneratorContext generatorContext;

    @Mock
    private ApplicationContext applicationContext;

    @Mock
    private EaProcessor eaProcessor;

    @InjectMocks
    private FrameworkProcessor frameworkProcessor;

    @Test
    void runTest_noEnabledGeneratorsError() {
        when(generatorContext.getConfiguration()).thenReturn(mockGeneratorConfiguration(null));
        assertThrows(GeneratorException.class, () -> frameworkProcessor.run());

        when(generatorContext.getConfiguration()).thenReturn(mockGeneratorConfiguration(Set.of("someText")));
        when(applicationContext.getBeansWithAnnotation(GenerateCode.class)).thenReturn(Collections.emptyMap());
        assertThrows(GeneratorException.class, () -> frameworkProcessor.run());

        verify(eaProcessor, times(2)).run();
        verify(generatorContext, times(2)).getConfiguration();
    }

    @Test
    void runTest_generatorNotInstanceOfGeneratorRunnerError() {
        when(generatorContext.getConfiguration()).thenReturn(mockGeneratorConfiguration(Set.of(PROFILE_NAME)));
        when(applicationContext.getBeansWithAnnotation(GenerateCode.class)).thenReturn(Map.of(PROFILE_NAME, new GeneratorWithoutImplementation()));
        assertThrows(GeneratorException.class, () -> frameworkProcessor.run());

        verify(eaProcessor).run();
        verify(generatorContext).getConfiguration();
    }

    @Test
    void runTest_shouldPass() {
        when(generatorContext.getConfiguration()).thenReturn(mockGeneratorConfiguration(Set.of(PROFILE_NAME)));
        when(applicationContext.getBeansWithAnnotation(GenerateCode.class)).thenReturn(Map.of(PROFILE_NAME, new GeneratorWithImplementation()));
        assertDoesNotThrow(() -> frameworkProcessor.run());

        verify(eaProcessor).run();
        verify(generatorContext).getConfiguration();
    }

    private GeneratorConfiguration mockGeneratorConfiguration(Set<String> enabledGenerators) {
        var generatorConfiguration = new GeneratorConfiguration();
        generatorConfiguration.setEnabledGenerators(enabledGenerators);

        return generatorConfiguration;
    }

    @GenerateCode(name = PROFILE_NAME)
    class GeneratorWithoutImplementation {
    }


    @GenerateCode(name = PROFILE_NAME)
    class GeneratorWithImplementation implements GeneratorRunner {
        @Override
        public void run() {
            //not implemented
        }
    }
}
