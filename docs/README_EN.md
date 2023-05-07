# English version of documentation

## Basic information
This application is used to generate code from Enterprise Architect models. It is fully configurable, you can write your own mappers and custom generation profiles.
The application already includes generators for Java (SpringBoot), Swagger and Avro schema. It is designed as a SpringBoot starter application.

```mermaid
sequenceDiagram
    actor A as User
    participant B as Mapper
    participant C as Common-Api
    participant D as Java-Generator
    participant E as Swagger-Generator
    A->>B: runApp()
    B->>B: readFromEaDb()
    B->>C: map()
    C->>D: generate()
    C->>E: generate()
```

<strong>The user must always write their own mapper!</strong>

## Application requirements
* Java version 17 or higher
* SpringBoot version 3 or higher
* Maven

## Components

### Common-api
Common-api is a model that serves as an intermediate layer between EA models and generator profiles. Its purpose is to make the generator profile independent of the EA modeling rules.
```mermaid
classDiagram
    class CommonApi{
    CommonApi : +ApiResource[] apiResources
    CommonApi : +DTOProperty[] dtoObjects
    }
    
    class ApiResource{
    ApiResource : +ApiEndpoint[] apiEndpoints
    ApiResource : +String path
    ApiResource : +String name
    }
   
    class ApiEndpoint{
    ApiEndpoint : +String path
    ApiEndpoint : +String name
    ApiEndpoint : +String description
    ApiEndpoint : +HttpMethod method
    ApiEndpoint : +HttpMessage request
    ApiEndpoint : +Map(HttpStatus, HttpMessage) responses
    ApiEndpoint : +Parameter[] httpHeaders
    ApiEndpoint : +Parameter[] pathParams
    ApiEndpoint : +Parameter[] queryParams
    }
    
    class Parameter{
    Parameter : +String name
    Parameter : +String example
    Parameter : +boolean required
    Parameter : +DataType dataType
    }
    
    class HttpMessage{
    HttpMessage : +DTOPropertyWrapper property
    HttpMessage : +HttpMessageType httpMessageType
    }
    
    class DTOPropertyWrapper{
    DTOPropertyWrapper : +DTOProperty property
    DTOPropertyWrapper : +boolean required
    DTOPropertyWrapper : +boolean isArray
    }
    
    class DTOProperty{
    DTOProperty : +String name
    DTOProperty : +DataType dataType
    DTOProperty : +String[] enumValues
    DTOProperty : +Map(String, DTOPropertyWrapper) childProperties
    }
    
    class DataType{
    <<enumeration>>
    INTEGER
    LONG
    FLOAT
    DOUBLE
    STRING
    BOOLEAN
    DATE
    DATETIME
    BIG_DECIMAL
    OBJECT
    ENUM
    UNDEFINED
    }
    
    class HttpMethod{
    <<enumeration>>
    GET
    POST
    PUT
    DELETE
    PATCH
    OPTIONS
    }
    
    class HttpMessageType{
    <<enumeration>>
    REQUEST
    RESPONSE
    }
    
    
    CommonApi-->ApiResource
    CommonApi-->DTOProperty
    ApiResource-->ApiEndpoint
    ApiEndpoint-->HttpMessage
    ApiEndpoint-->HttpMethod
    ApiEndpoint-->Parameter
    Parameter-->DataType
    HttpMessage-->HttpMessageType
    DTOProperty-->DataType
    DTOProperty-->DTOPropertyWrapper
    DTOPropertyWrapper-->DTOProperty
```

### Mapper
The mapper is a component that handles the processing of data from the EA database into a standardized common-api. The class must be annotated and implement a MapperHandler.
```java
import com.mmasata.eagenerator.MapperHandler;
import com.mmasata.eagenerator.annotations.Mapper;

@Mapper(name = "my-mapper")
public class MyMapper implements MapperHandler {

    @Override
    public List<ApiResource> mapApiResources() {
        //implement mapping to common-api ApiResources here
    }

    @Override
    public List<DTOProperty> mapDtoObjects() {
        //implement mapping to common-api DTOProperties here
    }
}
```
Access to the database can be handled by custom implementation or by using JPA entities defined in the framework. JPA entities are located in
<strong>"com.mmasata.eagenerator.database.entity"</strong> and to use them you need to enable the <strong>@EnableGeneratorJpaEntities</strong> annotation - you can write your own JPA repository.
```java
@SpringBootApplication
@EnableGeneratorJpaEntities
public class MyApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }

}
```

### Generators
The generator is the component that handles the processing of the common-api into the resulting files. The class must be annotated and
implement a GeneratorHandler.
There is also a Beana FileProcessor that takes care of writing to files and working with freemarker templates.
```java
import com.mmasata.eagenerator.GeneratorHandler;
import com.mmasata.eagenerator.annotations.Generator;
import com.mmasata.eagenerator.processor.FileProcessor;
import lombok.RequiredArgsConstructor;

import java.io.FileWriter;
import java.io.StringWriter;
import java.util.Map;

@Generator(name = "my-generator")
@RequiredArgsConstructor
public class MyGenerator implements GeneratorHandler {

    private final FileProcessor fileProcessor;

    @Override

    public void run() {
        //implement generator logic here
        Writer fileData = new StringWriter();
        fileProcessor.generate("myFile.txt", fileData);
    }

}
```