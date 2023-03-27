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
components:
  schemas:
  <#list schemas as schema>
    <@handleSchema schema=schema indent=4/>
  </#list>


<#macro handleSchema schema indent>
    <#local padding = ""?left_pad(indent)/>
${padding}${schema.name}:
<#if schema.isArray()>
${padding}  type: array
${padding}  items:
${padding}    type: ${schema.type}
${padding}    <#if schema.format??>format: ${schema.format}</#if>
    <#local padding = ""?left_pad(indent + 2)/>
    <#if schema.required??>
${padding}  required:
        <#list schema.required as req>
${padding}    - ${req}
        </#list>
    </#if>
    <#if schema.childs??>
${padding}  properties:
        <#list schema.childs as child>
            <@handleSchema schema=child indent= indent + 6/>
        </#list>
    </#if>
<#else>
${padding}  type: ${schema.type}
<#if schema.format??>${padding}  format: ${schema.format}</#if>
<#if schema.required??>${padding}  required:
        <#list schema.required as req>
${padding}    - ${req}
        </#list>
      </#if>
      <#if schema.childs??>
${padding}  properties:
        <#list schema.childs as child>
            <@handleSchema schema=child indent= indent + 4/>
        </#list>
      </#if>
</#if>
</#macro>


<#macro handleOperation operation>
      operationId: ${operation.operationId}
      <#if operation.description??>
      description: ${operation.description}
      </#if>
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
          application/json:
            <#if operation.isRequestSchemaArray()>
            schema:
              type: array
              items:
                $ref: '#/components/schemas/${operation.requestSchemaName}'
            <#else>
            schema:
              $ref: '#/components/schemas/${operation.requestSchemaName}'
            </#if>
      </#if>
      responses:
      <#list operation.swaggerResponses as response>
        ${response.code}:
          description: ${response.description}
          content:
            application/json:
              <#if response.isArray()>
              schema:
                type: array
                items:
                  $ref: '#/components/schemas/${response.schemaName}'
              <#else>
              schema:
                $ref: '#/components/schemas/${response.schemaName}'
              </#if>
      </#list>
</#macro>