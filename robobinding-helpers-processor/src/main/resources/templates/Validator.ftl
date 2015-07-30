package templates;

<#list validators as item>

    @DependsOnStateOf({"${item.name}"})
    public boolean is${item.name?cap_first}Valid() {
        return ${item.evaluationExpression};
    }

    @DependsOnStateOf({"${item.name}"})
    public int get${item.name?cap_first}Error() {
        if (!this.is${item.name?cap_first}Valid()) {
            return ${item.errorResource};
        }
        return 0;
    }
</#list>

    public boolean isValid(String field) {
        switch(field.toLowerCase()) {
            <#list validators as item>
            case "${item.name}?lower_case":
                return is${item.name?cap_first}Valid();
            </#list>
            default:
                return false;
        }
    }

    public boolean isValid() {
        return <#list validators as item>is${item.name?cap_first}Valid()<#sep> &&
            <#else>true</#list>;
    }

    public int getError(String field) {
        switch(field.toLowerCase()) {
            <#list validators as item>
            case "${item.name}?lower_case":
                return get${item.name?cap_first}Error();
            </#list>
            default:
                return 0;
        }
    }

    public java.util.Map<String, Integer> getErrors() {
        java.util.Map<String, Integer> errors = new java.util.HashMap<>();
        <#list validators as item>
            if (!is${item.name?cap_first}Valid()) {
                errors.put("${item.name}", get${item.name?cap_first}Error());
            }
        </#list>
        return errors;
    }