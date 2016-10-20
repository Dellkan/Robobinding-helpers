package com.dellkan.robobinding.helpers.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * You should make your {@link com.dellkan.robobinding.helpers.modelgen.PresentationModel PresentationModel} extend this class,
 * so it will get all these helpers methods.
 */
public abstract class PresentationModelWrapper implements IHasPresentationModel {
    private static IGlobalPresentationModelLookup lookupTable = null;
    private Map<PresentationModelWrapper, String> attachedModels = new HashMap<>();

    public void attachModel(PresentationModelWrapper model, String prefix) {
        attachedModels.put(model, prefix);
    }

    private static IGlobalPresentationModelLookup getLookupTable() {
        if (lookupTable == null) {
            try {
                Class cls = Class.forName("com.dellkan.robobinding.helpers.GlobalPresentationModelLookup");
                lookupTable = (IGlobalPresentationModelLookup) cls.newInstance();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return lookupTable;
    }

    /**
     * A reference to the generated class
     */
    private transient IHasPresentationModel presentationModel = null;

    private void initializePresentationModel() {
        if (presentationModel == null) {
            if (getLookupTable() != null) {
                presentationModel = getLookupTable().getPresentationModel(this);
            }
        }
    }

    /**
     * Give this to the robobinding.. binder, which uses the getters/setters.
     * @return A robobinding compatible class, compiled according to your annotation usages
     */
    public IHasPresentationModel getPresentationModel() {
        initializePresentationModel();
        return presentationModel;
    }

    @Override
    public void refresh() {
        getPresentationModel().refresh();
        // Also refresh all attached models
        for (Map.Entry<PresentationModelWrapper, String> entry : attachedModels.entrySet()) {
            entry.getKey().refresh();
        }
    }

    @Override
    public void refresh(String... field) {
        getPresentationModel().refresh(field);
        // Also refresh all attached models. Some trickery necessary due to prefixes and capitalizations
        for (Map.Entry<PresentationModelWrapper, String> entry : attachedModels.entrySet()) {
            String[] fields = new String[field.length];
            for (int i = 0; i < field.length; i++) {
                if (!entry.getValue().isEmpty() && !field[i].startsWith(entry.getValue())) {
                    fields[i] = entry.getValue() + field[i].substring(0, 1).toUpperCase() + field[i].substring(1);
                } else {
                    fields[i] = field[i];
                }
            }
            entry.getKey().refresh(fields);
        }
    }

    @Override
    public boolean isValid() {
        return getPresentationModel().isValid();
    }

    @Override
    public boolean isValid(String field) {
        return getPresentationModel().isValid(field);
    }

    @Override
    public Map<String, Integer> getErrors() {
        return getPresentationModel().getErrors();
    }

    @Override
    public int getError(String field) {
        return getPresentationModel().getError(field);
    }

    @Override
    public Map<String, Object> getData() {
        return getPresentationModel().getData();
    }

    @Override
    public Map<String, Object> getData(String group) {
        return getPresentationModel().getData(group);
    }
}
