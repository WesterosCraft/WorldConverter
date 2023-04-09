package me.dags.scraper.v1_18;

import me.dags.converter.datagen.GameDataWriter;
import me.dags.converter.datagen.Schema;
import me.dags.converter.datagen.SectionWriter;
import me.dags.converter.datagen.biome.BiomeData;
import me.dags.converter.datagen.block.BlockData;
import me.dags.converter.datagen.block.StateData;
import net.minecraft.world.level.biome.Biome;
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
import java.util.LinkedList;
import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
@Mod("data_generator")
public class Scraper {

    public Scraper() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public static void generate(FMLCommonSetupEvent event) {
        Schema schema = Schema.modern("1.18");
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
    	int id = ((net.minecraftforge.registries.ForgeRegistry<Biome>)net.minecraftforge.registries.ForgeRegistries.BIOMES).getID(biome);
        return new BiomeData(biome.getRegistryName(), id);
    }

    private static BlockData getBlockData(Block block) {
        StateData defaults = getStateData(block.defaultBlockState());
        List<StateData> states = new LinkedList<>();
        boolean upgrade = false;
        // Check if class of block we need to upgrade
        if ((block instanceof StairBlock) ||
    		(block instanceof WallBlock) ||
    		(block instanceof FenceBlock) ||
    		(block instanceof IronBarsBlock) ||
    		(block instanceof FlowerPotBlock) ||
    		(block instanceof DoublePlantBlock) ||
    		(block instanceof FenceGateBlock) ||
    		(block instanceof DoorBlock) ||
    		(block instanceof FireBlock) ||
    		(block instanceof RedStoneWireBlock)){
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
