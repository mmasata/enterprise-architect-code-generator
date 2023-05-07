package com.mmasata.eagenerator.processor;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

/**
 * Class for working with outputs. Processes freemarker templates and fills files with data.
 */
@Slf4j
@RequiredArgsConstructor
public class FileProcessor {

    private static final String EXPORT_FOLDER = "export";
    private static final String FOLDER_SEPARATOR = "/";

    private final Configuration freemarkerConfig;

    /**
     * The class processes the freemarker template with the data for it and writes the output to the StringWriter.
     *
     * @param templateName Name of the freemarker template.
     * @param variables    Variable map for freemarker template.
     * @return Returns the processed template in StringWriter.
     */
    public StringWriter processFreemarkerTemplate(String templateName,
                                                  Map<String, Object> variables) {

        var stringWriter = new StringWriter();
        var template = getTemplate(templateName);

        try {
            template.process(variables, stringWriter);
        } catch (TemplateException | IOException e) {

            log.error("Processing template {} failed.", templateName);
            throw new RuntimeException(e);
        }

        return stringWriter;
    }

    /**
     * Generates files from the data.
     *
     * @param data A map where the key prescribes the name of the output file and the value of the data to be written to it.
     */
    public void generate(Map<String, Writer> data) {
        data.forEach(this::generate);
    }

    /**
     * Generates file from the data.
     *
     * @param fileName Name of the file
     * @param fileData Stream of data content
     */
    public void generate(String fileName,
                         Writer fileData) {

        fileName = EXPORT_FOLDER + FOLDER_SEPARATOR + fileName;

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
