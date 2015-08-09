package com.dellkan.robobinding.helpers.model;

import java.util.ArrayList;
import java.util.List;

public class ListContainer<T> {
    private List<T> items = new ArrayList<>();
    private T selectedItem = null;

    public T getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(T selectedItem) {
        this.selectedItem = selectedItem;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
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
}
