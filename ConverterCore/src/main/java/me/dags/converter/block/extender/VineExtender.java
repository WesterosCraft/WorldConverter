package me.dags.converter.block.extender;

import me.dags.converter.block.BlockState;
import me.dags.converter.extent.chunk.Chunk;
import me.dags.converter.registry.Registry;
import org.jnbt.CompoundTag;
import org.jnbt.StringTag;

public class VineExtender implements StateExtender {
    @Override
    public BlockState getExtendedState(BlockState state, Registry.Parser<BlockState> parser, Chunk.Reader chunk, int x, int y, int z) throws Exception {
        CompoundTag properties = state.getData().getCompound("Properties");
        BlockState above = chunk.getState(x, y + 1, z);
        String abovename = above.getBlockName();
        CompoundTag extendedProperties = properties.copy();
        extendedProperties.put("#up", (state.getBlockName().equals(abovename) || "minecraft:air".equals(abovename))?"false":"true");
		//System.out.println(state.getBlockName() + ".extend(" + properties.toString() + "=" + extendedProperties.toString());

        return BlockState.createTransientInstance(state, extendedProperties);
    }
}
