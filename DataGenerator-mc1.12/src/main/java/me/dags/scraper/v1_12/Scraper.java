package me.dags.scraper.v1_12;

import me.dags.converter.datagen.GameDataWriter;
import me.dags.converter.datagen.Schema;
import me.dags.converter.datagen.SectionWriter;
import me.dags.converter.datagen.biome.BiomeData;
import me.dags.converter.datagen.block.BlockData;
import me.dags.converter.datagen.block.StateData;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockFire;
import net.minecraft.block.BlockFlowerPot;
import net.minecraft.block.BlockPane;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockVine;
import net.minecraft.block.BlockWall;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.minecraftforge.fml.common.network.NetworkCheckHandler;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid="data_generator")
public class Scraper {
	public static Field blockStateFld;
	
    public Scraper() {
        MinecraftForge.EVENT_BUS.register(this);
    }
    @EventHandler
    public void generate(FMLPostInitializationEvent event) {
    	Field[] flds = Block.class.getDeclaredFields();	// Get all fields
    	for (Field f : flds) {
            if (f.getType().isAssignableFrom(BlockStateContainer.class)) {
            	blockStateFld = f;
            	blockStateFld.setAccessible(true);
            }
    	}
    	if (blockStateFld == null) {
        	System.err.println("Cannot find blockState in Block");
        }

        Schema schema = Schema.legacy("1.12");
        try (GameDataWriter writer = new GameDataWriter(schema)) {
            try (SectionWriter<BlockData> section = writer.startBlocks()){
                for (Block block : ForgeRegistries.BLOCKS) {
                    section.write(getBlockData(block));
                }
            }
            try (SectionWriter<BiomeData> section = writer.startBiomes()) {
                for (Biome biome : ForgeRegistries.BIOMES) {
                    section.write(geBiomeData(biome));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Mappings.generate();
    }

    private static BiomeData geBiomeData(Biome biome) {
        return new BiomeData(biome.getRegistryName(), Biome.getIdForBiome(biome));
    }

    private static BlockData getBlockData(Block block) {
        StateData defaults = getStateData(block.getDefaultState());
        List<StateData> states = new LinkedList<>();
        boolean upgrade = false;
        // Check if class of block we need to upgrade
        if ((block instanceof BlockStairs) ||
    		(block instanceof BlockWall) ||
    		(block instanceof BlockFence) ||
    		(block instanceof BlockPane) ||
    		(block instanceof BlockFlowerPot) ||
    		(block instanceof BlockDoublePlant) ||
    		(block instanceof BlockFenceGate) ||
    		(block instanceof BlockDoor) ||
    		(block instanceof BlockFire) ||
    		(block instanceof BlockRedstoneWire) ||
        	(block instanceof BlockChest)){
        	upgrade = true;
        }
        try {
        BlockStateContainer sc = (BlockStateContainer) blockStateFld.get(block);
        for (IBlockState state : sc.getValidStates()) {
            states.add(getStateData(state));
        }
        } catch (Exception x) {
        	System.out.println("Exception readong block state");
        }
        return new BlockData(block.getRegistryName(), Block.getIdFromBlock(block), upgrade, defaults, states);
    }

    private static StateData getStateData(IBlockState state) {
        StringBuilder sb = new StringBuilder();
        Map<IProperty<?>, Comparable<?>> map = state.getProperties();
        TreeMap<String, IProperty<?>> pmap = new TreeMap<String, IProperty<?>>();
        for (IProperty<?> entry : map.keySet()) {
        	pmap.put(entry.getName(), entry);
        }
        for (IProperty<?> p : pmap.values()) {
            if (sb.length() > 0) {
                sb.append(',');
            }
            Comparable<?> val = state.getValue(p);
            String v = val.toString().toLowerCase();
            if (val instanceof IStringSerializable) {
            	v = ((IStringSerializable) val).getName();
            }
            sb.append(p.getName()).append('=').append(v);
        };
        return new StateData(sb.toString(), state.getBlock().getMetaFromState(state));
    }
    @net.minecraftforge.fml.common.network.NetworkCheckHandler
    public boolean netCheckHandler(Map<String, String> mods, Side side) {
        return true;
    }

}
