package me.dags.scraper.v1_12;

import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.datafix.DataFixesManager;
import net.minecraft.util.datafix.IFixType;

import java.util.HashMap;
import java.util.Map;

public class DataFixers {

    private static final Map<String, String> pottedPlants = new HashMap<>();

    static {
        pottedPlants.put("empty", Blocks.FLOWER_POT.getRegistryName() + "");
    }

    public static String fixBiome(String biome) {
        return biome;
    }

    public static NBTTagCompound fixState(NBTTagCompound state) {
        NBTTagCompound fixed = customFix(state);
        if (fixed == state) {
            return fixOneState(state);
        }
        return fixed;
    }

    public static NBTTagCompound fixOneState(NBTTagCompound state) {
    	return state;
    }

    public static boolean fixable(NBTTagCompound state) {
        switch (state.getString("Name")) {
            case "minecraft:skull":
                return false;
            default:
                return true;
        }
    }

    private static NBTTagCompound customFix(NBTTagCompound in) {
        switch (in.getString("Name")) {
            case "minecraft:flower_pot":
                return fixPlantPot(in);
            case "minecraft:leaves":
                return fixLeaves(in);
            default:
                return in;
        }
    }

    private static NBTTagCompound fixLeaves(NBTTagCompound in) {
        NBTTagCompound fixed = fixOneState(in);
        NBTTagCompound from = in.getCompoundTag("Properties");
        NBTTagCompound to = fixed.getCompoundTag("Properties");
        boolean persist = from.getString("decayable").equals("false");
        to.setString("persistent", String.valueOf(persist));
        return fixed;
    }

    private static NBTTagCompound fixDoublePlant(NBTTagCompound in) {
        return in;
    }

    private static NBTTagCompound fixPlantPot(NBTTagCompound in) {
        NBTTagCompound props = in.getCompoundTag("Properties");
        String contents = props.getString("contents");
        String flattened = pottedPlants.get(contents);
        if (flattened == null) {
            return in;
        }
        NBTTagCompound flat = new NBTTagCompound();
        flat.setString("Name", flattened);
        return flat;
    }
}
