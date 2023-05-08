package me.dags.converter.biome.convert;

import me.dags.converter.biome.Biome;
import me.dags.converter.converter.DataConverter;
import me.dags.converter.registry.RemappingRegistry;
import me.dags.converter.version.Version;
import org.jnbt.Tag;

public class BiomeConverter implements DataConverter {

    private final long seed;
    private final Version versionIn;
    private final Version versionOut;
    private final RemappingRegistry<Biome> registry;

    public BiomeConverter(long seed, Version versionIn, Version versionOut, RemappingRegistry<Biome> registry) {
        this.seed = seed;
        this.registry = registry;
        this.versionIn = versionIn;
        this.versionOut = versionOut;
    }

    @Override
    public boolean isOptional() {
        return false;
    }

    @Override
    public String getInputKey() {
        return "Biomes";
    }

    @Override
    public String getOutputKey() {
        return "Biomes";
    }

    @Override
    public Tag<?> convert(Tag<?> tag) {
        BiomeContainer.Reader reader = versionIn.getBiomeFormat().newReader(seed, tag);
        BiomeContainer.Writer writer = versionOut.getBiomeFormat().newWriter();
        int step = writer.stepSize();
        int[] accum = new int[step * step * step];
        for (int by = 0; by < writer.sizeY(); by += step) {
            for (int bz = 0; bz < writer.sizeZ(); bz += step) {
                for (int bx = 0; bx < writer.sizeX(); bx += step) {
                	for (int x = 0; x < step; x++) {
                    	for (int y = 0; y < step; y++) {
                        	for (int z = 0; z < step; z++) {
                    			accum[(x * step * step) + (y * step) + z] = reader.get(bx + x, by + y, bz + z);
                        	}
                    	}
                	}
                	int max_count = 0;
                	int mostfreq = 0;
                	for (int i = 0; i < accum.length; i++) {
            	         int count = 0;
            	         for (int j = 0; j < accum.length; j++) {
            	            if (accum[i] == accum[j]) {
            	               count++;
            	            }
            	         }
            	         if (count > max_count) {
            	            max_count = count;
            	            mostfreq = accum[i];
            	         }
                	}
                    Biome in = registry.getInput(mostfreq);
                    Biome out = registry.getOutput(in);
                    writer.set(bx, by, bz, out.getId());
                }
            }
        }
        return writer.getTag();
    }
}
