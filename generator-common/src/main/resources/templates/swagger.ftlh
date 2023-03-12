openapi: 3.0.3
info:
    title: ${title}
    version: ${version}
paths:
    <#list endpoints as endpoint>
    ${endpoint.path}:
        <#if endpoint.get??>
            get:
                operationId: ${endpoint.get.operationId}
                <#if endpoint.get.swaggerParameters??>
                parameters:
                    <#list endpoint.get.swaggerParameters as parameter>
                        -   name: ${parameter.name}
                            in: ${parameter.in}
                            required: ${parameter.required?then('true', 'false')}
                            schema:
                                type: ${parameter.type}
                                <#if parameter.format??>
                                format: ${parameter.format}
                                </#if>
                    </#list>
                </#if>
        </#if>
        <#if endpoint.post??>
            post:
                operationId: ${endpoint.post.operationId}
                <#if endpoint.post.swaggerParameters??>
                parameters:
                    <#list endpoint.post.swaggerParameters as parameter>
                    -   name: ${parameter.name}
                        in: ${parameter.in}
                        required: ${parameter.required?then('true', 'false')}
                        schema:
                        type: ${parameter.type}
                        <#if parameter.format??>
                            format: ${parameter.format}
                        </#if>
                    </#list>
                </#if>
        </#if>
        <#if endpoint.put??>
            put:
                operationId: ${endpoint.put.operationId}
                <#if endpoint.put.swaggerParameters??>
                parameters:
                    <#list endpoint.put.swaggerParameters as parameter>
                    -   name: ${parameter.name}
                        in: ${parameter.in}
                        required: ${parameter.required?then('true', 'false')}
                        schema:
                        type: ${parameter.type}
                        <#if parameter.format??>
                            format: ${parameter.format}
                        </#if>
                    </#list>
                </#if>
        </#if>
        <#if endpoint.delete??>
            delete:
                operationId: ${endpoint.delete.operationId}
                <#if endpoint.delete.swaggerParameters??>
                parameters:
                <#list endpoint.delete.swaggerParameters as parameter>
                    -   name: ${parameter.name}
                        in: ${parameter.in}
                        required: ${parameter.required?then('true', 'false')}
                        schema:
                        type: ${parameter.type}
                        <#if parameter.format??>
                            format: ${parameter.format}
                        </#if>
                    </#list>
                </#if>
        </#if>
        <#if endpoint.patch??>
            patch:
                operationId: ${endpoint.patch.operationId}
                <#if endpoint.patch.swaggerParameters??>
                parameters:
                    <#list endpoint.patch.swaggerParameters as parameter>
                    -   name: ${parameter.name}
                        in: ${parameter.in}
                        required: ${parameter.required?then('true', 'false')}
                        schema:
                        type: ${parameter.type}
                        <#if parameter.format??>
                            format: ${parameter.format}
                        </#if>
                    </#list>
                </#if>
        </#if>
    </#list>