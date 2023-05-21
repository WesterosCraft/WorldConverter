package me.dags.converter.data.entity;

import me.dags.converter.block.BlockState;
import me.dags.converter.converter.DataConverter;
import me.dags.converter.data.EntityConverter;
import me.dags.converter.data.EntityDataConverter;
import me.dags.converter.registry.RemappingRegistry;
import me.dags.converter.util.Utils;

import java.util.List;

import org.jnbt.CompoundTag;

public class UUIDConverter implements EntityConverter {
    public UUIDConverter() {
    }

    @Override
    public String getId() {
        return "";
    }

	@Override
	public CompoundTag convert(CompoundTag data) {
		int[] uuid = new int[4];
		Long uuidleast = data.getLong("UUIDLeast");
		if (uuidleast != null) {
			uuid[2] = (int)(0xFFFFFFFFL & (uuidleast.longValue() >> 32));
			uuid[3] = (int)(0xFFFFFFFFL & uuidleast.longValue());
		}
		Long uuidmost = data.getLong("UUIDMost");
		if (uuidmost != null) {
			uuid[0] = (int)(0xFFFFFFFFL & (uuidmost.longValue() >> 32));
			uuid[1] = (int)(0xFFFFFFFFL & uuidmost.longValue());
		}
		data.put("UUID", uuid);
		return data;
	}
}
