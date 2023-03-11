package ea.code.generator.processor;

import ea.code.generator.context.GeneratorContext;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
@Slf4j
public class EaProcessor {

    private static EaProcessor INSTANCE = null;

    public static EaProcessor getInstance() {

        if(INSTANCE == null) {
            INSTANCE = new EaProcessor();
        }

        return INSTANCE;
    }

    public void run(GeneratorContext context) {

        log.trace("Running EAProcessor.");
        //TODO zpracuje config a zacne prekladat do common-api
        //TODO doplni do contextu List<ApiResource>, Kafku atd...
        context.setApiResources(Collections.emptyList());
    }

}
