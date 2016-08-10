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
    public Map<String, Object> getData(String group) {
        Map<String, Object> data = new HashMap<String, Object>();

        Map<String, Object> parent = data;
        Map<String, Object> child = data;

        switch (group) {
            <#list descriptor.dataGroups as group>
            case "${group}":
                <#list dataItems as item><#if item.inGroup(group)>
                // Field ${item.dataName}
                child = data;
                <#list item.path as path><#if path?has_content>
                // Path ${path}
                parent = child;
                if (!parent.containsKey("${path}")) {
                    child = new HashMap<String, Object>();
                    parent.put("${path}", child);
                } else {
                    child = (Map<String, Object>) parent.get("${path}");
                }
                </#if>
                </#list>
                <#if item.conditional>
                if (${item.conditionalMethod}()) {
                    child.put("${item.dataName}", ${item.dataAccessor});
                }<#else>
                child.put("${item.dataName}", ${item.dataAccessor});

                </#if></#if></#list><#sep>break;
            </#list>
        }
        return data;
    }

    public Map<String, Object> getData() {
        return getData("");
    }
