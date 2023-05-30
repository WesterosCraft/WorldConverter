package me.dags.converter.item.registry;

import me.dags.converter.item.Item;
import me.dags.converter.block.registry.PaletteReader;
import me.dags.converter.registry.AbstractRegistry;
import me.dags.converter.registry.Registry;
import me.dags.converter.util.storage.IntMap;
import org.jnbt.CompoundTag;
import org.jnbt.Tag;

import java.text.ParseException;
import java.util.List;

public class ItemRegistry extends AbstractRegistry<Item> implements Registry.Parser<Item> {

    protected ItemRegistry(Builder<Item> builder) {
        super(builder);
    }

    @Override
    public Parser<Item> getParser() {
        return this;
    }

    @Override
    public Item parse(String in) throws ParseException {
        return parse(new Item(in, -1));
    }

    @Override
    public Item parse(CompoundTag in) throws ParseException {
        return parse(new Item(in.getString("K"), in.getInt("V")));
    }

    @Override
    public Reader<Item> parsePalette(List<Tag<CompoundTag>> list) throws ParseException {
        IntMap<Item> map = new IntMap<>(list.size());
        for (int i = 0; i < list.size(); i++) {
            Item biome = parse(list.get(i).asCompound());
            map.put(i, biome);
        }
        return new PaletteReader<>(map, Item.NONE);
    }

    public static Builder<Item> builder(String version) {
        return new AbstractRegistry.Builder<>(version, Item.NONE, Item.MAX_ID, ItemRegistry::new);
    }
}
