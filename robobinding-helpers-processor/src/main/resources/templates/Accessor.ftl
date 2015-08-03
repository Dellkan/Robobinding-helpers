<#if item.getter>
    public <#if item.boolean>boolean is<#else>String get</#if>${item.name?cap_first}() {
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
<#if item.setter>
    <#list item.dependsOn>@DependsOnStateOf({<#items as dependency>"${dependency}"<#sep>,</#items>})</#list>
    public void set${item.name?cap_first}(<#if item.boolean>boolean value<#else>String value</#if>) {
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
