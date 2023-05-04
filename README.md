# Code generator framework from Enterprise Architect


## Problems
* avro-schema - Array of Enums are not supported. In this case please use array of string or another supported avro data type
* java-spring - It is not possible to generate standard controllers and reactive ones in one artifact. Libraries are mutually exclusive and will overwrite each other's Beans.
* mapper - @RunMapper and @Transactional cannot be in the same class. When working with a database, you need to make a different class. The @Transactional creates a proxy over the class that the annotation generator can't handle.
