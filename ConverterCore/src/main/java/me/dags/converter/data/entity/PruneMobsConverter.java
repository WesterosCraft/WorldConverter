package me.dags.converter.data.entity;

import me.dags.converter.block.BlockState;
import me.dags.converter.converter.DataConverter;
import me.dags.converter.data.EntityConverter;
import me.dags.converter.data.EntityDataConverter;
import me.dags.converter.registry.RemappingRegistry;
import me.dags.converter.util.Utils;
import me.dags.converter.util.log.Logger;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jnbt.CompoundTag;

public class PruneMobsConverter implements EntityConverter {
	public static HashMap<String, Integer> strippedMobs = new HashMap<String, Integer>();
	private static String[] pruneExceptions = { "minecraft:armor_stand" };
	private static Set<String> pruneExcept = new HashSet<String>(Arrays.asList(pruneExceptions));
	
    public PruneMobsConverter() {
    }

    @Override
    public String getId() {
        return "";
    }

	@Override
	public CompoundTag convert(CompoundTag data) {
		if (data.getFloatTag("Health").isPresent()) {
			// Prune items with health (living entities)
			String id = data.getString("id");
			// If exception, keep it
			if (pruneExcept.contains(id)) return data;
			
			synchronized(strippedMobs) {
				Integer cnt = strippedMobs.get(id);
				strippedMobs.put(id,  (cnt != null) ? (cnt+1) : 1);
			}
			return null;
		}
		return data;
	}
	
	public static void dumpPrunedEntities() {
		Logger.log("pruned entities:");
		for (String k : strippedMobs.keySet()) {
			Logger.log("  " + k + ": " + strippedMobs.get(k));	
		}
		Logger.log("=================================");
		Logger.flush();
	}

}
