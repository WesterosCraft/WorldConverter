package me.dags.converter.item;

import java.util.function.UnaryOperator;

public class ItemMapper implements UnaryOperator<Item> {
    @Override
    public Item apply(Item biome) {
        return biome;
    }
}
