package me.dags.scraper.v1_12;

import me.dags.converter.block.BlockState;
import me.dags.converter.datagen.GameDataWriter;
import me.dags.converter.datagen.Schema;
import me.dags.converter.datagen.SectionWriter;
import me.dags.converter.datagen.biome.BiomeData;
import me.dags.converter.datagen.block.BlockData;
import me.dags.converter.datagen.block.StateData;
import me.dags.converter.datagen.item.ItemData;
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
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockVine;
import net.minecraft.block.BlockWall;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.jline.utils.Log;

import net.minecraftforge.fml.common.network.NetworkCheckHandler;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid="data_generator")
public class Scraper {
	public static Field blockStateFld;
	private static Set<String> downsolidids = new HashSet<String>();
	private static Set<String> downslabids = new HashSet<String>();
	private static Set<String> downstairids = new HashSet<String>();
	
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
                    if (block instanceof BlockStairs) downstairids.add(block.getRegistryName().toString());
                    if (block instanceof BlockSlab) downslabids.add(block.getRegistryName().toString());
                }
            }
            try (SectionWriter<BiomeData> section = writer.startBiomes()) {
                for (Biome biome : ForgeRegistries.BIOMES) {
                    section.write(geBiomeData(biome));
                }
            }
            try (SectionWriter<ItemData> section = writer.startItems()) {
                for (Item item : ForgeRegistries.ITEMS) {
                    section.write(geItemData(item));
                }
            }
            String val = "downsolidids=";
            for (String s : downsolidids) {
            	val += "\"" + s + "\",";
            }
            System.out.println(val);
            val = "downstairids=";
            for (String s : downstairids) {
            	val += "\"" + s + "\",";
            }
            System.out.println(val);
            val = "downslabids=";
            for (String s : downslabids) {
            	val += "\"" + s + "\",";
            }
            System.out.println(val);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Mappings.generate();
    }

    private static BiomeData geBiomeData(Biome biome) {
        return new BiomeData(biome.getRegistryName(), Biome.getIdForBiome(biome));
    }

    private static ItemData geItemData(Item itm) {
        return new ItemData(itm.getRegistryName(), Item.getIdFromItem(itm));
    }

    public static class NullWorld implements IBlockAccess {
		@Override
		public TileEntity getTileEntity(BlockPos pos) {
			return null;
		}

		@Override
		public int getCombinedLight(BlockPos pos, int lightValue) {
			return 0;
		}

		@Override
		public IBlockState getBlockState(BlockPos pos) {
			return Blocks.AIR.getDefaultState();
		}

		@Override
		public boolean isAirBlock(BlockPos pos) {
			return true;
		}

		@Override
		public Biome getBiome(BlockPos pos) {
			return Biomes.PLAINS;
		}

		@Override
		public int getStrongPower(BlockPos pos, EnumFacing direction) {
			return 0;
		}

		@Override
		public WorldType getWorldType() {
			return WorldType.DEFAULT;
		}

		@Override
		public boolean isSideSolid(BlockPos pos, EnumFacing side, boolean _default) {
			return false;
		}    	
    }
    private static NullWorld nullWorld = new NullWorld();
    
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
        	boolean isDownSolid = false;
	        BlockStateContainer sc = (BlockStateContainer) blockStateFld.get(block);
	        for (IBlockState state : sc.getValidStates()) {
	            states.add(getStateData(state));
	            try {
		            if (block.getBlockFaceShape(nullWorld, state, new BlockPos(0,0,0), EnumFacing.DOWN) == BlockFaceShape.SOLID) {
		            	if (!isDownSolid) {
		            		isDownSolid = true;
		            		downsolidids.add(block.getRegistryName().toString());
		            	}
		            }
	            } catch (Exception x) {
	            	// Ignore blocks we cannot check shape on (tile entity blocks)
	            }
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
