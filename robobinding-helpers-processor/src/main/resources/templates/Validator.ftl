<#list validators as item>

    private ValidationProcessor ${item.name?uncap_first}ErrorProcessor;

    <#assign methodName>is${item.name}Valid</#assign>
    <#if !item.methodExists(methodName)>
    @DependsOnStateOf({<#list item.dependsOn as dependency>"${dependency}"<#sep>, </#list>})
    public boolean ${methodName}() {
        if (this.${item.name?uncap_first}ErrorProcessor == null) {
            try {
                Annotation annotation = ${item.accessorClass}.getClass().getDeclaredField("${item.fieldName}").getAnnotation(${item.annotationType}.class);
                this.${item.name?uncap_first}ErrorProcessor = new ${item.processorType}(annotation);
            } catch (NoSuchFieldException e) {
               e.printStackTrace();
            }
        }
        if (this.${item.name?uncap_first}ErrorProcessor != null) {
            boolean skipCheck = false;
            <#if item.hasValidateIf>
            skipCheck = !${item.validateIf}();
            </#if>
            <#if item.hasValidateIfValue>
            <#if item.numeric>
            skipCheck = ((Number) ${item.accessor}) == null || ((Number) ${item.accessor}).doubleValue() == 0D;
            <#elseif item.boolean>
            skipCheck = ((${item.type}) ${item.accessor}) == null;
            <#elseif item.string>
            skipCheck = ${item.accessor} == null || ${item.accessor}.trim().isEmpty() || ${item.accessor}.equalsIgnoreCase("0");
            <#else>
            skipCheck = ${item.accessor} == null;
            </#if>
            </#if>
            return skipCheck || this.${item.name?uncap_first}ErrorProcessor.isValid(${item.accessor});
        }
        return false;
    }
    </#if>

    <#assign methodName>is${item.name}Invalid</#assign>
    <#if !item.methodExists(methodName)>
    @DependsOnStateOf({<#list item.dependsOn as dependency>"${dependency}"<#sep>, </#list>})
    public boolean ${methodName}() {
        return !is${item.name}Valid();
    }
    </#if>

    <#assign methodName>get${item.name}Error</#assign>
    <#if !item.methodExists(methodName)>
    @DependsOnStateOf({<#list item.dependsOn as dependency>"${dependency}"<#sep>, </#list>})
    public int ${methodName}() {
        if (this.${item.name?uncap_first}ErrorProcessor == null) {
            try {
                Annotation annotation = ${item.accessorClass}.getClass().getDeclaredField("${item.fieldName}").getAnnotation(${item.annotationType}.class);
                this.${item.name?uncap_first}ErrorProcessor = new ${item.processorType}(annotation);
            } catch (NoSuchFieldException e) {
               e.printStackTrace();
            }
        }
        if (this.${item.name?uncap_first}ErrorProcessor != null) {
            if (is${item.name}Invalid()) {
                return this.${item.name?uncap_first}ErrorProcessor.getError(${item.accessor});
            }
        }
        return 0;
    }
    </#if>
</#list>

    public boolean isValid(String field) {
        switch(field.toLowerCase()) {
            <#list validators as item>
            case "${item.name?lower_case}":
                return is${item.name}Valid();
            </#list>
            default:
                return false;
        }
    }

    <#list validators>@DependsOnStateOf({<#items as item><#list item.dependsOn as dependency>"${dependency}"<#sep>, </#list><#sep>, </#items>})</#list>
    public boolean isValid() {
        return <#list validators as item>
            <#if item.hasValidateIf>
            ((!${item.validateIf}()) || (${item.validateIf}() && is${item.name}Valid()))
            <#else>is${item.name}Valid()
            </#if><#sep> &&
            <#else>true
        </#list>;
    }

    public int getError(String field) {
        switch(field.toLowerCase()) {
            <#list validators as item>
            case "${item.name?lower_case}":
                return get${item.name}Error();
            </#list>
            default:
                return 0;
        }
    }

    public java.util.Map<String, Integer> getErrors() {
        java.util.Map<String, Integer> errors = new java.util.HashMap<>();
        <#list validators as item>
            if (!is${item.name}Valid()) {
                errors.put("${item.name}", get${item.name}Error());
            }
        </#list>
        return errors;
    }