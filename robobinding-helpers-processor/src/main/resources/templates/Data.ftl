<#list dataItems as item>
    <#if item.listContainer>
    public List<Map<String, Object>> getListContainerData${item.name?cap_first}() {
        List<Map<String, Object>> data = new java.util.ArrayList<Map<String, Object>>();
        for (com.dellkan.robobinding.helpers.model.PresentationModelWrapper item : this.data.${item.name}.getItems()) {
            data.add(item.getData());
        }
        return data;
    }
    </#if>
</#list>
    public Map<String, Object> getData() {
        Map<String, Object> data = new HashMap<String, Object>();
        <#list dataItems as item>
        data.put("${item.dataName}", ${item.dataAccessor}());
        </#list>
        return data;
    }
