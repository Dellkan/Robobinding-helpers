
    @org.robobinding.annotation.ItemPresentationModel(${item.type}$$ItemHelper.class)
    public List<${item.type}> get${item.name?cap_first}() {
        return ${item.accessor}.getItems();
    }

    <#assign methodName>${item.name}Selected</#assign>
    <#if !item.methodExists(methodName)>
    public void ${item.name}Selected(ItemClickEvent event) {
        ${item.accessor}.setSelectedItem(get${item.name?cap_first}().get(event.getPosition()));
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
        return ${item.accessor}.getSelectedItem();
    }
    </#if>

    <#assign methodName>get${item.name?cap_first}SelectedPosition</#assign>
    <#if !item.methodExists(methodName)>
    public int ${methodName}() {
        return ${item.accessor}.getSelectedPosition();
    }
    </#if>

    <#assign methodName>set${item.name?cap_first}SelectedPosition</#assign>
    <#if !item.methodExists(methodName)>
    public void ${methodName}(int position) {
        ${item.accessor}.setSelectedPosition(position);
        this.changeHandler.firePropertyChange("${item.name}");
        this.changeHandler.firePropertyChange("${item.name}Selected");
        this.changeHandler.firePropertyChange("${item.name}SelectedPosition");
    }
    </#if>
