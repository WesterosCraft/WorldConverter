package me.dags.converter.datagen;

import me.dags.converter.datagen.biome.BiomeData;
import me.dags.converter.datagen.biome.BiomeWriter;
import me.dags.converter.datagen.block.BlockData;
import me.dags.converter.datagen.block.BlockWriter;
import me.dags.converter.datagen.block.BlockWriterLegacy;
import me.dags.converter.datagen.item.ItemData;
import me.dags.converter.datagen.item.ItemWriter;
import me.dags.converter.datagen.writer.ValueWriter;

public class Schema {

    public final String version;
    public final ValueWriter<BlockData> block;
    public final ValueWriter<BiomeData> biome;
    public final ValueWriter<ItemData> item;

    public Schema(String version, ValueWriter<BlockData> block, ValueWriter<BiomeData> biome, ValueWriter<ItemData> item) {
        this.version = version;
        this.block = block;
        this.biome = biome;
        this.item = item;
    }

    public static Schema legacy(String version) {
        return new Schema(version, new BlockWriterLegacy(), new BiomeWriter(), new ItemWriter());
    }

    public static Schema modern(String version) {
        return new Schema(version, new BlockWriter(), new BiomeWriter(), new ItemWriter());
    }
}
