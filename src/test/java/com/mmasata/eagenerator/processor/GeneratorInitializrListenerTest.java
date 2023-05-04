package com.mmasata.eagenerator.processor;

import com.mmasata.eagenerator.GeneratorHandler;
import com.mmasata.eagenerator.annotations.Generator;
import com.mmasata.eagenerator.context.GeneratorContext;
import com.mmasata.eagenerator.context.model.GeneratorConfiguration;
import com.mmasata.eagenerator.exception.GeneratorException;
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
class GeneratorInitializrListenerTest {

    private static final String PROFILE_NAME = "profile";

    @Mock
    private GeneratorContext generatorContext;

    @Mock
    private ApplicationContext applicationContext;

    @Mock
    private MapperProcessor mapperProcessor;

    @InjectMocks
    private GeneratorInitializrListener generatorInitializrListener;

    @Test
    void runTest_noEnabledGeneratorsError() {
        when(generatorContext.getConfiguration()).thenReturn(mockGeneratorConfiguration(null));
        assertThrows(GeneratorException.class, () -> generatorInitializrListener.onApplicationEvent(null));

        when(generatorContext.getConfiguration()).thenReturn(mockGeneratorConfiguration(Set.of("someText")));
        when(applicationContext.getBeansWithAnnotation(Generator.class)).thenReturn(Collections.emptyMap());
        assertThrows(GeneratorException.class, () -> generatorInitializrListener.onApplicationEvent(null));

        verify(mapperProcessor, times(2)).run();
        verify(generatorContext, times(2)).getConfiguration();
    }

    @Test
    void runTest_generatorNotInstanceOfGeneratorRunnerError() {
        when(generatorContext.getConfiguration()).thenReturn(mockGeneratorConfiguration(Set.of(PROFILE_NAME)));
        when(applicationContext.getBeansWithAnnotation(Generator.class)).thenReturn(Map.of(PROFILE_NAME, new GeneratorWithoutImplementation()));
        assertThrows(GeneratorException.class, () -> generatorInitializrListener.onApplicationEvent(null));

        verify(mapperProcessor).run();
        verify(generatorContext).getConfiguration();
    }

    @Test
    void runTest_shouldPass() {
        when(generatorContext.getConfiguration()).thenReturn(mockGeneratorConfiguration(Set.of(PROFILE_NAME)));
        when(applicationContext.getBeansWithAnnotation(Generator.class)).thenReturn(Map.of(PROFILE_NAME, new GeneratorWithImplementation()));
        assertDoesNotThrow(() -> generatorInitializrListener.onApplicationEvent(null));

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
