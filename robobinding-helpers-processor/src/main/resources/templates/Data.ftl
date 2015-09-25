<#list dataItems as item>
    <#if item.listContainer>
    public List<Map<String, Object>> getListContainerData${item.name}() {
        List<Map<String, Object>> data = new java.util.ArrayList<Map<String, Object>>();
        for (com.dellkan.robobinding.helpers.model.PresentationModelWrapper item : ${item.listAccessor}.getItems()) {
            data.add(item.getData());
        }
        return data;
    }
    </#if>
</#list>
    public Map<String, Object> getData() {
        Map<String, Object> data = new HashMap<String, Object>();
        <#list dataItems as item>
        <#if item.conditional>
        if (${item.conditionalMethod}()) {
            data.put("${item.dataName}", ${item.dataAccessor}());
        }<#else>
        data.put("${item.dataName}", ${item.dataAccessor}());
        </#if></#list>
        return data;
    }
