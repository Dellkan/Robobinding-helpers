package com.dellkan.robobinding.helpers.model;

public interface IGlobalPresentationModelLookup {
    Class<? extends IHasPresentationModel> lookup(Class<? extends IHasPresentationModel> dataModelClass);
    IHasPresentationModel getPresentationModel(PresentationModelWrapper dataModel);
}
