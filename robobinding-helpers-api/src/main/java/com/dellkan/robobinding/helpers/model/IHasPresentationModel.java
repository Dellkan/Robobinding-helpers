package com.dellkan.robobinding.helpers.model;

import com.dellkan.robobinding.helpers.modelgen.AddToData;

import java.util.Map;
import java.util.UUID;

/**
 * <p>Helpers for {@link com.dellkan.robobinding.helpers.modelgen.PresentationModel PresentationModel}.
 * By modifying directly without using the generated get/setters, we lose the update functionality robobinding uses
 * to notify itself of changes on the model. Hence, these methods can be used to run that update.</p>
 * {@link PresentationModelWrapper} implements this so you don't have to. Extend {@link PresentationModelWrapper} to get these for free.
 */
public interface IHasPresentationModel extends IHasValidation {
    /**
     * Will fire onPropertyChange for all fields that has {@link com.dellkan.robobinding.helpers.modelgen.GetSet GetSet}
     */
    public void refresh();
    /**
     * Will fire onPropertyChange for a single named field. The field must be marked with {@link com.dellkan.robobinding.helpers.modelgen.GetSet GetSet} to be detected
     * @param field Field to refresh
     */
    public void refresh(String... field);

    /**
     * Collects data from every method or field marked with the {@link AddToData AddToData} annotation
     * @return map of data
     */
    public Map<String, Object> getData();

    /**
     * Collects data from every method or field marked with the {@link AddToData AddToData} annotation with the given group
     * @param group The group to search for. Only data marked with this group will be returned
     * @return map of data
     */
    public Map<String, Object> getData(String group);
}
