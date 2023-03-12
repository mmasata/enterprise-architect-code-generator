package ea.code.generator.processor;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileProcessor {

    private final Configuration freemarkerConfig;

    public StringWriter processFreemarkerTemplate(String templateName,
                                                  Map<String, String> variables) {

        var stringWriter = new StringWriter();
        var template  = getTemplate(templateName);

        try {
            template.process(variables, stringWriter);
        } catch (TemplateException | IOException e) {

            log.error("Processing template {} failed.", templateName);
            throw new RuntimeException(e);
        }

        return stringWriter;
    }

    public void generate(Map<String, StringWriter> data) {

        data.forEach((fileName, fileData) -> {

            try {
                var file = new File(fileName);
                var parent = file.getParentFile();

                if (parent != null && !parent.exists() && !parent.mkdirs()) {
                    log.error("Couldn't create directory for {}", fileName);
                    throw new IllegalStateException();
                }

                var fileWriter = new FileWriter(file);
                fileWriter.write(fileData.toString());
                fileWriter.close();
                log.info("Data was written to file {}.", fileName);
            } catch (IOException e) {
                log.error("Generating file {} failed.", fileName);
                throw new RuntimeException(e);
            }

        });
    }

    private Template getTemplate(String name) {

        try {

            return freemarkerConfig.getTemplate(name);
        } catch (IOException e) {

            log.error("Couldn't found FreeMarker template {}", name);
            throw new RuntimeException(e);
        }
    }

}
