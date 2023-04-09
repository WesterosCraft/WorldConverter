package me.dags.scraper.v1_18;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import me.dags.scraper.v1_18.DataFixers;
import net.minecraft.nbt.CompoundTag;

import java.io.*;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Mappings {

    public static void generate() {
        File gameData = new File("data.json");
        if (!gameData.exists()) {
            return;
        }
        try (FileReader reader = new FileReader(gameData)) {
            JsonElement root = new JsonParser().parse(reader);
            if (!root.isJsonObject()) {
                return;
            }
            String version = root.getAsJsonObject().get("version").getAsString();
            try (FileWriter writer = new FileWriter("blocks_" + version + "-1.18.txt")) {
                writeBlockMappings(root.getAsJsonObject().getAsJsonObject("blocks"), writer);
            }
            try (FileWriter writer = new FileWriter("biomes_" + version + "-1.18.txt")) {
                writeBiomeMappings(root.getAsJsonObject().getAsJsonObject("biomes"), writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeBlockMappings(JsonObject blocks, Writer writer) {
        PrintWriter printWriter = new PrintWriter(writer);
        for (Map.Entry<String, JsonElement> block : blocks.entrySet()) {
            if (!block.getValue().isJsonObject()) {
                continue;
            }

            List<CompoundTag> states = parseStates(block.getKey(), block.getValue().getAsJsonObject());
            for (CompoundTag state : states) {
                if (!DataFixers.fixable(state)) {
                    continue;
                }

                String from = serialize(state);
                CompoundTag output;
                try {
                    output = DataFixers.fixState(state);
                } catch (Throwable t) {
                    output = state;
                }

                String to = serialize(output);
                if (from.equals(to)) {
                    continue;
                }
                printWriter.print(from);
                printWriter.print(" -> ");
                printWriter.print(to);
                printWriter.println();
            }
        }
        printWriter.flush();
    }

    private static void writeBiomeMappings(JsonObject biomes, Writer writer) {
        PrintWriter printWriter = new PrintWriter(writer);
        for (Map.Entry<String, JsonElement> entry : biomes.entrySet()) {
            String from = entry.getKey();
            String to = DataFixers.fixBiome(from);
            if (from.equals(to)) {
                continue;
            }
            printWriter.print(from);
            printWriter.print(" -> ");
            printWriter.print(to);
            printWriter.println();
        }
    }

    private static List<CompoundTag> parseStates(String name, JsonObject block) {
        JsonElement def = block.get("default");
        JsonElement variants = block.get("states");
        if (def == null || variants == null) {
            return Collections.singletonList(parse(name, ""));
        }

        CompoundTag defaults = parse(name, def.getAsString());
        if (variants.isJsonArray()) {
            List<CompoundTag> states = new LinkedList<>();
            for (JsonElement state : variants.getAsJsonArray()) {
                states.add(parse(name, state.getAsString()));
            }
            return states;
        } else if (variants.isJsonObject()) {
            CompoundTag[] states = new CompoundTag[16];
            for (Map.Entry<String, JsonElement> e : variants.getAsJsonObject().entrySet()) {
                int meta = e.getValue().getAsInt();
                CompoundTag state = parse(name, e.getKey());
                CompoundTag current = states[meta];
                if (current == null) {
                    states[meta] = state;
                } else if (compare(state, current, defaults) < 0){
                    states[meta] = state;
                }
            }
            List<CompoundTag> list = new LinkedList<>();
            for (CompoundTag state : states) {
                if (state != null) {
                    list.add(state);
                }
            }
            return list;
        } else {
            return Collections.singletonList(defaults);
        }
    }

    private static CompoundTag parse(String name, String properties) {
        CompoundTag root = new CompoundTag();
        root.putString("Name", name);
        if (properties.isEmpty()) {
            return root;
        }

        try {
            CompoundTag props = new CompoundTag();
            String[] pairs = properties.split(",");
            for (String pair : pairs) {
                String[] keyVal = pair.split("=");
                props.putString(keyVal[0], keyVal[1]);
            }
            root.put("Properties", props);
            return root;
        } catch (Throwable t) {
            System.out.println("##### " + name + " : " + properties);
            throw new RuntimeException(t);
        }
    }

    private static String serialize(CompoundTag state) {
        String name = state.getString("Name");
        if (state.contains("Properties")) {
            CompoundTag props = state.getCompound("Properties");
            StringBuilder sb = new StringBuilder(name);
            sb.append('[');
            serializeProperties(sb, props);
            sb.append(']');
            return sb.toString();
        }
        return name;
    }

    private static String serializeProperties(CompoundTag props) {
        return serializeProperties(new StringBuilder(), props).toString();
    }

    private static StringBuilder serializeProperties(StringBuilder sb, CompoundTag props) {
        boolean first = true;
        for (String key : props.getAllKeys()) {
            if (!first) {
                sb.append(',');
            }
            first = false;
            sb.append(key).append('=').append(props.getString(key));
        }
        return sb;
    }

    private static int compare(CompoundTag a, CompoundTag b, CompoundTag defaults) {
        a = a.getCompound("Properties");
        b = b.getCompound("Properties");
        defaults = defaults.getCompound("Properties");

        int score = 0;
        for (String key : a.getAllKeys()) {
            Object aVal = a.getString(key);
            Object bVal = b.getString(key);
            if (aVal.equals(bVal)) {
                continue;
            }
            Object defVal = defaults.getString(key);
            if (aVal.equals(defVal)) {
                score--;
            } else if (bVal.equals(defVal)) {
                score++;
            }
        }
        return score;
    }
}
