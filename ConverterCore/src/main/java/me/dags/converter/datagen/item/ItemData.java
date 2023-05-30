package me.dags.converter.datagen.item;

public class ItemData {

    private final String name;
    private final int id;

    public ItemData(Object name, int id) {
        this.name = "" + name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}
