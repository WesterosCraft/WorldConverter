package me.dags.converter.extent.chunk;

import me.dags.converter.block.BlockState;
import me.dags.converter.data.tile.LazyTileEntityMap;
import me.dags.converter.data.tile.TileEntityMap;
import me.dags.converter.extent.volume.Volume;
import me.dags.converter.extent.volume.latest.AbstractVolumeReader;
import me.dags.converter.util.log.Logger;

import org.jnbt.CompoundTag;
import org.jnbt.ListTag;
import org.jnbt.Nbt;
import org.jnbt.Tag;
import org.jnbt.TagType;

import java.util.List;

public abstract class AbstractChunkReader implements Chunk.Reader {

    private final CompoundTag level;
    private final Volume.Reader[] sections;
    private final TileEntityMap tileEntityMap;
    private final List<Tag<CompoundTag>> sectionData;

    public AbstractChunkReader(CompoundTag root) {
        this.level = root.getCompound("Level");
        this.tileEntityMap = new LazyTileEntityMap(level);
        this.sectionData = level.getListTag("Sections", TagType.COMPOUND).getBacking();
        int maxy = -1;
        // Don't assume packed
        for (Tag<CompoundTag> s : this.sectionData) {
        	int y = s.asCompound().getByteTag("Y").getValue();
        	maxy = Math.max(maxy, (int) y);
        }
        this.sections = new Volume.Reader[maxy+2];	// Include room for y=-1
    }

    @Override
    public int getSectionCount() {
        return (sections.length - 1);
    }

    @Override
    public TileEntityMap getTileEntityMap() {
        return tileEntityMap;
    }

    @Override
    public Volume.Reader getSection(int index) throws Exception {
        Volume.Reader reader = sections[index+1];
        if (reader == null) {
        	// Find the right section, since may not be packed
        	CompoundTag match = null;
            for (Tag<CompoundTag> s : this.sectionData) {
            	int y = s.asCompound().getByteTag("Y").getValue();
            	if (y == index) {
            		match = s.asCompound();;
            		break;
            	}
            }
            if (match != null) {
            	reader = createSection(match);
            }
            else {
            	reader = new EmptySection();
            }
        	sections[index+1] = reader;
        }
        return reader;
    }

    @Override
    public Tag<?> getData(String key) {
        return level.get(key);
    }

    protected abstract Volume.Reader createSection(CompoundTag section) throws Exception;
    
    private static class EmptySection implements Volume.Reader {
    	private EmptySection() {
    	}

		@Override
		public int getWidth() {
			return 16;
		}

		@Override
		public int getHeight() {
			return 16;
		}

		@Override
		public int getLength() {
			return 16;
		}

		@Override
		public BlockState getState(int x, int y, int z) {
			return BlockState.AIR;
		}

		@Override
		public Tag<?> getData(String key) {
			return Nbt.list(TagType.COMPOUND, (Iterable<CompoundTag>) null);
		}
    }

}
