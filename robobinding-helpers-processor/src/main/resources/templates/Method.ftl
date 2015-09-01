
    <#list item.dependsOn>@DependsOnStateOf({<#items as dependency>"${dependency}"<#sep>,</#items>})</#list>
    public ${item.type} ${item.name}(<#list item.params as param>${param.type} ${param.name}<#sep>, </#list>) {
        <#if item.type != "void">return </#if>${item.accessor}(<#list item.params as param>${param.name}<#sep>, </#list>);
    }
