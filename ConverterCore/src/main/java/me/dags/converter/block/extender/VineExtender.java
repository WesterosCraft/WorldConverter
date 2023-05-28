package me.dags.converter.block.extender;

import me.dags.converter.block.BlockState;
import me.dags.converter.extent.chunk.Chunk;
import me.dags.converter.registry.Registry;

import java.util.Arrays;
import java.util.HashSet;

import org.jnbt.CompoundTag;
import org.jnbt.StringTag;

public class VineExtender implements StateExtender {
	private static String solidDownBlocks[] = { "minecraft:yellow_glazed_terracotta", "minecraft:stained_hardened_clay",
			"minecraft:daylight_detector_inverted", "westerosblocks:stone_slab_block_5_2",
			"westerosblocks:stone_block_2_stair_3", "westerosblocks:stone_block_2_stair_4",
			"minecraft:double_stone_slab2", "westerosblocks:stone_block_2_stair_0",
			"westerosblocks:stone_block_2_stair_1", "westerosblocks:stone_block_2_stair_2", "minecraft:grass",
			"minecraft:oak_stairs", "minecraft:chain_command_block", "minecraft:magma", "westerosblocks:solid_blocks_1",
			"westerosblocks:wool_slab_block_1_2", "minecraft:monster_egg", "minecraft:concrete_powder",
			"minecraft:enchanting_table", "westerosblocks:trapdoor_block_2", "minecraft:observer",
			"westerosblocks:furnace_block_0", "westerosblocks:furnace_block_4", "westerosblocks:random_slab_block_1_2",
			"westerosblocks:furnace_block_3", "westerosblocks:furnace_block_2", "westerosblocks:furnace_block_1",
			"westerosblocks:terrainset_slab_block_2_2", "minecraft:stone", "minecraft:unpowered_repeater",
			"minecraft:sand", "westerosblocks:snow_stairs", "minecraft:redstone_ore", "minecraft:purpur_slab",
			"westerosblocks:stone_slab_block_6_2", "westerosblocks:thatch_stair_1",
			"westerosblocks:stone_block_5_stair_1", "westerosblocks:stone_block_5_stair_0",
			"westerosblocks:thatch_stair_2", "westerosblocks:trapdoor_block_0", "westerosblocks:stone_block_5_stair_3",
			"westerosblocks:thatch_stair_3", "westerosblocks:stone_block_5_stair_2", "westerosblocks:trapdoor_block_1",
			"westerosblocks:cobble_dark_stairs", "westerosblocks:stone_block_5_stair_5",
			"westerosblocks:stone_block_5_stair_4", "westerosblocks:metal_slab_block_0_2",
			"westerosblocks:stone_block_5_stair_7", "minecraft:nether_brick_stairs", "westerosblocks:thatch_stair_0",
			"westerosblocks:stone_block_5_stair_6", "minecraft:hay_block", "westerosblocks:light_emitting_blocks_0",
			"minecraft:trapdoor", "minecraft:purpur_pillar", "westerosblocks:wool_slab_block_0", "minecraft:stonebrick",
			"minecraft:light_blue_glazed_terracotta", "westerosblocks:wood_slab_block_1",
			"westerosblocks:wood_slab_block_0", "westerosblocks:stone_slab_block_4_2", "westerosblocks:stained_glass_0",
			"westerosblocks:stone_slab_block_9", "statues:blockpebble", "minecraft:jukebox",
			"westerosblocks:stone_slab_block_5", "westerosblocks:sound_blocks_3", "westerosblocks:stone_slab_block_6",
			"westerosblocks:sound_blocks_2", "westerosblocks:stone_slab_block_7", "westerosblocks:sound_blocks_1",
			"westerosblocks:stone_slab_block_8", "westerosblocks:stone_slab_block_1", "westerosblocks:sound_blocks_0",
			"westerosblocks:white_wood_stairs", "minecraft:ice", "minecraft:beacon",
			"westerosblocks:stone_slab_block_2", "westerosblocks:stone_slab_block_3",
			"westerosblocks:stone_slab_block_4", "minecraft:powered_comparator", "westerosblocks:wool_slab_block_0_2",
			"westerosblocks:stone_slab_block_0", "westerosblocks:wool_slab_block_1", "minecraft:grass_path",
			"westerosblocks:river_cobble_stairs", "minecraft:sea_lantern", "westerosblocks:leaves_block_0",
			"minecraft:powered_repeater", "minecraft:wool", "westerosblocks:solid_blocks_0",
			"westerosblocks:random_slab_block_0_2", "minecraft:double_wooden_slab", "minecraft:frosted_ice",
			"minecraft:structure_block", "westerosblocks:cuboid_block_7_stairs_0",
			"westerosblocks:cuboid_block_7_stairs_1", "minecraft:silver_glazed_terracotta",
			"minecraft:brown_glazed_terracotta", "westerosblocks:leaves_block_1", "minecraft:purpur_double_slab",
			"westerosblocks:leaves_block_2", "westerosblocks:leaves_block_3", "minecraft:end_bricks",
			"minecraft:crafting_table", "minecraft:lit_redstone_ore", "minecraft:tnt",
			"westerosblocks:translucent_slab_block_0_2", "westerosblocks:stone_slab_block_3_2",
			"minecraft:brown_mushroom_block", "minecraft:acacia_stairs", "minecraft:red_sandstone",
			"westerosblocks:bedrock_stairs", "minecraft:log", "westerosblocks:stone_block_12",
			"westerosblocks:stone_block_11", "westerosblocks:stone_block_12_stair_0",
			"westerosblocks:stone_block_12_stair_1", "westerosblocks:stone_block_7_stair_0", "minecraft:brick_block",
			"westerosblocks:stone_block_10", "minecraft:pumpkin", "minecraft:bookshelf",
			"minecraft:orange_glazed_terracotta", "minecraft:end_portal_frame", "minecraft:diamond_ore",
			"minecraft:purpur_stairs", "westerosblocks:random_slab_block_3_2", "westerosblocks:stone_valyrian_stairs",
			"westerosblocks:moss_block_0_stair_0", "minecraft:stained_glass", "minecraft:mob_spawner",
			"minecraft:repeating_command_block", "westerosblocks:wood_colours", "minecraft:noteblock", "minecraft:dirt",
			"westerosblocks:stone_block_1_stair_4", "westerosblocks:stone_slab_block_2_2",
			"minecraft:magenta_glazed_terracotta", "minecraft:black_glazed_terracotta", "westerosblocks:arrow_slits_0",
			"minecraft:glass", "westerosblocks:arrow_slits_1", "westerosblocks:stone_block_1_stair_0",
			"westerosblocks:stone_block_1_stair_1", "westerosblocks:stone_block_1_stair_2",
			"westerosblocks:stone_block_1_stair_3", "westerosblocks:arrow_slits_6", "westerosblocks:arrow_slits_2",
			"westerosblocks:arrow_slits_3", "minecraft:slime", "westerosblocks:arrow_slits_4", "minecraft:gold_block",
			"westerosblocks:arrow_slits_5", "westerosblocks:random_slab_block_2", "westerosblocks:random_slab_block_1",
			"westerosblocks:random_slab_block_0", "minecraft:hardened_clay", "westerosblocks:random_slab_block_5",
			"minecraft:end_stone", "westerosblocks:random_slab_block_4", "westerosblocks:random_slab_block_3",
			"westerosblocks:stone_block_4_stair_0", "westerosblocks:sand_block_0",
			"westerosblocks:random_slab_block_2_2", "minecraft:piston", "minecraft:snow_layer",
			"westerosblocks:yellow_wood_stairs", "westerosblocks:black_wood_stairs",
			"westerosblocks:stone_block_4_stair_2", "westerosblocks:stone_block_4_stair_1",
			"minecraft:red_sandstone_stairs", "westerosblocks:stone_block_4_stair_3", "minecraft:leaves2",
			"minecraft:iron_ore", "minecraft:leaves", "westerosblocks:metal_slab_block_1_2", "minecraft:netherrack",
			"minecraft:red_nether_brick", "minecraft:bone_block", "minecraft:blue_glazed_terracotta",
			"minecraft:sandstone_stairs", "minecraft:white_glazed_terracotta", "minecraft:pink_glazed_terracotta",
			"westerosblocks:stone_slab_block_1_2", "minecraft:mossy_cobblestone", "minecraft:red_glazed_terracotta",
			"minecraft:furnace", "minecraft:concrete", "minecraft:iron_trapdoor", "minecraft:green_glazed_terracotta",
			"westerosblocks:grey_wood_stairs", "minecraft:log2", "minecraft:redstone_lamp", "minecraft:quartz_stairs",
			"westerosblocks:stone_block_9_stair_7", "westerosblocks:stone_block_9_stair_6",
			"westerosblocks:stone_block_11_stair_1", "westerosblocks:random_slab_block_5_2",
			"westerosblocks:stone_block_9_stair_5", "westerosblocks:stone_block_9_stair_4", "minecraft:barrier",
			"westerosblocks:stone_block_11_stair_0", "westerosblocks:stone_block_9_stair_8",
			"westerosblocks:orange_brick_stair_0", "westerosblocks:stone_block_9_stair_3",
			"westerosblocks:stone_block_9_stair_2", "westerosblocks:stone_block_9_stair_1",
			"westerosblocks:six_sided_blocks_0", "westerosblocks:stone_block_9_stair_0", "minecraft:stone_stairs",
			"minecraft:sticky_piston", "minecraft:snow", "minecraft:melon_block", "minecraft:unpowered_comparator",
			"westerosblocks:ice_stairs", "westerosblocks:soulsand_like_blocks_0", "minecraft:jungle_stairs",
			"minecraft:diamond_block", "westerosblocks:stone_block_9", "westerosblocks:stone_block_3_stair_11",
			"minecraft:lapis_block", "westerosblocks:stone_block_8", "westerosblocks:stone_block_3_stair_12",
			"minecraft:stone_slab", "minecraft:farmland", "minecraft:prismarine", "minecraft:bedrock",
			"westerosblocks:stone_block_3_stair_10", "minecraft:iron_block", "westerosblocks:stone_block_3_stair_13",
			"minecraft:lit_redstone_lamp", "westerosblocks:metal_block_0", "westerosblocks:stone_block_1",
			"westerosblocks:stone_block_0", "westerosblocks:stone_slab_stair_0", "westerosblocks:stone_block_3",
			"westerosblocks:stone_block_2", "westerosblocks:stone_block_5", "westerosblocks:terrainset_stair_1",
			"westerosblocks:stone_slab_block_0_2", "westerosblocks:stone_block_4", "westerosblocks:terrainset_stair_0",
			"minecraft:spruce_stairs", "westerosblocks:stone_block_7", "westerosblocks:terrainset_stair_3",
			"westerosblocks:stone_block_6", "westerosblocks:terrainset_stair_2", "minecraft:gravel",
			"minecraft:nether_brick", "minecraft:carpet", "minecraft:soul_sand", "minecraft:red_mushroom_block",
			"minecraft:glowstone", "minecraft:nether_wart_block", "westerosblocks:utility_block_0",
			"westerosblocks:random_slab_block_4_2", "minecraft:lime_glazed_terracotta", "minecraft:stone_slab2",
			"westerosblocks:metal_slab_block_1", "westerosblocks:green_wood_stairs",
			"westerosblocks:metal_slab_block_0", "minecraft:piston_head", "minecraft:stone_brick_stairs",
			"westerosblocks:daub_wattle_block_6", "minecraft:dropper", "westerosblocks:daub_wattle_block_5",
			"minecraft:gold_ore", "westerosblocks:daub_wattle_block_7", "westerosblocks:stone_slab_block_9_2",
			"minecraft:lit_furnace", "minecraft:birch_stairs", "minecraft:cyan_glazed_terracotta",
			"statues:blockdisplaystand", "westerosblocks:daub_wattle_block_0", "westerosblocks:daub_wattle_block_2",
			"minecraft:purpur_block", "westerosblocks:daub_wattle_block_1", "minecraft:daylight_detector",
			"westerosblocks:daub_wattle_block_4", "westerosblocks:daub_wattle_block_3",
			"westerosblocks:wood_slab_block_1_2", "minecraft:quartz_block", "minecraft:dark_oak_stairs",
			"minecraft:dispenser", "westerosblocks:crate_block_2", "minecraft:coal_ore",
			"westerosblocks:translucent_slab_block_0", "westerosblocks:moss_block_0", "westerosblocks:crate_block_0",
			"westerosblocks:crate_block_1", "minecraft:planks", "minecraft:brick_stairs", "minecraft:quartz_ore",
			"westerosblocks:terrainset_stair_10", "minecraft:double_stone_slab", "westerosblocks:terrainset_stair_12",
			"westerosblocks:terrainset_stair_11", "westerosblocks:terrainset_stair_14",
			"westerosblocks:terrainset_stair_13", "westerosblocks:terrainset_stair_16",
			"westerosblocks:terrainset_stair_15", "westerosblocks:terrainset_stair_18",
			"westerosblocks:terrainset_stair_17", "minecraft:cobblestone", "westerosblocks:stone_block_3_stair_1",
			"westerosblocks:stone_block_3_stair_0", "westerosblocks:stone_block_3_stair_3",
			"westerosblocks:terrainset_stair_5", "minecraft:redstone_block", "minecraft:coal_block",
			"westerosblocks:stone_block_3_stair_2", "westerosblocks:terrainset_stair_4",
			"westerosblocks:stone_block_3_stair_5", "westerosblocks:terrainset_stair_7",
			"westerosblocks:northern_wood_stairs", "westerosblocks:stone_block_3_stair_4",
			"westerosblocks:terrainset_stair_6", "westerosblocks:stone_block_3_stair_7",
			"westerosblocks:terrainset_stair_9", "westerosblocks:stone_block_3_stair_6",
			"westerosblocks:terrainset_stair_8", "westerosblocks:stone_slab_block_8_2",
			"westerosblocks:stone_block_3_stair_9", "westerosblocks:terrainset_slab_block_0_2",
			"westerosblocks:stone_block_3_stair_8", "westerosblocks:purple_wood_stairs", "minecraft:wooden_slab",
			"minecraft:command_block", "minecraft:sandstone", "minecraft:lit_pumpkin", "minecraft:packed_ice",
			"westerosblocks:wood_slab_block_0_2", "minecraft:emerald_block", "minecraft:sponge", "minecraft:lapis_ore",
			"minecraft:mycelium", "westerosblocks:wood_vertical", "minecraft:obsidian",
			"westerosblocks:metal_block_0_stair_5", "westerosblocks:terrainset_slab_block_2",
			"westerosblocks:blue_wood_stairs", "westerosblocks:metal_block_0_stair_6",
			"westerosblocks:terrainset_slab_block_1", "westerosblocks:metal_block_0_stair_3",
			"westerosblocks:metal_block_0_stair_4", "westerosblocks:bookshelves_block_0",
			"westerosblocks:metal_block_0_stair_9", "westerosblocks:bench_block_0",
			"westerosblocks:metal_block_0_stair_7", "westerosblocks:metal_block_0_stair_8",
			"minecraft:purple_glazed_terracotta", "westerosblocks:log_block_0",
			"westerosblocks:terrainset_slab_block_0", "westerosblocks:red_wood_stairs", "opframe:opframe",
			"westerosblocks:log_block_6", "minecraft:gray_glazed_terracotta",
			"westerosblocks:sandstone_block_0_stair_8", "westerosblocks:log_block_5", "minecraft:clay",
			"westerosblocks:sandstone_block_0_stair_5", "westerosblocks:log_block_8", "westerosblocks:log_block_7",
			"westerosblocks:sandstone_block_0_stair_3", "westerosblocks:log_block_2", "westerosblocks:log_block_1",
			"westerosblocks:sandstone_block_0_stair_4", "westerosblocks:sandstone_block_0_stair_1",
			"westerosblocks:log_block_4", "westerosblocks:stone_slab_block_7_2",
			"westerosblocks:sandstone_block_0_stair_2", "westerosblocks:log_block_3",
			"westerosblocks:terrainset_slab_block_1_2", "westerosblocks:sandstone_block_0_stair_0",
			"minecraft:emerald_ore", "westerosblocks:metal_block_0_stair_1", "westerosblocks:metal_block_0_stair_2" };
    private static String[] solidDownStairs = { "minecraft:sandstone_stairs","westerosblocks:stone_block_2_stair_3","westerosblocks:stone_block_2_stair_4","westerosblocks:stone_block_2_stair_0","westerosblocks:stone_block_2_stair_1","westerosblocks:stone_block_2_stair_2","minecraft:oak_stairs","westerosblocks:grey_wood_stairs","minecraft:quartz_stairs","westerosblocks:stone_block_9_stair_7","westerosblocks:stone_block_9_stair_6","westerosblocks:stone_block_11_stair_1","westerosblocks:stone_block_9_stair_5","westerosblocks:stone_block_9_stair_4","westerosblocks:stone_block_11_stair_0","westerosblocks:stone_block_9_stair_8","westerosblocks:orange_brick_stair_0","westerosblocks:stone_block_9_stair_3","westerosblocks:stone_block_9_stair_2","westerosblocks:stone_block_9_stair_1","westerosblocks:stone_block_9_stair_0","minecraft:stone_stairs","westerosblocks:snow_stairs","westerosblocks:thatch_stair_1","westerosblocks:stone_block_5_stair_1","westerosblocks:stone_block_5_stair_0","westerosblocks:thatch_stair_2","westerosblocks:stone_block_5_stair_3","westerosblocks:thatch_stair_3","westerosblocks:stone_block_5_stair_2","westerosblocks:ice_stairs","westerosblocks:cobble_dark_stairs","westerosblocks:stone_block_5_stair_5","minecraft:jungle_stairs","westerosblocks:stone_block_5_stair_4","westerosblocks:stone_block_5_stair_7","minecraft:nether_brick_stairs","westerosblocks:thatch_stair_0","westerosblocks:stone_block_5_stair_6","westerosblocks:stone_block_3_stair_11","westerosblocks:stone_block_3_stair_12","westerosblocks:stone_block_3_stair_10","westerosblocks:stone_block_3_stair_13","westerosblocks:stone_slab_stair_0","westerosblocks:terrainset_stair_1","westerosblocks:terrainset_stair_0","minecraft:spruce_stairs","westerosblocks:terrainset_stair_3","westerosblocks:terrainset_stair_2","westerosblocks:white_wood_stairs","westerosblocks:river_cobble_stairs","westerosblocks:green_wood_stairs","minecraft:stone_brick_stairs","westerosblocks:cuboid_block_7_stairs_0","westerosblocks:cuboid_block_7_stairs_1","minecraft:birch_stairs","minecraft:dark_oak_stairs","minecraft:acacia_stairs","westerosblocks:bedrock_stairs","westerosblocks:stone_block_12_stair_0","minecraft:brick_stairs","westerosblocks:stone_block_12_stair_1","westerosblocks:stone_block_7_stair_0","westerosblocks:terrainset_stair_10","westerosblocks:terrainset_stair_12","westerosblocks:terrainset_stair_11","westerosblocks:terrainset_stair_14","westerosblocks:terrainset_stair_13","westerosblocks:terrainset_stair_16","westerosblocks:terrainset_stair_15","westerosblocks:terrainset_stair_18","westerosblocks:terrainset_stair_17","westerosblocks:stone_block_3_stair_1","minecraft:purpur_stairs","westerosblocks:stone_block_3_stair_0","westerosblocks:stone_block_3_stair_3","westerosblocks:terrainset_stair_5","westerosblocks:stone_block_3_stair_2","westerosblocks:terrainset_stair_4","westerosblocks:stone_block_3_stair_5","westerosblocks:terrainset_stair_7","westerosblocks:northern_wood_stairs","westerosblocks:stone_block_3_stair_4","westerosblocks:stone_valyrian_stairs","westerosblocks:terrainset_stair_6","westerosblocks:stone_block_3_stair_7","westerosblocks:terrainset_stair_9","westerosblocks:stone_block_3_stair_6","westerosblocks:terrainset_stair_8","westerosblocks:moss_block_0_stair_0","westerosblocks:stone_block_3_stair_9","westerosblocks:stone_block_3_stair_8","westerosblocks:purple_wood_stairs","westerosblocks:stone_block_1_stair_4","westerosblocks:stone_block_1_stair_0","westerosblocks:stone_block_1_stair_1","westerosblocks:stone_block_1_stair_2","westerosblocks:stone_block_1_stair_3","westerosblocks:metal_block_0_stair_5","westerosblocks:blue_wood_stairs","westerosblocks:metal_block_0_stair_6","westerosblocks:metal_block_0_stair_3","westerosblocks:metal_block_0_stair_4","westerosblocks:metal_block_0_stair_9","westerosblocks:metal_block_0_stair_7","westerosblocks:metal_block_0_stair_8","westerosblocks:stone_block_4_stair_0","westerosblocks:red_wood_stairs","westerosblocks:sandstone_block_0_stair_8","westerosblocks:yellow_wood_stairs","westerosblocks:sandstone_block_0_stair_5","westerosblocks:black_wood_stairs","westerosblocks:sandstone_block_0_stair_3","westerosblocks:stone_block_4_stair_2","westerosblocks:sandstone_block_0_stair_4","westerosblocks:stone_block_4_stair_1","minecraft:red_sandstone_stairs","westerosblocks:sandstone_block_0_stair_1","westerosblocks:sandstone_block_0_stair_2","westerosblocks:stone_block_4_stair_3","westerosblocks:sandstone_block_0_stair_0","westerosblocks:metal_block_0_stair_1","westerosblocks:metal_block_0_stair_2" };
    private static String[] solidDownSlabs = { "westerosblocks:wood_slab_block_1_2","westerosblocks:translucent_slab_block_0_2","westerosblocks:stone_slab_block_5_2","westerosblocks:stone_slab_block_3_2","westerosblocks:stone_slab_block_1_2","minecraft:double_stone_slab2","westerosblocks:translucent_slab_block_0","westerosblocks:wool_slab_block_1_2","westerosblocks:random_slab_block_5_2","minecraft:double_stone_slab","westerosblocks:random_slab_block_1_2","westerosblocks:random_slab_block_3_2","westerosblocks:terrainset_slab_block_2_2","minecraft:purpur_slab","westerosblocks:stone_slab_block_8_2","westerosblocks:terrainset_slab_block_0_2","westerosblocks:stone_slab_block_6_2","minecraft:wooden_slab","westerosblocks:metal_slab_block_0_2","minecraft:stone_slab","westerosblocks:wood_slab_block_0_2","westerosblocks:wool_slab_block_0","westerosblocks:wood_slab_block_1","westerosblocks:wood_slab_block_0","westerosblocks:stone_slab_block_4_2","westerosblocks:stone_slab_block_2_2","westerosblocks:stone_slab_block_9","westerosblocks:stone_slab_block_0_2","westerosblocks:stone_slab_block_5","westerosblocks:stone_slab_block_6","westerosblocks:stone_slab_block_7","westerosblocks:stone_slab_block_8","westerosblocks:stone_slab_block_1","westerosblocks:stone_slab_block_2","westerosblocks:stone_slab_block_3","westerosblocks:stone_slab_block_4","westerosblocks:wool_slab_block_0_2","westerosblocks:stone_slab_block_0","westerosblocks:wool_slab_block_1","westerosblocks:random_slab_block_2","westerosblocks:random_slab_block_4_2","westerosblocks:terrainset_slab_block_2","westerosblocks:random_slab_block_1","westerosblocks:terrainset_slab_block_1","westerosblocks:random_slab_block_0","minecraft:stone_slab2","westerosblocks:metal_slab_block_1","westerosblocks:random_slab_block_5","westerosblocks:random_slab_block_4","westerosblocks:metal_slab_block_0","westerosblocks:random_slab_block_3","westerosblocks:random_slab_block_0_2","minecraft:double_wooden_slab","westerosblocks:random_slab_block_2_2","westerosblocks:terrainset_slab_block_0","westerosblocks:stone_slab_block_9_2","westerosblocks:stone_slab_block_7_2","westerosblocks:terrainset_slab_block_1_2","minecraft:purpur_double_slab","westerosblocks:metal_slab_block_1_2" };
	private static HashSet<String> downSolidIDs = new HashSet<String>(Arrays.asList(solidDownBlocks));
	private static HashSet<String> downStairIDs = new HashSet<String>(Arrays.asList(solidDownStairs));
	private static HashSet<String> downSlabIDs = new HashSet<String>(Arrays.asList(solidDownSlabs));
	@Override
	public BlockState getExtendedState(BlockState state, Registry.Parser<BlockState> parser, Chunk.Reader chunk, int x,
			int y, int z) throws Exception {
		CompoundTag properties = state.getData().getCompound("Properties");
		BlockState above = chunk.getState(x, y + 1, z);
		String abovename = above.getBlockName();
		CompoundTag extendedProperties = properties.copy();
		String upval = "false";
		if (downSolidIDs.contains(abovename)) {
			upval = "true";
			CompoundTag aboveprops = above.getData().getCompound("Properties");	
			if (downStairIDs.contains(abovename)) {
				if (aboveprops.getStringTag("half").getValue().equals("top")) upval = "false";
			}
			else if (downSlabIDs.contains(abovename)) {
				if (aboveprops.getStringTag("half").getValue().equals("top")) upval = "false";
			}			
		}
		extendedProperties.put("#up", upval);
        		
		// System.out.println(state.getBlockName() + ".extend(" + properties.toString()
		// + "=" + extendedProperties.toString());

		return BlockState.createTransientInstance(state, extendedProperties);
	}
}
