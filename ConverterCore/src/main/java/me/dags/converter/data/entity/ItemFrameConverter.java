package me.dags.converter.data.entity;

import me.dags.converter.block.BlockState;
import me.dags.converter.converter.DataConverter;
import me.dags.converter.data.EntityConverter;
import me.dags.converter.data.EntityDataConverter;
import me.dags.converter.registry.RemappingRegistry;
import me.dags.converter.util.Utils;

import java.util.List;

import org.jnbt.CompoundTag;

public class ItemFrameConverter implements EntityConverter {

	private final RemappingRegistry<BlockState> registry;
	
    public ItemFrameConverter(RemappingRegistry<BlockState> registry) {
    	this.registry = registry;
    }

    @Override
    public String getId() {
        return "minecraft:item_frame";
    }

    private static final byte[] facingmap = { 3, 4, 2, 5 };
	@Override
	public CompoundTag convert(CompoundTag data) {
		//System.out.println("item_frame.convert(" + data.toString() + ")");
		CompoundTag item = data.getCompound("Item");
		if (item != null) {
			String id = item.getString("id");
			Short meta = item.getShort("Damage");
			//System.out.println("   " + id + ":" + meta);
		}
		data.put("Facing", facingmap[data.getByte("Facing")]);
		return data;
	}
}
