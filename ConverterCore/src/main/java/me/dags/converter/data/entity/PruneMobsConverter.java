package me.dags.converter.data.entity;

import me.dags.converter.block.BlockState;
import me.dags.converter.converter.DataConverter;
import me.dags.converter.data.EntityConverter;
import me.dags.converter.data.EntityDataConverter;
import me.dags.converter.registry.RemappingRegistry;
import me.dags.converter.util.Utils;

import java.util.List;

import org.jnbt.CompoundTag;

public class PruneMobsConverter implements EntityConverter {
    public PruneMobsConverter() {
    }

    @Override
    public String getId() {
        return "";
    }

	@Override
	public CompoundTag convert(CompoundTag data) {
		if (data.getFloatTag("Health").isPresent()) {
			System.out.println("Stripped " + data.getString("id"));
			// Prune items with health (living entities)
			return null;
		}
		return data;
	}
}
