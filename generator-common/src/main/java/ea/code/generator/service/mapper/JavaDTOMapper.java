package ea.code.generator.service.mapper;

import ea.code.generator.api.DTOProperty;
import ea.code.generator.api.rest.ApiResource;
import ea.code.generator.api.rest.enums.DataType;
import ea.code.generator.context.GeneratorContext;
import ea.code.generator.service.freemarker.model.JavaParam;
import ea.code.generator.service.helper.JavaHelper;
import ea.code.generator.service.model.JavaFileDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;

import static ea.code.generator.service.constants.JavaConstants.IMPORT_LIST;
import static ea.code.generator.service.constants.JavaConstants.IMPORT_NULLABLE;
import static ea.code.generator.service.constants.JavaConstants.JAVA_DATA_TYPES_MAPPER;
import static ea.code.generator.service.constants.JavaConstants.METHOD_REACTIVE_OR_ARR_WRAPPER;
import static ea.code.generator.service.constants.JavaConstants.NON_REACTIVE_ARRAY;

@Component
@RequiredArgsConstructor
public class JavaDTOMapper implements Function<GeneratorContext, List<JavaFileDTO>> {

    private final JavaHelper javaHelper;

    @Override
    public List<JavaFileDTO> apply(GeneratorContext generatorContext) {

        var apiResources = generatorContext.getApiResources();
        var DTOObjects = new ArrayList<JavaFileDTO>();
        var createdDTOs = new HashSet<String>();

        var endpoints = apiResources.stream()
                .map(ApiResource::getEndpoints)
                .flatMap(Collection::stream)
                .toList();

        endpoints.forEach(endpoint -> {
            if (endpoint.getRequest() != null) {
                handleDTO(DTOObjects,
                        createdDTOs,
                        endpoint.getRequest().getProperty().getProperty());
            }

            endpoint.getResponses().forEach((httpStatus, httpMessage) -> handleDTO(DTOObjects,
                    createdDTOs,
                    httpMessage.getProperty().getProperty()));
        });
        return DTOObjects;
    }

    private void handleDTO(List<JavaFileDTO> DTOObjects,
                           Set<String> createdDTOs,
                           DTOProperty property) {

        if (createdDTOs.contains(property.getName())) { //redundant
            return;
        }

        createdDTOs.add(property.getName());

        var DTOObject = new JavaFileDTO();
        DTOObject.setFolder(javaHelper.getModelFolder());
        DTOObject.setFileName(property.getName());
        DTOObject
                .addVariable("package", javaHelper.getModelPackage())
                .addVariable("className", property.getName());

        var javaParams = new ArrayList<JavaParam>();
        var imports = new TreeSet<String>();
        property.getChildProperties().forEach((name, wrapper) -> {

            var childProperty = wrapper.getProperty();
            var childJavaDataType = JAVA_DATA_TYPES_MAPPER.get(childProperty.getDataType());
            var attributeDataType = childProperty.getDataType() == DataType.OBJECT
                    || childProperty.getDataType() == DataType.ENUM
                    ? childProperty.getName()
                    : childJavaDataType.getDataType();

            if (wrapper.isArray()) {
                imports.add(IMPORT_LIST);
                attributeDataType = METHOD_REACTIVE_OR_ARR_WRAPPER.formatted(NON_REACTIVE_ARRAY, attributeDataType);
            }

            if (!wrapper.isRequired()) {
                imports.add(IMPORT_NULLABLE);
            }

            if (childProperty.getDataType() == DataType.OBJECT) {
                handleDTO(DTOObjects, createdDTOs, childProperty);
            }

            if (childProperty.getDataType() == DataType.ENUM) {
                handleEnum(DTOObjects, createdDTOs, childProperty);
            }

            if (childJavaDataType != null && childJavaDataType.getImportName() != null) {
                imports.add(childJavaDataType.getImportName());
            }

            javaParams.add(new JavaParam(!wrapper.isRequired(), attributeDataType, name));
        });

        DTOObject
                .addVariable("javaParams", javaParams)
                .addVariable("imports", imports);
        DTOObjects.add(DTOObject);
    }

    private void handleEnum(List<JavaFileDTO> DTOObjects,
                            Set<String> createdDTOs,
                            DTOProperty property) {

        if (createdDTOs.contains(property.getName())) { //redundant
            return;
        }

        createdDTOs.add(property.getName());

        var DTOObject = new JavaFileDTO();
        DTOObject.setFolder(javaHelper.getModelFolder());
        DTOObject.setFileName(property.getName());
        DTOObject
                .addVariable("package", javaHelper.getModelPackage())
                .addVariable("className", property.getName())
                .addVariable("isEnum", Boolean.TRUE)
                .addVariable("enumValues", property.getEnumValues());

        DTOObjects.add(DTOObject);
    }

}
