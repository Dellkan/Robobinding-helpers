package com.dellkan.robobinding.helpers.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ListContainer<T> implements Serializable {
    private List<T> items = new ArrayList<>();
    private T selectedItem = null;

    public T getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(T selectedItem) {
        this.selectedItem = selectedItem;
    }

    private int selectedPosition = 0;
    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void setSelectedPosition(int position) {
        selectedPosition = position;
        setSelectedItem(getItem(position));
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
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
