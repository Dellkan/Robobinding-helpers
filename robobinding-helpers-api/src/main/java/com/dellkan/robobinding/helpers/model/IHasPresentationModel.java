package com.dellkan.robobinding.helpers.model;

/**
 * Helpers for {@link @com.dellkan.robobinding.helpers.modelgen.PresentationModel PresentationModel}.
 * By modifying directly without using the generated get/setters, we lose the update functionality robobinding uses
 * to notify itself of changes on the model. Hence, these methods can be used to run that update.<br />
 * <br />
 * {@link Wrapper} implements this so you don't have to. Extend {@link Wrapper} to get these for free.
 */
public interface IHasPresentationModel extends IHasValidation {
    /**
     * Will fire onPropertyChange for all fields that has {@link com.dellkan.robobinding.helpers.modelgen.GetSet GetSet}
     */
    public void refresh();
    /**
     * Will fire onPropertyChange for a single named field. The field must be marked with {@link com.dellkan.robobinding.helpers.modelgen.GetSet GetSet} to be detected
     */
    public void refresh(String field);
}
