package me.dags.scraper.v1_18;

import com.mojang.serialization.Dynamic;

import net.minecraft.SharedConstants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.StringTag;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;

public class DataFixers {

    private static final Map<String, String> pottedPlants = new HashMap<>();

    static {
        pottedPlants.put("empty", Blocks.FLOWER_POT.getRegistryName() + "");
        pottedPlants.put("rose", Blocks.POTTED_POPPY.getRegistryName() + "");  // X
        pottedPlants.put("blue_orchid", Blocks.POTTED_BLUE_ORCHID.getRegistryName() + "");
        pottedPlants.put("allium", Blocks.POTTED_ALLIUM.getRegistryName() + "");
        pottedPlants.put("houstonia", Blocks.POTTED_AZURE_BLUET.getRegistryName() + ""); // ?
        pottedPlants.put("red_tulip", Blocks.POTTED_RED_TULIP.getRegistryName() + "");
        pottedPlants.put("orange_tulip", Blocks.POTTED_ORANGE_TULIP.getRegistryName() + "");
        pottedPlants.put("white_tulip", Blocks.POTTED_WHITE_TULIP.getRegistryName() + "");
        pottedPlants.put("pink_tulip", Blocks.POTTED_PINK_TULIP.getRegistryName() + "");
        pottedPlants.put("oxeye_daisy", Blocks.POTTED_OXEYE_DAISY.getRegistryName() + "");
        pottedPlants.put("dandelion", Blocks.POTTED_DANDELION.getRegistryName() + "");
        pottedPlants.put("oak_sapling", Blocks.POTTED_OAK_SAPLING.getRegistryName() + "");
        pottedPlants.put("spruce_sapling", Blocks.POTTED_SPRUCE_SAPLING.getRegistryName() + "");
        pottedPlants.put("birch_sapling", Blocks.POTTED_BIRCH_SAPLING.getRegistryName() + "");
        pottedPlants.put("jungle_sapling", Blocks.POTTED_JUNGLE_SAPLING.getRegistryName() + "");
        pottedPlants.put("acacia_sapling", Blocks.POTTED_ACACIA_SAPLING.getRegistryName() + "");
        pottedPlants.put("dark_oak_sapling", Blocks.POTTED_DARK_OAK_SAPLING.getRegistryName() + "");
        pottedPlants.put("mushroom_brown", Blocks.POTTED_BROWN_MUSHROOM.getRegistryName() + "");
        pottedPlants.put("dead_bush", Blocks.POTTED_DEAD_BUSH.getRegistryName() + "");
        pottedPlants.put("fern", Blocks.POTTED_FERN.getRegistryName() + "");
        pottedPlants.put("cactus", Blocks.POTTED_CACTUS.getRegistryName() + "");
        pottedPlants.put("mushroom_red", Blocks.POTTED_RED_MUSHROOM.getRegistryName() + "");
    }

    public static String fixBiome(String biome) {
        int oldVer = 1125;
        int newVer = SharedConstants.getCurrentVersion().getWorldVersion();
        System.out.println(biome + ": " + oldVer + " -> " + newVer);
        Dynamic<?> dynamic = new Dynamic<>(NbtOps.INSTANCE, StringTag.valueOf(biome));
        dynamic = net.minecraft.util.datafix.DataFixers.getDataFixer().update(References.BIOME, dynamic, oldVer, newVer);
        StringTag tag = (StringTag) dynamic.getValue();
        return tag.toString();
    }

    public static CompoundTag fixState(CompoundTag state) {
        CompoundTag fixed = customFix(state);
        if (fixed == state) {
            return fixOneState(state);
        }
        return fixed;
    }

    public static CompoundTag fixOneState(CompoundTag state) {
        int oldVer = 100;
        int newVer = SharedConstants.getCurrentVersion().getWorldVersion();
        Dynamic<?> dynamic = new Dynamic<>(NbtOps.INSTANCE, state);
        dynamic = net.minecraft.util.datafix.DataFixers.getDataFixer().update(References.BLOCK_STATE, dynamic, oldVer, newVer);
        CompoundTag tag = (CompoundTag) dynamic.getValue();
        BlockState blockState = NbtUtils.readBlockState(tag);
        return NbtUtils.writeBlockState(blockState);
    }

    public static boolean fixable(CompoundTag state) {
        switch (state.getString("Name")) {
            case "minecraft:skull":
                return false;
            default:
                return true;
        }
    }

    private static CompoundTag customFix(CompoundTag in) {
        switch (in.getString("Name")) {
            case "minecraft:flower_pot":
                return fixPlantPot(in);
            case "minecraft:leaves":
                return fixLeaves(in);
            default:
                return in;
        }
    }

    private static CompoundTag fixLeaves(CompoundTag in) {
        CompoundTag fixed = fixOneState(in);
        CompoundTag from = in.getCompound("Properties");
        CompoundTag to = fixed.getCompound("Properties");
        boolean persist = from.getString("decayable").equals("false");
        to.putString("persistent", String.valueOf(persist));
        return fixed;
    }

    private static CompoundTag fixDoublePlant(CompoundTag in) {
        return in;
    }

    private static CompoundTag fixPlantPot(CompoundTag in) {
        CompoundTag props = in.getCompound("Properties");
        String contents = props.getString("contents");
        String flattened = pottedPlants.get(contents);
        if (flattened == null) {
            return in;
        }
        CompoundTag flat = new CompoundTag();
        flat.putString("Name", flattened);
        return flat;
    }
}
