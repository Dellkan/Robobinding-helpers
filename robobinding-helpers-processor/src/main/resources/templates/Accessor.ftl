<#if item.getter>
    <#assign methodName><#if item.boolean>is<#else>get</#if>${item.name?cap_first}</#assign>
    <#if !item.methodExists(methodName)>
    <#list item.dependsOn>@DependsOnStateOf({<#items as dependency>"${dependency}"<#sep>,</#items>})</#list>
    public <#if item.boolean>boolean<#else>String</#if> ${methodName}() {
        <#if item.numeric>
        ${item.type} value = this.data.${item.name};
        if (value != null) {
            return "" + value;
        }
        return null;
        <#else>
        return this.data.${item.name};
        </#if>
    }
    </#if>
</#if>
<#if item.setter>
    <#assign methodName>set${item.name?cap_first}</#assign>
    <#if !item.methodExists(methodName)>
    public void ${methodName}(<#if item.boolean>boolean value<#else>String value</#if>) {
        <#if item.numeric>
        try {
            this.data.${item.name} = ${item.type}.valueOf(value);
        } catch (NumberFormatException e) {

        }
        <#else>
        this.data.${item.name} = value;
        </#if>
        this.changeHandler.firePropertyChange("${item.name}");
    }
    </#if>
</#if>
