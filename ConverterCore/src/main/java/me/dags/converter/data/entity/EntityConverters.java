package me.dags.converter.data.entity;

import me.dags.converter.block.BlockState;
import me.dags.converter.converter.DataConverter;
import me.dags.converter.data.EntityListConverter;
import me.dags.converter.data.entity.painting.PaintingConverter;
import me.dags.converter.registry.RemappingRegistry;
import me.dags.converter.util.Utils;

public class EntityConverters {

    public static DataConverter getDataConverter() {
        return new EntityListConverter("Entities", Utils.listOf(new PaintingConverter()));
    }

    public static DataConverter getNewDataConverter(RemappingRegistry<BlockState> registry) {
        return new EntityListConverter("Entities", Utils.listOf(new PaintingConverter(), new ItemFrameConverter(registry), new UUIDConverter(), new PruneMobsConverter()));
    }
}
