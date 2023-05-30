package me.dags.converter.item;

import me.dags.converter.registry.RegistryItem;

public class Item implements RegistryItem<Item> {

    public static final int MAX_ID = 99999;
    public static final Item NONE = new Item("null", 0);

    private final String name;
    private final int id;

    public Item(Object name, int id) {
        this.name = "" + name;
        this.id = id;
    }

    @Override
    public String getIdentifier() {
        return name;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public Item parseExtended(String properties) {
        return this;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj.getClass() == Item.class && name.equals(((Item) obj).name);
    }
}
