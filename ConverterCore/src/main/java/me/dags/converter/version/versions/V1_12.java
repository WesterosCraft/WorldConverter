package me.dags.converter.version.versions;

import me.dags.converter.block.extender.*;
import me.dags.converter.util.map.FastMap;

import java.util.Map;

public class V1_12 extends V1_10 {

    private static final Map<String, StateExtender> STATE_EXTENDERS = new FastMap<String, StateExtender>() {{
        put("minecraft:bed", new BedExtender());
        put("minecraft:standing_banner", new BannerExtender());
        put("minecraft:wall_banner", new BannerExtender());
        put("minecraft:double_plant", new DoublePlantExtender_v1_12());
        put("minecraft:skull", new SkullExtender());
        put("minecraft:flower_pot", new FlowerPotExtender());
        put("minecraft:wooden_door", new DoorExtender());
        put("minecraft:iron_door", new DoorExtender());
        put("minecraft:spruce_door", new DoorExtender());
        put("minecraft:birch_door", new DoorExtender());
        put("minecraft:jungle_door", new DoorExtender());
        put("minecraft:acacia_door", new DoorExtender());
        put("minecraft:dark_oak_door", new DoorExtender());
        put("minecraft:vine", new VineExtender());

        put("conquest:door_oaknowindow", new DoorExtender());
        put("conquest:door_wood1", new DoorExtender());
        put("conquest:door_wood2", new DoorExtender());
        put("conquest:door_elven1", new DoorExtender());
        put("conquest:door_elven2", new DoorExtender());

        put("conquest:planks_brownweathered_door", new DoorExtender());
        put("conquest:planks_purplepainted_door", new DoorExtender());
        put("conquest:planks_lightblueweathered_door", new DoorExtender());
        put("conquest:planks_blueweathered_door", new DoorExtender());
        put("conquest:planks_darkblueweathered_door", new DoorExtender());
        put("conquest:planks_cyanweathered_door", new DoorExtender());
        put("conquest:planks_limeweathered_door", new DoorExtender());
        put("conquest:planks_greenweathered_door", new DoorExtender());
        put("conquest:planks_yellowweathered_door", new DoorExtender());
        put("conquest:planks_lightredweathered_door", new DoorExtender());
        put("conquest:planks_redweathered_door", new DoorExtender());
        put("conquest:planks_whiteweathered_door", new DoorExtender());
        put("conquest:planks_orangepainted_door", new DoorExtender());
        
        put("westerosblocks:door_spruce", new DoorExtender());
        put("westerosblocks:door_weirwood", new DoorExtender());
        put("westerosblocks:door_secret_sandstone_red", new DoorExtender());
        put("westerosblocks:door_locked_oak", new DoorExtender());
        put("westerosblocks:door_locked_spruce", new DoorExtender());
        put("westerosblocks:door_locked_birch", new DoorExtender());
        put("westerosblocks:door_locked_jungle", new DoorExtender());
        put("westerosblocks:door_locked_northern_wood", new DoorExtender());
        
        put("westerosblocks:vine_block_0", new VineExtender());
        put("westerosblocks:vine_block_1", new VineExtender());
        put("westerosblocks:vine_block_2", new VineExtender());
        put("westerosblocks:vine_block_3", new VineExtender());
        put("westerosblocks:vine_block_4", new VineExtender());
        put("westerosblocks:vine_block_5", new VineExtender());

    }};

    @Override
    public int getId() {
        return 1343;
    }

    @Override
    public String getVersion() {
        return "1.12";
    }

    @Override
    protected Map<String, StateExtender> getExtenders() {
        return STATE_EXTENDERS;
    }
}
