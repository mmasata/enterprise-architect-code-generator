<#ftl output_format="plainText">
package ${package};

public enum ${className} {

<#list enumValues as enumValue> ${enumValue}<#sep>,
</#list>

}