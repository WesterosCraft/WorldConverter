package me.dags.converter.data.tile;

import me.dags.converter.block.BlockState;
import me.dags.converter.converter.DataConverter;
import me.dags.converter.data.EntityConverter;
import me.dags.converter.data.EntityListConverter;
import me.dags.converter.data.tile.chisel.ChiselTileConverter;
import me.dags.converter.data.tile.chisel.ChiselTileLegacy;
import me.dags.converter.registry.RemappingRegistry;
import me.dags.converter.util.log.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// unused
public class TileEntityConverters {

    public static DataConverter getTileConverter(RemappingRegistry<BlockState> registry) {
        Logger.log("Adding tile converter");
        return new EntityListConverter("TileEntities", getConverters(registry));
    }

    public static List<EntityConverter> getConverters(RemappingRegistry<BlockState> registry) {
        return Collections.singletonList(getChiselConverter(registry));
    }

    public static EntityConverter getChiselConverter(RemappingRegistry<BlockState> registry) {
        return ChiselTileConverter.builder()
                .states(registry)
                .reader(ChiselTileLegacy::new)
                .writer(ChiselTileLegacy::new)
                .build();
    }

    // Get converters for 1.15+
    public static DataConverter getNewConverters(RemappingRegistry<BlockState> registry) {
    	List<EntityConverter> list = new ArrayList<EntityConverter>();
    	list.add(new ObsoleteTileConverter("minecraft:flower_pot"));
    	list.add(new ObsoleteTileConverter("minecraft:noteblock"));
    	list.add(new ObsoleteTileConverter("minecraft:furnace"));
    	list.add(new ObsoleteTileConverter("minecraft:lit_furnace"));
//    	list.add(new ObsoleteTileConverter("minecraft:bed"));
    	list.add(new ObsoleteTileConverter("westerosblocks:bed_block_0"));
    	list.add(new ObsoleteTileConverter("westerosblocks:bed_block_1"));
    	list.add(new ObsoleteTileConverter("westerosblocks:bed_block_2"));
    	list.add(new ObsoleteTileConverter("westerosblocks:bed_block_3"));
    	list.add(new ObsoleteTileConverter("westerosblocks:bed_block_4"));
    	list.add(new ObsoleteTileConverter("westerosblocks:bed_block_5"));
    	list.add(new ObsoleteTileConverter("westerosblocks:bed_block_6"));
    	list.add(new ObsoleteTileConverter("westerosblocks:bed_block_7"));
    	list.add(new ObsoleteTileConverter("westerosblocks:bed_block_8"));
    	list.add(new ObsoleteTileConverter("westerosblocks:wctileentitysound"));
    	list.add(new ObsoleteTileConverter("minecraft:opframetileentity"));
    	list.add(new ObsoleteTileConverter("minecraft:statues_playertileentity"));
    	list.add(new ObsoleteTileConverter("minecraft:statues_tileentity"));
    	
    	return new EntityListConverter("TileEntities", list);
    }

}
