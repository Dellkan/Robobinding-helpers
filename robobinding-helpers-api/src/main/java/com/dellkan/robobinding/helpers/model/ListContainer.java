package com.dellkan.robobinding.helpers.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ListContainer<T> implements Serializable {
    private List<T> items = new ArrayList<>();

    public T getSelectedItem() {
        if (items.size() > 0 && getSelectedPosition() >= 0 && getSelectedPosition() < items.size()) {
            return items.get(getSelectedPosition());
        }
        return null;
    }

    public void setSelectedItem(T selectedItem) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).equals(selectedItem)) {
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
        return items;
    }

    public void setItems(Collection<T> items) {
        this.items.clear();
        this.items.addAll(items);
    }

    public void addItem(T item) {
        this.items.add(item);
    }

    public void addItem(T item, int position) {
        this.items.add(position, item);
    }

    public T getItem(int position) {
        return this.items.get(position);
    }

    public void removeItem(int position) {
        this.items.remove(position);
    }

    public void removeItem(T item) {
        this.items.remove(item);
    }

    public int size() {
        return items.size();
    }

    public boolean contains(T item) {
        for (T mItem : items) {
            if (mItem.equals(item)) {
                return true;
            }
        }
        return false;
    }
}
