<#list validators as item>

    <#if !item.methodValidation>
    private ValidationProcessor ${item.name?uncap_first}ErrorProcessor;
    </#if>

    <#if !item.methodExists(item.validationNameValid)>
    @DependsOnStateOf({<#list item.dependsOn as dependency>
        <#if item.selfDependencyNecessary(dependency)>"${dependency}"</#if><#sep>,</#list>
    })
    public boolean ${item.validationNameValid}() {
        <#if !item.methodValidation>
        if (this.${item.name?uncap_first}ErrorProcessor == null) {
            try {
                Annotation annotation = ${item.accessorClass}.getClass().getDeclaredField("${item.fieldName}").getAnnotation(${item.annotationType}.class);
                this.${item.name?uncap_first}ErrorProcessor = new ${item.processorType}(annotation);
            } catch (NoSuchFieldException e) {
               e.printStackTrace();
            }
        }
        if (this.${item.name?uncap_first}ErrorProcessor != null) {
        </#if>
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
            <#if item.methodValidation>
            return skipCheck || ${item.accessor}();
            <#else>
            return skipCheck || this.${item.name?uncap_first}ErrorProcessor.isValid(${item.accessor});
        }
        return false;
        </#if>
    }
    </#if>

    <#if !item.methodExists(item.validationNameInvalid)>
    @DependsOnStateOf({<#list item.dependsOn as dependency>"${dependency}"<#sep>, </#list>})
    public boolean ${item.validationNameInvalid}() {
        return !${item.validationNameValid}();
    }
    </#if>

    <#assign methodName>get${item.name}Error</#assign>
    <#if !item.methodExists(methodName)>
    @DependsOnStateOf({<#list item.dependsOn as dependency>"${dependency}"<#sep>, </#list>})
    public int ${methodName}() {
        <#if !item.methodValidation>
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
        <#else>
        return ${item.methodError};
        </#if>
    }
    </#if>
</#list>

    public boolean isValid(String field) {
        switch(field.toLowerCase()) {
            <#list validators as item>
            case "${item.name?lower_case}":
                return ${item.validationNameValid}();
            </#list>
            default:
                return false;
        }
    }

    <#list validators>@DependsOnStateOf({<#items as item><#list item.dependsOn as dependency>"${dependency}"<#sep>, </#list><#sep>, </#items>})</#list>
    public boolean isValid() {
        return <#list validators as item><#if item.hasValidateIf>
            ((!${item.validateIf}()) || (${item.validateIf}() && ${item.validationNameValid}()))<#else>${item.validationNameValid}()</#if><#sep> &&
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
            if (!${item.validationNameValid}()) {
                errors.put("${item.name}", get${item.name}Error());
            }
        </#list>
        return errors;
    }