package ea.code.generator;

import ea.code.generator.annotations.GenerateCode;
import ea.code.generator.annotations.RunGenerator;
import ea.code.generator.context.GeneratorContext;
import ea.code.generator.processor.FileProcessor;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@GenerateCode(name = "test")
@RequiredArgsConstructor
public class MyCustomGenerator {

    private final GeneratorContext generatorContext;

    private final FileProcessor fileProcessor;

    @RunGenerator
    public void run() {
        System.out.println("---------- Test generator");
        System.out.println(generatorContext.getConfiguration().getCompany());

        var data = fileProcessor.processFreemarkerTemplate("test.ftlh", Map.of("test", "myCoolCustomValue"));
        fileProcessor.generate(Map.of("myCoolCustomGenerator.txt", data));
    }

}
