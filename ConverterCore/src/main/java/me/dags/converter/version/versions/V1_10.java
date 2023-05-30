package me.dags.converter.version.versions;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.dags.converter.biome.Biome;
import me.dags.converter.biome.registry.BiomeRegistry;
import me.dags.converter.block.BlockState;
import me.dags.converter.block.PropertyComparator;
import me.dags.converter.block.Serializer;
import me.dags.converter.block.extender.DoublePlantExtender;
import me.dags.converter.block.extender.StateExtender;
import me.dags.converter.block.registry.BlockRegistry;
import me.dags.converter.item.Item;
import me.dags.converter.item.registry.ItemRegistry;
import me.dags.converter.util.map.FastMap;
import me.dags.converter.version.Version;
import me.dags.converter.version.VersionData;
import me.dags.converter.version.format.BiomeFormat;
import me.dags.converter.version.format.ChunkFormat;
import me.dags.converter.version.format.SchematicFormat;
import org.jnbt.CompoundTag;
import org.jnbt.Nbt;

import java.text.ParseException;
import java.util.Map;

public class V1_10 implements Version {

    private final Map<String, StateExtender> STATE_EXTENDERS = new FastMap<String, StateExtender>() {{
        put("minecraft:double_plant", new DoublePlantExtender());
    }};

    @Override
    public int getId() {
        return 512;
    }

    @Override
    public String getVersion() {
        return "1.10";
    }

    @Override
    public boolean isLegacy() {
        return true;
    }

    @Override
    public ChunkFormat getChunkFormat() {
        return ChunkFormat.LEGACY;
    }

    @Override
    public BiomeFormat getBiomeFormat() {
        return BiomeFormat.LEGACY;
    }

    @Override
    public SchematicFormat getSchematicFormat() {
        return SchematicFormat.LEGACY;
    }

    @Override
    public VersionData parseGameData(JsonObject json) throws Exception {
        BlockRegistry.Builder<BlockState> blocks = BlockRegistry.builder(getVersion(), BlockState.LEGACY_MAX_ID);
        for (Map.Entry<String, JsonElement> entry : json.getAsJsonObject("blocks").entrySet()) {
            if (!entry.getValue().isJsonObject()) {
                continue;
            }

            JsonObject block = entry.getValue().getAsJsonObject();
            JsonObject states = block.getAsJsonObject("states");
            if (states == null || states.size() == 0) {
                parseOne(entry.getKey(), block, blocks);
            } else {
                parse(entry.getKey(), block, blocks);
            }
        }

        BiomeRegistry.Builder<Biome> biomes = BiomeRegistry.builder(getVersion());
        for (Map.Entry<String, JsonElement> entry : json.getAsJsonObject("biomes").entrySet()) {
            Biome biome = new Biome(entry.getKey(), entry.getValue().getAsInt());
            biomes.addUnchecked(biome.getId(), biome);
        }

        BiomeRegistry.Builder<Item> items = ItemRegistry.builder(getVersion());
        for (Map.Entry<String, JsonElement> entry : json.getAsJsonObject("items").entrySet()) {
            Item itm = new Item(entry.getKey(), entry.getValue().getAsInt());
            items.addUnchecked(itm.getId(), itm);
        }

        return new VersionData(this, blocks.build(), biomes.build(), items.build());
    }

    protected Map<String, StateExtender> getExtenders() {
        return STATE_EXTENDERS;
    }

    private void parseOne(String name, JsonObject block, BlockRegistry.Builder<BlockState> builder) throws ParseException {
        int blockId = block.get("id").getAsInt();
        int stateId = BlockState.getStateId(blockId, 0);
        boolean upgrade = block.get("upgrade").getAsBoolean();
        StateExtender extender = getExtenders().getOrDefault(name, StateExtender.NONE);
        CompoundTag state = Nbt.compound(1).put("Name", name);
        builder.addUnchecked(stateId, new BlockState(stateId, state, extender, upgrade));
    }

    private void parse(String name, JsonObject block, BlockRegistry.Builder<BlockState> builder) throws ParseException {
        int blockId = block.get("id").getAsInt();
        JsonObject states = block.getAsJsonObject("states");
        String defaults = block.get("default").getAsString();
        String fixerId = block.has("fixer") ? block.get("fixer").getAsString() : name;
        StateExtender extender = getExtenders().getOrDefault(fixerId, StateExtender.NONE);

        boolean upgrade = block.get("upgrade").getAsBoolean();
        CompoundTag defProps = Serializer.deserializeProps(defaults);
        PropertyComparator stateComparator = new PropertyComparator(defProps);

        int minId = 15;
        CompoundTag[] persistentStates = new CompoundTag[16];
        for (Map.Entry<String, JsonElement> state : states.entrySet()) {
            int meta = state.getValue().getAsInt();
            minId = Math.min(minId, meta);
            CompoundTag props = Serializer.deserializeProps(state.getKey());
            CompoundTag current = persistentStates[meta];
            if (current == null) {
                persistentStates[meta] = props;
                continue;
            }
            if (stateComparator.compare(current, props) > 0) {
                persistentStates[meta] = props;
            }
        }

        for (int meta = 0; meta < persistentStates.length; meta++) {
            CompoundTag props = persistentStates[meta];
            if (props == null) {
                continue;
            }

            int stateId = BlockState.getStateId(blockId, meta);
            CompoundTag state = Nbt.compound(2)
                    .put("Name", name)
                    .put("Properties", props);

            builder.addUnchecked(stateId, new BlockState(stateId, state, extender, upgrade));
        }
    }
}
