package me.dags.scraper.v1_18;

import me.dags.converter.datagen.GameDataWriter;
import me.dags.converter.datagen.Schema;
import me.dags.converter.datagen.SectionWriter;
import me.dags.converter.datagen.biome.BiomeData;
import me.dags.converter.datagen.block.BlockData;
import me.dags.converter.datagen.block.StateData;
import me.dags.converter.datagen.item.ItemData;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
@Mod("data_generator")
public class Scraper {

	public Scraper() {
		MinecraftForge.EVENT_BUS.register(this);
	}

	// We're going to 1.15 format, and allowing upgrade in place to get to 1.18, so
	// use 1.15 biomes
	private static Map<String, Integer> bipmes115 = new HashMap<String, Integer>();

	static {
    	bipmes115.put("minecraft:ocean", 0);
    	bipmes115.put("minecraft:plains", 1);
		bipmes115.put("minecraft:desert", 2);
		bipmes115.put("minecraft:mountains", 3);
		bipmes115.put("minecraft:forest", 4);
		bipmes115.put("minecraft:taiga", 5);
		bipmes115.put("minecraft:swamp", 6);
		bipmes115.put("minecraft:river", 7);
		bipmes115.put("minecraft:nether", 8);
		bipmes115.put("minecraft:the_end", 9);
		bipmes115.put("minecraft:frozen_ocean", 10);
		bipmes115.put("minecraft:frozen_river", 11);
		bipmes115.put("minecraft:snowy_tundra", 12);
		bipmes115.put("minecraft:snowy_mountains", 13);
		bipmes115.put("minecraft:mushroom_fields", 14);
		bipmes115.put("minecraft:mushroom_field_shore", 15);
		bipmes115.put("minecraft:beach", 16);
		bipmes115.put("minecraft:desert_hills", 17);
		bipmes115.put("minecraft:wooded_hills", 18);
		bipmes115.put("minecraft:taiga_hills", 19);
		bipmes115.put("minecraft:mountain_edge", 20);
		bipmes115.put("minecraft:jungle", 21);
		bipmes115.put("minecraft:jungle_hills", 22);
		bipmes115.put("minecraft:jungle_edge", 23);
		bipmes115.put("minecraft:deep_ocean", 24);
		bipmes115.put("minecraft:stone_shore", 25);
		bipmes115.put("minecraft:snowy_beach", 26);
		bipmes115.put("minecraft:birch_forest", 27);
		bipmes115.put("minecraft:birch_forest_hills", 28);
		bipmes115.put("minecraft:dark_forest", 29);
		bipmes115.put("minecraft:snowy_taiga", 30);
		bipmes115.put("minecraft:snowy_taiga_hills", 31);
		bipmes115.put("minecraft:giant_tree_taiga", 32);
		bipmes115.put("minecraft:giant_tree_taiga_hills", 33);
		bipmes115.put("minecraft:wooded_mountains", 34);
		bipmes115.put("minecraft:savanna", 35);
		bipmes115.put("minecraft:savanna_plateau", 36);
		bipmes115.put("minecraft:badlands", 37);
		bipmes115.put("minecraft:wooded_badlands_plateau", 38);
		bipmes115.put("minecraft:badlands_plateau", 39);
		bipmes115.put("minecraft:small_end_islands", 40);
		bipmes115.put("minecraft:end_midlands", 41);
		bipmes115.put("minecraft:end_highlands", 42);
		bipmes115.put("minecraft:end_barrens", 43);
		bipmes115.put("minecraft:warm_ocean", 44);
		bipmes115.put("minecraft:lukewarm_ocean", 45);
		bipmes115.put("minecraft:cold_ocean", 46);
		bipmes115.put("minecraft:deep_warm_ocean", 47);
		bipmes115.put("minecraft:deep_lukewarm_ocean", 48);
		bipmes115.put("minecraft:deep_cold_ocean", 49);
		bipmes115.put("minecraft:deep_frozen_ocean", 50);
		bipmes115.put("minecraft:the_void", 127);
		bipmes115.put("minecraft:sunflower_plains", 129);
		bipmes115.put("minecraft:desert_lakes", 130);
		bipmes115.put("minecraft:gravelly_mountains", 131);
		bipmes115.put("minecraft:flower_forest", 132);
		bipmes115.put("minecraft:taiga_mountains", 133);
		bipmes115.put("minecraft:swamp_hills", 134);
		bipmes115.put("minecraft:ice_spikes", 140);
		bipmes115.put("minecraft:modified_jungle", 149);
		bipmes115.put("minecraft:modified_jungle_edge", 151);
		bipmes115.put("minecraft:tall_birch_forest", 155);
		bipmes115.put("minecraft:tall_birch_hills", 156);
		bipmes115.put("minecraft:dark_forest_hills", 157);
		bipmes115.put("minecraft:snowy_taiga_mountains", 158);
		bipmes115.put("minecraft:giant_spruce_taiga", 160);
		bipmes115.put("minecraft:giant_spruce_taiga_hills", 161);
		bipmes115.put("minecraft:modified_gravelly_mountains", 162);
		bipmes115.put("minecraft:shattered_savanna", 163);
		bipmes115.put("minecraft:shattered_savanna_plateau", 164);
		bipmes115.put("minecraft:eroded_badlands", 165);
		bipmes115.put("minecraft:modified_wooded_badlands_plateau", 166);
		bipmes115.put("minecraft:modified_badlands_plateau", 167);
		bipmes115.put("minecraft:bamboo_jungle", 168);
		bipmes115.put("minecraft:bamboo_jungle_hills", 169);
    }

	@SubscribeEvent
	public static void generate(FMLCommonSetupEvent event) {
		Schema schema = Schema.modern("1.18");
		try (GameDataWriter writer = new GameDataWriter(schema)) {
			try (SectionWriter<BlockData> section = writer.startBlocks()) {
				for (Block block : ForgeRegistries.BLOCKS) {
					section.write(getBlockData(block));
				}
			}
			try (SectionWriter<BiomeData> section = writer.startBiomes()) {
				for (String rk : bipmes115.keySet()) {
					
					section.write(new BiomeData(rk, bipmes115.get(rk)));
				}
			}
            try (SectionWriter<ItemData> section = writer.startItems()) {
                for (Item item : ForgeRegistries.ITEMS) {
                    section.write(geItemData(item));
                }
            }
		} catch (Exception e) {
			e.printStackTrace();
		}

		Mappings.generate();
	}

	// private static BiomeData geBiomeData(Biome biome) {
	// int id =
	// ((net.minecraftforge.registries.ForgeRegistry<Biome>)net.minecraftforge.registries.ForgeRegistries.BIOMES).getID(biome);
	// return new BiomeData(biome.getRegistryName(), id);
	// }
    private static ItemData geItemData(Item itm) {
        return new ItemData(itm.getRegistryName(), 0);
    }


	private static BlockData getBlockData(Block block) {
		StateData defaults = getStateData(block.defaultBlockState());
		List<StateData> states = new LinkedList<>();
		boolean upgrade = false;
		// Check if class of block we need to upgrade
		if ((block instanceof StairBlock) || (block instanceof WallBlock) || (block instanceof FenceBlock)
				|| (block instanceof IronBarsBlock) || (block instanceof FlowerPotBlock)
				|| (block instanceof DoublePlantBlock) || (block instanceof FenceGateBlock)
				|| (block instanceof DoorBlock) || (block instanceof FireBlock)
				|| (block instanceof RedStoneWireBlock)) {
			upgrade = true;
		}

		for (BlockState state : block.getStateDefinition().getPossibleStates()) {
			states.add(getStateData(state));
		}
		return new BlockData(block.getRegistryName(), 0, upgrade, defaults, states);
	}

	private static StateData getStateData(BlockState state) {
		StringBuilder sb = new StringBuilder();
		state.getProperties().stream().sorted(Comparator.comparing(Property::getName)).forEach(p -> {
			if (sb.length() > 0) {
				sb.append(',');
			}
			sb.append(p.getName()).append('=').append(state.getValue(p).toString().toLowerCase());
		});
		return new StateData(sb.toString());
	}
}
