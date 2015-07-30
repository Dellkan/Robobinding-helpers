package templates;

public <#if item.boolean>boolean is<#else>String get</#if>${item.name?cap_first}() {
        <#if item.numeric>
        return "" + this.data.${item.name};
        <#else>
        return this.data.${item.name};
        </#if>
    }
<#if item.setter>
    <#list item.dependsOn>@DependsOnStateOf({<#items as dependency>"${dependency}"<#sep>,</#items>})</#list>
    public void set${item.name?cap_first}(<#if item.boolean>boolean value<#else>String value</#if>) {
        <#if item.numeric>
        try {
            this.data.${item.name} = <#if item.type == "int">Integer<#else>${item.type?cap_first}</#if>.valueOf(value);
        } catch (NumberFormatException e) {

        }
        <#else>
        this.data.${item.name} = value;
        </#if>
        this.changeHandler.firePropertyChange("${item.name}");
    }
</#if>
