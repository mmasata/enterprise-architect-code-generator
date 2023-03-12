openapi: 3.0.3
info:
  title: ${title}
  version: ${version}
paths:
  <#list endpoints as endpoint>
  ${endpoint.path}:
  <#if endpoint.get??>
    get:
    <@handleOperation operation=endpoint.get/>
  </#if>
  <#if endpoint.post??>
    post:
    <@handleOperation operation=endpoint.post/>
  </#if>
  <#if endpoint.put??>
    put:
    <@handleOperation operation=endpoint.put/>
  </#if>
  <#if endpoint.delete??>
    delete:
    <@handleOperation operation=endpoint.delete/>
  </#if>
  <#if endpoint.patch??>
    patch:
    <@handleOperation operation=endpoint.patch/>
  </#if>
  </#list>


<#macro handleOperation operation>
      operationId: ${operation.operationId}
      <#if operation.swaggerParameters??>
      parameters:
      <#list operation.swaggerParameters as parameter>
        - name: ${parameter.name}
          in: ${parameter.in}
          required: ${parameter.required?then('true', 'false')}
          schema:
            type: ${parameter.type}
            <#if parameter.format??>
            format: ${parameter.format}
            </#if>
      </#list>
      </#if>
      <#if operation.requestSchemaName??>
          requestBody:
            content:
              ${operation.requestContentType}:
                schema:
                  $ref: '#/components/schemas/${operation.requestSchemaName}'
      </#if>
          responses:
      <#list operation.swaggerResponses as response>
            ${response.code}:
              description: ${response.description}
              content:
                ${response.contentType}:
                  schema:
                    $ref: '#/components/schemas/${response.schemaName}'
      </#list>
</#macro>