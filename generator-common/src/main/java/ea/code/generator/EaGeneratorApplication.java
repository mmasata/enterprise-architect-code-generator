package ea.code.generator;

import ea.code.generator.processor.FrameworkProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EaGeneratorApplication {
    public static void run(Class mainClass, String[] args) {

        var springContext = SpringApplication.run(mainClass, args);
        var frameworkProcessor = (FrameworkProcessor) springContext.getBean("frameworkProcessor");

        frameworkProcessor.run();
    }

}
