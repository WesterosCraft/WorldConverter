package me.dags.converter.data.tile;

import me.dags.converter.data.EntityConverter;
import org.jnbt.CompoundTag;

public class ObsoleteTileConverter implements EntityConverter {
	
	private final String entid;
	
	public ObsoleteTileConverter(String id) {
		entid = id;
	}
	
	@Override
	public String getId() {
		return entid;
	}

	@Override
	public CompoundTag convert(CompoundTag data) {
		return null;
	}
}
