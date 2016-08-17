private Map<String, ValidationProcessor> validationProcessors = new HashMap<>();

private ValidationProcessor getValidationProcessor(String name) {
    if (!validationProcessors.containsKey(name)) {
        switch (name) {<#list validators as item><#list item.validators as validator>
            case "${validator.uniqueName}": // ${item.name}@${validator.annotationTypeSimpleName}
                try {
                    JSONObject validationConfig = new JSONObject("${validator.configValues?j_string}");
                    validationProcessors.put(name, new ${validator.processorType}(validationConfig));
                } catch(Exception e) {}
                break;</#list></#list>
        }
    }
    return validationProcessors.get(name);
}

<#list validators as item>
    <#if !item.methodExists(item.validationNameValid)>
    @DependsOnStateOf({<#list item.dependsOn as dependency>
        <#if item.selfDependencyNecessary(dependency)>"${dependency}"</#if><#sep>,</#list>
    })
    public boolean ${item.validationNameValid}() {
        boolean isValid = false;
        <#list item.validators as validator>
        // @${validator.annotationType}
        {
            try {
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
                isValid = skipCheck || ${item.accessor}();
                <#else>
                isValid = skipCheck || getValidationProcessor("${validator.uniqueName}").isValid(${validator.accessor});
                </#if>

                if (!isValid) {
                    return false;
                }
            } catch(Exception e) { }
        }
        </#list>
        return isValid;
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
        int error = 0;
        <#list item.validators as validator>
        // @${validator.annotationType}
        {
            <#if !item.methodValidation>
            try {
                error = getValidationProcessor("${validator.uniqueName}").getError(${item.accessor});
                <#else>
                error = ${item.methodError};
                </#if>

                if (error != 0) {
                    return error;
                }
            } catch(Exception e) { }
        }
        </#list>

        return 0;
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