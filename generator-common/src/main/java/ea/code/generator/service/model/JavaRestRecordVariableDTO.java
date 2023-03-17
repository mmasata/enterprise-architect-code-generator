package ea.code.generator.service.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class JavaRestRecordVariableDTO {

    private List<JavaRestRecordDTO> restControllers;
    private List<JavaRestRecordDTO> records;
}
