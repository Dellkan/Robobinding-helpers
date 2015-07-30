package com.dellkan.robobinding.helpers.model;

public interface IHasPresentationModel extends IHasValidation {
    public void refresh();
    public void refresh(String field);
}
