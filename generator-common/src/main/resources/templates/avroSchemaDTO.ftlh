<@handleSchema schema=root indent=0/>

<#macro handleSchema schema indent>
<#local padding = ""?left_pad(indent)/>
${padding}{
${padding}  "type": "record",
${padding}  "name": "${schema.name}",
<#if schema.namespace??>${padding}  "namespace": "${schema.namespace}",</#if>
${padding}  "fields": [
<#list schema.fields as key, childSchema>
${padding}    {
${padding}      "name": "${key}",
  <#if (childSchema.type == "enum")>
${padding}      "type": "${childSchema.type}",
${padding}      "symbols": [<#list childSchema.enumValues as enumValue>"${enumValue}"<#sep>,</#list>]
  <#elseif (childSchema.type != "record")>
    <#if childSchema.isArray()>
${padding}      "type": {
${padding}      "type": "array",
${padding}      "items": "${childSchema.type}"
${padding}      }
    <#else>
${padding}      "type": "${childSchema.type}"
    </#if>
  <#else>
    <#if childSchema.isArray()>
${padding}      "type": {
${padding}        "type": "array",
${padding}        "items": <@handleSchema schema=childSchema indent= indent + 6/>
${padding}        }
    <#else>
${padding}      "type": <@handleSchema schema=childSchema indent= indent + 6/>
    </#if>
  </#if>
${padding}    }<#sep>,
</#list>

${padding}  ]
${padding}}
</#macro>
