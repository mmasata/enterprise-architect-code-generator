package ea.code.generator.embedded.service;

import ea.code.generator.annotations.GenerateCode;
import ea.code.generator.annotations.RunGenerator;
import lombok.RequiredArgsConstructor;

@GenerateCode(name = "test")
@RequiredArgsConstructor
public class TestService {

    @RunGenerator
    public void run() {
        System.out.println("Test generator");
    }

}
