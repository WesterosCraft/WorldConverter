package me.dags.converter.biome.convert;

import org.jnbt.Nbt;
import org.jnbt.Tag;

public class BiomeContainer115 implements BiomeContainer {
    protected final int[] biomes;

    public BiomeContainer115(int[] biomes) {
        this.biomes = biomes;
    }

    @Override
    public int sizeX() {
        return 16;
    }

    @Override
    public int sizeY() {
        return 256;
    }

    @Override
    public int sizeZ() {
        return 16;
    }
    
    @Override
    public int stepSize() {
    	return 4;
    }

    private static int indexOf(int x, int y, int z) {
        int bx = (x >> 2) & 0x3;
        int by = (y >> 2) & 0x3F;
        int bz = (z >> 2) & 0x3;
        return (by << 4) | (bz << 2) | bx;
    }

    public static class Reader extends BiomeContainer115 implements BiomeContainer.Reader {

        public Reader(int[] biomes) {
            super(biomes);
        }

        @Override
        public int get(int x, int y, int z) {
            int index = indexOf(x, y, z);
            if (index < biomes.length) {
                return biomes[index];
            }
            return 0;
        }
    }

    public static class Writer extends BiomeContainer115 implements BiomeContainer.Writer {

        public Writer() {
            super(new int[4 * 4 * 64]);
        }

        @Override
        public Tag<?> getTag() {
            return Nbt.tag(biomes);
        }

        @Override
        public void set(int x, int y, int z, int biome) {
            int index = indexOf(x, y, z);
            if (index < biomes.length) {
                biomes[index] = biome;
            }
        }
    }
}
