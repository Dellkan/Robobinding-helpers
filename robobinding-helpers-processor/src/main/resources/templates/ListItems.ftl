
    @org.robobinding.annotation.ItemPresentationModel(${item.type}$$ItemHelper.class)
    public List<${item.type}> get${item.name}() {
        return ${item.accessor}.getItems();
    }

    <#assign methodName>${item.name?uncap_first}Selected</#assign>
    <#if !item.methodExists(methodName)>
    public void ${methodName}(ItemClickEvent event) {
        ${item.accessor}.setSelectedItem(get${item.name}().get(event.getPosition()));
        this.changeHandler.firePropertyChange("${item.name?uncap_first}Selected");
    }
    </#if>

    <#assign methodName>${item.name?uncap_first}Clicked</#assign>
    <#assign onClickMethod>on${item.name}Click</#assign>
    <#if !item.methodExists(methodName)>
    public void ${methodName}(ItemClickEvent event) {
        <#if item.methodExists(onClickMethod)>
        ${onClickMethod}(get${item.name}().get(event.getPosition()));
        </#if>
    }
    </#if>

    <#assign methodName>get${item.name?uncap_first}Selected</#assign>
    <#if !item.methodExists(methodName)>
    public ${item.type} get${item.name}Selected() {
        return ${item.accessor}.getSelectedItem();
    }
    </#if>

    <#assign methodName>get${item.name}SelectedPosition</#assign>
    <#if !item.methodExists(methodName)>
    public int ${methodName}() {
        return ${item.accessor}.getSelectedPosition();
    }
    </#if>

    <#assign methodName>set${item.name}SelectedPosition</#assign>
    <#if !item.methodExists(methodName)>
    public void ${methodName}(int position) {
        ${item.accessor}.setSelectedPosition(position);
        this.changeHandler.firePropertyChange("${item.name?uncap_first}");
        this.changeHandler.firePropertyChange("${item.name?uncap_first}Selected");
        this.changeHandler.firePropertyChange("${item.name?uncap_first}SelectedPosition");
    }
    </#if>

    <#assign methodName>is${item.name}Empty</#assign>
    <#if !item.methodExists(methodName)>
    public boolean ${methodName}() {
        return ${item.accessor}.size() == 0;
    }
    </#if>

    <#assign methodName>is${item.name}NotEmpty</#assign>
    <#if !item.methodExists(methodName)>
    public boolean ${methodName}() {
        return ${item.accessor}.size() != 0;
    }
    </#if>