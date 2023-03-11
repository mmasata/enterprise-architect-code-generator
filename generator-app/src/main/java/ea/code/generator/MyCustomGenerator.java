package ea.code.generator;

import ea.code.generator.annotations.GenerateCode;
import ea.code.generator.annotations.RunGenerator;

@GenerateCode(name = "test")
public class MyCustomGenerator {

    @RunGenerator
    public void run() {
        System.out.println("Test generator");
    }

}
