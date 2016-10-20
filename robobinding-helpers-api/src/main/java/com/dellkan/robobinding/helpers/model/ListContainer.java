package com.dellkan.robobinding.helpers.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListContainer<T> extends ArrayList<T> implements Serializable {
    public ListContainer() {
    }

    public ListContainer(Collection<? extends T> c) {
        super(c);
    }

    public T getSelectedItem() {
        if (size() > 0 && getSelectedPosition() >= 0 && getSelectedPosition() < size()) {
            return get(getSelectedPosition());
        }
        return null;
    }

    public void setSelectedItem(T selectedItem) {
        for (int i = 0; i < size(); i++) {
            if (get(i).equals(selectedItem)) {
                setSelectedPosition(i);
                break;
            }
        }
    }

    private int selectedPosition = 0;
    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void setSelectedPosition(int position) {
        selectedPosition = position;
    }

    public List<T> getItems() {
        return this;
    }

    public void setItems(Collection<T> items) {
        clear();
        addAll(items);
    }

    public void addItem(T item) {
        this.add(item);
    }

    public void addItem(T item, int position) {
        this.add(position, item);
    }

    public T getItem(int position) {
        return this.get(position);
    }

    public void removeItem(int position) {
        this.remove(position);
    }

    public void removeItem(T item) {
        this.remove(item);
    }

    public int size() {
        return super.size();
    }
}
