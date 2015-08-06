
    @ItemPresentationModel(${item.type}$$ItemHelper.class)
    public List<${item.type}> get${item.name?cap_first}() {
        return this.data.${item.name}.getItems();
    }

    <#assign methodName>${item.name}Selected</#assign>
    <#if !item.methodExists(methodName)>
    public void ${item.name}Selected(ItemClickEvent event) {
        this.data.${item.name}.setSelectedItem(get${item.name?cap_first}().get(event.getPosition()));
        this.changeHandler.firePropertyChange("${item.name}Selected");
    }
    </#if>

    <#assign methodName>${item.name}Clicked</#assign>
    <#assign onClickMethod>on${item.name?cap_first}Click</#assign>
    <#if !item.methodExists(methodName)>
    public void ${item.name}Clicked(ItemClickEvent event) {
        <#if item.methodExists(onClickMethod)>
        ${onClickMethod}(get${item.name?cap_first}().get(event.getPosition()));
        </#if>
    }
    </#if>

    <#assign methodName>get${item.name?cap_first}Selected</#assign>
    <#if !item.methodExists(methodName)>
    public ${item.type} get${item.name?cap_first}Selected() {
        return this.data.${item.name}.getSelectedItem();
    }
    </#if>
