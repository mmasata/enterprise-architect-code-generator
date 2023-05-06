package com.mmasata.eagenerator.processor;

import com.mmasata.eagenerator.GeneratorHandler;
import com.mmasata.eagenerator.annotations.Generator;
import com.mmasata.eagenerator.context.GeneratorContext;
import com.mmasata.eagenerator.context.model.GeneratorConfiguration;
import com.mmasata.eagenerator.exception.GeneratorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class GeneratorInitializrListenerTest {

    private static final String PROFILE_NAME = "profile";

    @Mock
    private GeneratorContext generatorContext;

    @Mock
    private ConfigurableApplicationContext applicationContext;

    @Mock
    private MapperProcessor mapperProcessor;

    @Mock
    private ApplicationReadyEvent applicationReadyEvent;

    @InjectMocks
    private GeneratorInitializrListener generatorInitializrListener;

    @BeforeEach
    void setup() {
        when(applicationReadyEvent.getApplicationContext()).thenReturn(applicationContext);
    }

    @Test
    void runTest_noEnabledGeneratorsError() {
        when(generatorContext.getConfiguration()).thenReturn(mockGeneratorConfiguration(null));
        assertThrows(GeneratorException.class, () -> generatorInitializrListener.onApplicationEvent(applicationReadyEvent));

        when(generatorContext.getConfiguration()).thenReturn(mockGeneratorConfiguration(Set.of("someText")));
        when(applicationContext.getBeansWithAnnotation(Generator.class)).thenReturn(Collections.emptyMap());
        assertThrows(GeneratorException.class, () -> generatorInitializrListener.onApplicationEvent(applicationReadyEvent));

        verify(mapperProcessor, times(2)).run();
        verify(generatorContext, times(2)).getConfiguration();
    }

    @Test
    void runTest_generatorNotInstanceOfGeneratorRunnerError() {
        when(generatorContext.getConfiguration()).thenReturn(mockGeneratorConfiguration(Set.of(PROFILE_NAME)));
        when(applicationContext.getBeansWithAnnotation(Generator.class)).thenReturn(Map.of(PROFILE_NAME, new GeneratorWithoutImplementation()));
        assertThrows(GeneratorException.class, () -> generatorInitializrListener.onApplicationEvent(applicationReadyEvent));

        verify(mapperProcessor).run();
        verify(generatorContext).getConfiguration();
    }

    @Test
    void runTest_shouldPass() {
        when(generatorContext.getConfiguration()).thenReturn(mockGeneratorConfiguration(Set.of(PROFILE_NAME)));
        when(applicationContext.getBeansWithAnnotation(Generator.class)).thenReturn(Map.of(PROFILE_NAME, new GeneratorWithImplementation()));
        assertDoesNotThrow(() -> generatorInitializrListener.onApplicationEvent(applicationReadyEvent));

        verify(mapperProcessor).run();
        verify(generatorContext).getConfiguration();
    }

    private GeneratorConfiguration mockGeneratorConfiguration(Set<String> enabledGenerators) {
        var generatorConfiguration = new GeneratorConfiguration();
        generatorConfiguration.setEnabledGenerators(enabledGenerators);

        return generatorConfiguration;
    }

    @Generator(name = PROFILE_NAME)
    class GeneratorWithoutImplementation {
    }


    @Generator(name = PROFILE_NAME)
    class GeneratorWithImplementation implements GeneratorHandler {
        @Override
        public void run() {
            //not implemented
        }
    }
}
