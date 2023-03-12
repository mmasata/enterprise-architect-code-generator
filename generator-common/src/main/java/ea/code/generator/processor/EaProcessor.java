package ea.code.generator.processor;

import ea.code.generator.context.GeneratorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Collections;


@Slf4j
@Component
@RequiredArgsConstructor
@Scope("singleton")
public class EaProcessor {

    private final GeneratorContext generatorContext;

    public void run() {

        log.trace("Running EAProcessor.");
        //TODO zpracuje config a zacne prekladat do common-api
        //TODO doplni do contextu List<ApiResource>, Kafku atd...
        generatorContext.setApiResources(Collections.emptyList());
    }

}
