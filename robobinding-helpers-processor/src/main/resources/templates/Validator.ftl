<#list validators as item>

    private ValidationProcessor ${item.name}ErrorProcessor;

    @DependsOnStateOf({"${item.name}"})
    public boolean is${item.name?cap_first}Valid() {
        if (this.${item.name}ErrorProcessor == null) {
            try {
                Annotation annotation = this.data.getClass().getDeclaredField("${item.name}").getAnnotation(${item.annotationType}.class);
                this.${item.name}ErrorProcessor = new ${item.processorType}(annotation);
            } catch (NoSuchFieldException e) {
               e.printStackTrace();
            }
        }
        if (this.${item.name}ErrorProcessor != null) {
            return this.${item.name}ErrorProcessor.isValid(this.data.${item.name});
        }
        return false;
    }

    @DependsOnStateOf({"${item.name}"})
    public int get${item.name?cap_first}Error() {
        if (this.${item.name}ErrorProcessor == null) {
            try {
                Annotation annotation = this.data.getClass().getDeclaredField("${item.name}").getAnnotation(${item.annotationType}.class);
                this.${item.name}ErrorProcessor = new ${item.processorType}(annotation);
            } catch (NoSuchFieldException e) {
               e.printStackTrace();
            }
        }
        if (this.${item.name}ErrorProcessor != null) {
            return this.${item.name}ErrorProcessor.getError(this.data.${item.name});
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

    <#list validators>@DependsOnStateOf({<#items as item>"${item.name}"<#sep>, </#items>})</#list>
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