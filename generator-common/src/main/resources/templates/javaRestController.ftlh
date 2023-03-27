<#ftl output_format="plainText">
package ${package};

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
<#list imports as import>
import ${import};
</#list>

@RestController
@RequestMapping(value = "${basePath}")
public interface ${controllerName} {

    <#list endpoints as endpoint>
      // Override this method
      @RequestMapping(method=RequestMethod.${endpoint.httpMethod}<#if endpoint.path??>, value="${endpoint.path}"</#if>)
      default ${endpoint.returnType} ${endpoint.methodName} (
<@handleRequestParams params=endpoint.params/>) {

          return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
      }

    </#list>

}

<#macro handleRequestParams params>
<#list params as param>             ${param}<#sep>,
</#list>
</#macro>