package ea.code.generator.processor;

import ea.code.generator.GeneratorMapper;
import ea.code.generator.annotations.CustomGeneratorMapper;
import ea.code.generator.api.DTOProperty;
import ea.code.generator.api.rest.ApiResource;
import ea.code.generator.context.GeneratorContext;
import ea.code.generator.context.model.GeneratorConfiguration;
import ea.code.generator.context.model.MappingConfiguration;
import ea.code.generator.context.model.enums.MappingType;
import ea.code.generator.validator.CommonApiValidator;
import ea.code.generator.validator.exception.GeneratorException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EaProcessorTest {

    private static final String PROFILE_NAME = "profile";

    @Mock
    private GeneratorContext generatorContext;

    @Mock
    private ApplicationContext applicationContext;

    @Mock
    private CommonApiValidator commonApiValidator;

    @InjectMocks
    private EaProcessor eaProcessor;

    @Test
    void runTest_generic_notImplementedError() {
        when(generatorContext.getConfiguration()).thenReturn(mockGeneratorConfiguration(MappingType.GENERIC, null));
        assertThrows(GeneratorException.class, () -> eaProcessor.run());
        verify(generatorContext).getConfiguration();
        verify(commonApiValidator, never()).validate(any());
    }

    @Test
    void runTest_custom_missingMapperError() {
        when(generatorContext.getConfiguration()).thenReturn(mockGeneratorConfiguration(MappingType.CUSTOM, null));
        when(applicationContext.getBeansWithAnnotation(CustomGeneratorMapper.class)).thenReturn(Collections.emptyMap());
        assertThrows(GeneratorException.class, () -> eaProcessor.run());

        when(generatorContext.getConfiguration()).thenReturn(mockGeneratorConfiguration(MappingType.CUSTOM, "someProfileWhichDontExistInTestContext"));
        assertThrows(GeneratorException.class, () -> eaProcessor.run());
        verify(commonApiValidator, never()).validate(any());
    }

    @Test
    void runTest_custom_mapperNotInstanceOfGeneratorMapperError() {
        when(generatorContext.getConfiguration()).thenReturn(mockGeneratorConfiguration(MappingType.CUSTOM, PROFILE_NAME));
        when(applicationContext.getBeansWithAnnotation(CustomGeneratorMapper.class)).thenReturn(Map.of(PROFILE_NAME, new MapperWithoutImplementation()));
        assertThrows(GeneratorException.class, () -> eaProcessor.run());
        verify(commonApiValidator, never()).validate(any());
    }

    @Test
    void runTest_shouldPass() {
        when(generatorContext.getConfiguration()).thenReturn(mockGeneratorConfiguration(MappingType.CUSTOM, PROFILE_NAME));
        when(applicationContext.getBeansWithAnnotation(CustomGeneratorMapper.class)).thenReturn(Map.of(PROFILE_NAME, new MapperWithImplementation()));
        assertDoesNotThrow(() -> eaProcessor.run());
        verify(commonApiValidator).validate(any());
    }

    private GeneratorConfiguration mockGeneratorConfiguration(MappingType mappingType,
                                                              String profile) {

        var generatorConfiguration = new GeneratorConfiguration();
        generatorConfiguration.setMappingConfiguration(new MappingConfiguration());
        generatorConfiguration.getMappingConfiguration().setType(mappingType);
        generatorConfiguration.getMappingConfiguration().setProfile(profile);

        return generatorConfiguration;
    }

    @CustomGeneratorMapper(name = PROFILE_NAME)
    class MapperWithoutImplementation {
    }

    @CustomGeneratorMapper(name = PROFILE_NAME)
    class MapperWithImplementation implements GeneratorMapper {

        @Override
        public List<ApiResource> mapApiResources() {
            return null;
        }

        @Override
        public List<DTOProperty> mapDtoObjects() {
            return null;
        }
    }

}
