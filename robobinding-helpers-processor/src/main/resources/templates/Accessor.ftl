<#if item.getter>
    <#if item.twoState>
    <#assign methodName>is${item.name}Active</#assign>
    <#if !item.methodExists(methodName)>
    @DependsOnStateOf({"${item.name?uncap_first}"<#list item.dependsOn as dependency>, "${dependency}"</#list>})
    public boolean ${methodName}() {
        return (Boolean)${item.accessor} != null ? ${item.accessor} : false;
    }
    </#if>

    <#assign methodName>is${item.name}Inactive</#assign>
    <#if !item.methodExists(methodName)>
    @DependsOnStateOf({"${item.name?uncap_first}"<#list item.dependsOn as dependency>, "${dependency}"</#list>})
    public boolean ${methodName}() {
        return (Boolean)${item.accessor} != null ? !${item.accessor} : true;
    }
    </#if>
    </#if>
    <#assign methodName><#if item.boolean>is<#else>get</#if>${item.name}</#assign>
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
    <#assign methodName>set${item.name}Active</#assign>
    <#if !item.methodExists(methodName)>
    public void ${methodName}(boolean toggle) {
        ${item.accessor} = toggle;
        this.changeHandler.firePropertyChange("${item.name?uncap_first}");
        this.changeHandler.firePropertyChange("${item.name?uncap_first}Active");
        this.changeHandler.firePropertyChange("${item.name?uncap_first}Inactive");
    }
    </#if>

    <#assign methodName>set${item.name}Inactive</#assign>
    <#if !item.methodExists(methodName)>
    public void ${methodName}(boolean toggle) {
        ${item.accessor} = !toggle;
        this.changeHandler.firePropertyChange("${item.name?uncap_first}");
        this.changeHandler.firePropertyChange("${item.name?uncap_first}Active");
        this.changeHandler.firePropertyChange("${item.name?uncap_first}Inactive");
    }
    </#if>
    </#if>
    <#assign methodName>set${item.name}</#assign>
    <#if !item.methodExists(methodName)>
    public void ${methodName}(<#if item.forceString>String<#elseif item.boolean>boolean<#else>${item.type}</#if> value) {
        <#if item.numeric && item.forceString>
        try {
            if (value == null || value.isEmpty()) {
                ${item.accessor} = ${item.type}.valueOf("0");
            } else {
                ${item.accessor} = ${item.type}.valueOf(value);
            }
        } catch (NumberFormatException e) {

        }
        <#else>
        ${item.accessor} = value;
        </#if>
        this.changeHandler.firePropertyChange("${item.name?uncap_first}");
    }
    </#if>
</#if>
