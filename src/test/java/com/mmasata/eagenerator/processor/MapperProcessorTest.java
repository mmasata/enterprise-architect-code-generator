package com.mmasata.eagenerator.processor;

import com.mmasata.eagenerator.MapperHandler;
import com.mmasata.eagenerator.annotations.Mapper;
import com.mmasata.eagenerator.api.DTOProperty;
import com.mmasata.eagenerator.api.rest.ApiResource;
import com.mmasata.eagenerator.context.GeneratorContext;
import com.mmasata.eagenerator.context.model.GeneratorConfiguration;
import com.mmasata.eagenerator.context.model.MappingConfiguration;
import com.mmasata.eagenerator.context.model.enums.MappingType;
import com.mmasata.eagenerator.validator.CommonApiValidator;
import com.mmasata.eagenerator.exception.GeneratorException;
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
class MapperProcessorTest {

    private static final String PROFILE_NAME = "profile";

    @Mock
    private GeneratorContext generatorContext;

    @Mock
    private ApplicationContext applicationContext;

    @Mock
    private CommonApiValidator commonApiValidator;

    @InjectMocks
    private MapperProcessor mapperProcessor;

    @Test
    void runTest_generic_notImplementedError() {
        when(generatorContext.getConfiguration()).thenReturn(mockGeneratorConfiguration(MappingType.GENERIC, null));
        assertThrows(GeneratorException.class, () -> mapperProcessor.run());
        verify(generatorContext).getConfiguration();
        verify(commonApiValidator, never()).validate(any());
    }

    @Test
    void runTest_custom_missingMapperError() {
        when(generatorContext.getConfiguration()).thenReturn(mockGeneratorConfiguration(MappingType.CUSTOM, null));
        when(applicationContext.getBeansWithAnnotation(Mapper.class)).thenReturn(Collections.emptyMap());
        assertThrows(GeneratorException.class, () -> mapperProcessor.run());

        when(generatorContext.getConfiguration()).thenReturn(mockGeneratorConfiguration(MappingType.CUSTOM, "someProfileWhichDontExistInTestContext"));
        assertThrows(GeneratorException.class, () -> mapperProcessor.run());
        verify(commonApiValidator, never()).validate(any());
    }

    @Test
    void runTest_custom_mapperNotInstanceOfGeneratorMapperError() {
        when(generatorContext.getConfiguration()).thenReturn(mockGeneratorConfiguration(MappingType.CUSTOM, PROFILE_NAME));
        when(applicationContext.getBeansWithAnnotation(Mapper.class)).thenReturn(Map.of(PROFILE_NAME, new MapperWithoutImplementation()));
        assertThrows(GeneratorException.class, () -> mapperProcessor.run());
        verify(commonApiValidator, never()).validate(any());
    }

    @Test
    void runTest_shouldPass() {
        when(generatorContext.getConfiguration()).thenReturn(mockGeneratorConfiguration(MappingType.CUSTOM, PROFILE_NAME));
        when(applicationContext.getBeansWithAnnotation(Mapper.class)).thenReturn(Map.of(PROFILE_NAME, new MapperWithImplementationHandler()));
        assertDoesNotThrow(() -> mapperProcessor.run());
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

    @Mapper(name = PROFILE_NAME)
    class MapperWithoutImplementation {
    }

    @Mapper(name = PROFILE_NAME)
    class MapperWithImplementationHandler implements MapperHandler {

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
