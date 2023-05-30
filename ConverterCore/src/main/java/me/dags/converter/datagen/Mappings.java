package me.dags.converter.datagen;

import me.dags.converter.biome.Biome;
import me.dags.converter.block.BlockState;
import me.dags.converter.converter.ConverterData;
import me.dags.converter.item.Item;
import me.dags.converter.registry.Mapper;
import me.dags.converter.registry.RemappingRegistry;
import me.dags.converter.resource.Container;
import me.dags.converter.resource.Resource;
import me.dags.converter.util.log.Logger;
import me.dags.converter.version.Version;
import me.dags.converter.version.VersionData;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;

public class Mappings {

    private final VersionData from;
    private final VersionData to;
    private final Mapper.Builder<BlockState> blocks;
    private final Mapper.Builder<Biome> biomes;
    private final Mapper.Builder<Item> items;

    private Mappings(VersionData from, VersionData to) {
        this.from = from;
        this.to = to;
        this.blocks = Mapper.builder(from.blocks, to.blocks);
        this.biomes = Mapper.builder(from.biomes, to.biomes);
        this.items = Mapper.builder(from.items, to.items);
    }

    public Mapper.Builder<BlockState> getBlocks() {
        return blocks;
    }

    public Mapper.Builder<Biome> getBiomes() {
        return biomes;
    }

    public Mapper.Builder<Item> getItems() {
        return items;
    }

    public Mappings builtIn() throws ParseException, IOException {
        String version = from.blocks.getVersion() + "-" + to.blocks.getVersion();
        Path path = Paths.get("mappings", version);
        try (Container container = Container.self(Version.class)) {
            for (Resource resource : container.getResources(path.resolve("blocks"))) {
                Logger.log("Parsing block mappings:", resource.getPath());
                getBlocks().parse(resource.getInputStream());
            }
            for (Resource resource : container.getResources(path.resolve("biomes"))) {
                Logger.log("Parsing biome mappings:", resource.getPath());
                getBiomes().parse(resource.getInputStream());
            }
            for (Resource resource : container.getResources(path.resolve("items"))) {
                Logger.log("Parsing biome mappings:", resource.getPath());
                getItems().parse(resource.getInputStream());
            }
        }
        return this;
    }

    public ConverterData build() {
        RemappingRegistry<BlockState> blocks;
        RemappingRegistry<Biome> biomes;
        RemappingRegistry<Item> items;

        if (from == to) {
            if (getBlocks().hasMappings()) {
                blocks = new RemappingRegistry<>(from.blocks, getBlocks().build());
            } else {
                blocks = new RemappingRegistry<>(from.blocks, Mapper.identity(from.version, from.blocks));
            }

            if (getBiomes().hasMappings()) {
                biomes = new RemappingRegistry<>(from.biomes, getBiomes().build());
            } else {
                biomes = new RemappingRegistry<>(from.biomes, Mapper.identity(from.version, from.biomes));
            }
            if (getItems().hasMappings()) {
                items = new RemappingRegistry<>(from.items, getItems().build());
            } else {
                items = new RemappingRegistry<>(from.items, Mapper.identity(from.version, from.items));
            }
        } else {
            blocks = new RemappingRegistry<>(from.blocks, getBlocks().build());
            biomes = new RemappingRegistry<>(from.biomes, getBiomes().build());
            items = new RemappingRegistry<>(from.items, getItems().build());
        }
        return new ConverterData(from.version, blocks, biomes, items);
    }

    public static Mappings build(Version from, Version to) throws Exception {
        if (from == to) {
            VersionData data = from.loadGameData();
            return build(data, data);
        }
        return build(from.loadGameData(), to.loadGameData());
    }

    public static Mappings build(VersionData from, VersionData to) {
        return new Mappings(from, to);
    }
}
