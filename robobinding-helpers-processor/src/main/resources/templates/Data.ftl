
    public Map<String, Object> getData() {
        Map<String, Object> data = new HashMap<String, Object>();
        <#list dataItems as item>
        data.put("${item.dataName}", ${item.dataAccessor}());
        </#list>
        return data;
    }
