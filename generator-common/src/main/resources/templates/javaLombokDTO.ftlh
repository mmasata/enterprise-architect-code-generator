<#ftl output_format="plainText">
package ${package};

import lombok.Data;
<#list imports as import>
import ${import};
</#list>

@Data
public class ${className} {
<@handleDtoParams params=javaParams/>;
}

<#macro handleDtoParams params>
<#list params as param>    <#if param.isNullable()>
    @Nullable</#if>
    private ${param.type} ${param.name}<#sep>,
    </#list>
</#macro>