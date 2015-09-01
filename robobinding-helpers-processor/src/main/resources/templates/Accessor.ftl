<#if item.getter>
    <#if item.twoState>
    <#assign methodName>is${item.name?cap_first}Active</#assign>
    <#if !item.methodExists(methodName)>
    <#list item.dependsOn>@DependsOnStateOf({<#items as dependency>"${dependency}"<#sep>,</#items>})</#list>
    public boolean ${methodName}() {
        return (Boolean)${item.accessor} != null ? ${item.accessor} : false;
    }
    </#if>

    <#assign methodName>is${item.name?cap_first}Inactive</#assign>
    <#if !item.methodExists(methodName)>
    <#list item.dependsOn>@DependsOnStateOf({<#items as dependency>"${dependency}"<#sep>,</#items>})</#list>
    public boolean ${methodName}() {
        return (Boolean)${item.accessor} != null ? !${item.accessor} : true;
    }
    </#if>
    </#if>
    <#assign methodName><#if item.boolean>is<#else>get</#if>${item.name?cap_first}</#assign>
    <#if !item.methodExists(methodName)>
    <#list item.dependsOn>@DependsOnStateOf({<#items as dependency>"${dependency}"<#sep>,</#items>})</#list>
    public <#if item.forceString>String<#elseif item.boolean>boolean<#else>${item.type}</#if> ${methodName}() {
        <#if item.numeric && item.forceString>
        ${item.type} value = ${item.accessor};
        if (value != null) {
            return "" + value;
        }
        return null;
        <#else>
        return ${item.accessor};
        </#if>
    }
    </#if>
</#if>
<#if item.setter>
    <#if item.twoState>
    <#assign methodName>set${item.name?cap_first}Active</#assign>
    <#if !item.methodExists(methodName)>
    public void ${methodName}(boolean toggle) {
        ${item.accessor} = toggle;
        this.changeHandler.firePropertyChange("${item.name}");
        this.changeHandler.firePropertyChange("${item.name}Active");
        this.changeHandler.firePropertyChange("${item.name}Inactive");
    }
    </#if>

    <#assign methodName>set${item.name?cap_first}Inactive</#assign>
    <#if !item.methodExists(methodName)>
    public void ${methodName}(boolean toggle) {
        ${item.accessor} = !toggle;
        this.changeHandler.firePropertyChange("${item.name}");
        this.changeHandler.firePropertyChange("${item.name}Active");
        this.changeHandler.firePropertyChange("${item.name}Inactive");
    }
    </#if>
    </#if>
    <#assign methodName>set${item.name?cap_first}</#assign>
    <#if !item.methodExists(methodName)>
    public void ${methodName}(<#if item.forceString>String<#elseif item.boolean>boolean<#else>${item.type}</#if> value) {
        <#if item.numeric && item.forceString>
        try {
            ${item.accessor} = ${item.type}.valueOf(value);
        } catch (NumberFormatException e) {

        }
        <#else>
        ${item.accessor} = value;
        </#if>
        this.changeHandler.firePropertyChange("${item.name}");
    }
    </#if>
</#if>
