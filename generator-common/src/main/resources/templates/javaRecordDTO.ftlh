<#ftl output_format="plainText">
package ${package};

<#list imports as import>
import ${import};
</#list>

public record ${recordName}(
<@handleRecordParams params=recordParams/>){}

<#macro handleRecordParams params>
<#list params as param>         ${param.type} ${param.name}<#sep>,
</#list>
</#macro>