
    @org.robobinding.annotation.ItemPresentationModel(${item.type}$$ItemHelper.class)
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

    <#assign methodName>get${item.name?cap_first}SelectedPosition</#assign>
    <#if !item.methodExists(methodName)>
    public int ${methodName}() {
        return this.data.${item.name}.getSelectedPosition();
    }
    </#if>

    <#assign methodName>set${item.name?cap_first}SelectedPosition</#assign>
    <#if !item.methodExists(methodName)>
    public void ${methodName}(int position) {
        this.data.${item.name}.setSelectedPosition(position);
        this.changeHandler.firePropertyChange("${item.name}");
        this.changeHandler.firePropertyChange("${item.name}Selected");
        this.changeHandler.firePropertyChange("${item.name}SelectedPosition");
    }
    </#if>
