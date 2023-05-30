package me.dags.converter.datagen.item;

import me.dags.converter.datagen.writer.DataWriter;
import me.dags.converter.datagen.writer.ValueWriter;

import java.io.IOException;

public class ItemWriter implements ValueWriter<ItemData> {
    @Override
    public void write(ItemData item, DataWriter writer) throws IOException {
        writer.name(item.getName()).value(item.getId());
    }
}
