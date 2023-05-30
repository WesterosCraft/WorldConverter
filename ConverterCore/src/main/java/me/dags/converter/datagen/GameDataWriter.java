package me.dags.converter.datagen;

import com.google.gson.stream.JsonWriter;
import me.dags.converter.datagen.biome.BiomeData;
import me.dags.converter.datagen.block.BlockData;
import me.dags.converter.datagen.item.ItemData;
import me.dags.converter.datagen.writer.DataWriter;
import me.dags.converter.datagen.writer.JsonDataWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class GameDataWriter implements AutoCloseable {

    private final Schema schema;
    private final DataWriter writer;

    private boolean open = false;

    public GameDataWriter(Schema schema) throws IOException {
        this(schema, new File("game_data.json"));
    }

    public GameDataWriter(Schema schema, File file) throws IOException {
        this.schema = schema;
        this.writer = new JsonDataWriter(new JsonWriter(new FileWriter(file)));
    }

    private void ensureOpen() throws IOException {
        if (!open) {
            open = true;
            writer.beginObject();
            writer.name("version").value(schema.version);
        }
    }

    public SectionWriter<BiomeData> startBiomes() throws IOException {
        ensureOpen();
        return new SectionWriter<>("biomes", writer, schema.biome);
    }

    public SectionWriter<BlockData> startBlocks() throws IOException {
        ensureOpen();
        return new SectionWriter<>("blocks", writer, schema.block);
    }

    public SectionWriter<ItemData> startItems() throws IOException {
        ensureOpen();
        return new SectionWriter<>("items", writer, schema.item);
    }

    @Override
    public void close() throws Exception {
        if (open) {
            open = false;
            writer.endObject().close();
        }
    }
}
