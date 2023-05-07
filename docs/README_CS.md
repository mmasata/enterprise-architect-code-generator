# Česká verze dokumentace

## Základní informace
Tato aplikace slouží k generování kódu z Enterprise Architect modelů. Je plně konfigurovatelné, lze si dopsat vlastní mappery a vlastní generovací profily.
Aplikace již obsahuje generátory pro Java (SpringBoot), Swagger a Avro schema. Je navržena jako SpringBoot starter aplikace.


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

<strong>Uživatel si vždy musí napsat svůj vlastní mapper!</strong>

## Požadavky na použití
* Java verze 17 nebo vyšší
* SpringBoot verze 3 nebo vyšší
* Maven

## Komponenty

### Common-api
Common-api je model, který slouží jako mezivrstva mezi EA modely a profily generátoru. Jeho účel je, aby profil generátoru byl nezávislý na pravidlech modelování v EA.

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