<#if item.getter>
    <#if item.twoState>
    <#assign methodName>is${item.name?cap_first}Active</#assign>
    <#if !item.methodExists(methodName)>
    <#list item.dependsOn>@DependsOnStateOf({<#items as dependency>"${dependency}"<#sep>,</#items>})</#list>
    public boolean ${methodName}() {
        return (Boolean)this.data.${item.name} != null ? this.data.${item.name} : false;
    }
    </#if>

    <#assign methodName>is${item.name?cap_first}Inactive</#assign>
    <#if !item.methodExists(methodName)>
    <#list item.dependsOn>@DependsOnStateOf({<#items as dependency>"${dependency}"<#sep>,</#items>})</#list>
    public boolean ${methodName}() {
        return (Boolean)this.data.${item.name} != null ? !this.data.${item.name} : true;
    }
    </#if>
    </#if>
    <#assign methodName><#if item.boolean>is<#else>get</#if>${item.name?cap_first}</#assign>
    <#if !item.methodExists(methodName)>
    <#list item.dependsOn>@DependsOnStateOf({<#items as dependency>"${dependency}"<#sep>,</#items>})</#list>
    public <#if item.forceString>String<#elseif item.boolean>boolean<#else>${item.type}</#if> ${methodName}() {
        <#if item.numeric && item.forceString>
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
    <#if item.twoState>
    <#assign methodName>set${item.name?cap_first}Active</#assign>
    <#if !item.methodExists(methodName)>
    public void ${methodName}(boolean toggle) {
        this.data.${item.name} = toggle;
        this.changeHandler.firePropertyChange("${item.name}");
        this.changeHandler.firePropertyChange("${item.name}Active");
        this.changeHandler.firePropertyChange("${item.name}Inactive");
    }
    </#if>

    <#assign methodName>set${item.name?cap_first}Inactive</#assign>
    <#if !item.methodExists(methodName)>
    public void ${methodName}(boolean toggle) {
        this.data.${item.name} = !toggle;
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
