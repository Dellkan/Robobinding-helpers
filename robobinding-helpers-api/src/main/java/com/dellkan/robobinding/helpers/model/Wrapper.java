package com.dellkan.robobinding.helpers.model;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * You should make your {@link com.dellkan.robobinding.helpers.modelgen.PresentationModel PresentationModel} extend this class,
 * so it will get all these helpers methods.
 */
public abstract class Wrapper implements IHasPresentationModel {
    /**
     * A reference to the generated class
     */
    private IHasPresentationModel presentationModel;

    /**
     * Give this to the robobinding.. binder, which uses the getters/setters.
     * @return A robobinding compatible class, compiled according to your annotation usages
     */
    public IHasPresentationModel getPresentationModel() {
        if (presentationModel == null) {
            Class<?> clazz;
            try {
                clazz = Class.forName(this.getClass().getName() + "$$Helper");
                Constructor<?> constructor = clazz.getConstructor(this.getClass());
                presentationModel = (IHasPresentationModel) constructor.newInstance(this);
            } catch (ClassNotFoundException e) {
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return presentationModel;
    }

    @Override
    public void refresh() {
        getPresentationModel().refresh();
    }

    @Override
    public void refresh(String field) {
        getPresentationModel().refresh(field);
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
}
