package me.dags.converter.converter.world;

import me.dags.converter.block.BlockState;
import me.dags.converter.converter.Converter;
import me.dags.converter.converter.ConverterData;
import me.dags.converter.converter.DataConverter;
import me.dags.converter.extent.WriterConfig;
import me.dags.converter.extent.chunk.Chunk;
import me.dags.converter.extent.chunk.ChunkData;
import me.dags.converter.extent.volume.Volume;
import me.dags.converter.registry.Registry;
import me.dags.converter.util.log.Logger;
import me.dags.converter.version.Version;
import org.jnbt.CompoundTag;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class ChunkConverter implements Converter {

    private final Version to;
    private final Version from;
    private final ConverterData data;
    private final WriterConfig config;
    private final List<DataConverter> level;
    private final List<DataConverter> section;

    public ChunkConverter(long seed, Version from, Version to, ConverterData data) {
        this.level = ChunkData.getLevelDataConverters(seed, from, to, data);
        this.section = ChunkData.sectionData();
        this.config = new WriterConfig();
        this.data = data;
        this.from = from;
        this.to = to;
        config.put("registry", data.blocks);
    }

    @Override
    public CompoundTag convert(CompoundTag in) throws Exception {
        Chunk.Reader reader = from.getChunkFormat().newReader(data.blocks, in);
        Chunk.Writer writer = to.getChunkFormat().newWriter(to, config);
        try {
            int sections = reader.getSectionCount();
            for (int i = 0; i < sections; i++) {
                Volume.Reader sectionReader = reader.getSection(i);
                Volume.Writer sectionWriter = writer.getSection(i);
                convertSection(i, reader, writer);
                DataConverter.writeData(sectionReader, sectionWriter, section);
            }
            DataConverter.writeData(reader, writer, level);
            return writer.flush();
        } catch (Throwable t) {
            Logger.log(t);
            throw t;
        }
    }
    
    private static class Count { long cnt; }
    public static HashMap<String, Count> counters_by_state = new HashMap<String, Count>();
    
    public static void dumpCounters() {
    	Set<String> keys = counters_by_state.keySet();
    	String[] keylist = keys.toArray(new String[0]);
    	Arrays.sort(keylist);
    	Logger.log("Output block counts by blockName:");
    	for (String key : keylist) {
    		Logger.log(key + ", " + counters_by_state.get(key).cnt);
    	}
    	Logger.flush();
    }

    private void convertSection(int index, Chunk.Reader chunkReader, Chunk.Writer chunkWriter) throws Exception {
        Volume.Reader reader = chunkReader.getSection(index);
        if (reader.size() == 0) {
            return;
        }

        Volume.Writer writer = chunkWriter.getSection(index);

        int blockY = index << 4;
        Registry.Parser<BlockState> parser = data.blocks.getParser();

        // blocks
        for (int y = 0; y < reader.getHeight(); y++) {
            for (int z = 0; z < reader.getLength(); z++) {
                for (int x = 0; x < reader.getWidth(); x++) {
                    BlockState stateIn = reader.getState(x, y, z);
                    BlockState extendedState = stateIn.getExtendedState(parser, chunkReader, x, blockY + y, z);

                    // try convert mappings explicitly for extended states first
                    BlockState stateOut = data.blocks.getOutput(extendedState);
                    if (stateOut == null) {
                        // fall back to non-extended state and try again
                        stateOut = data.blocks.getOutput(stateIn);
                        if (stateOut == null) {
                            throw new NullPointerException("No mapping for state: " + stateIn.getIdentifier());
                        }
                    }
                    if (stateOut != null) {
                        String id = stateOut.getBlockName();
                        if (!id.equals("minecraft:air")) {
                        	Count cnt = counters_by_state.get(id);
                        	if (cnt != null) { 
                        		cnt.cnt += 1; 
                    		}
                        	else { 
                        		cnt = new Count(); 
                        		cnt.cnt = 1;
                        		counters_by_state.put(id, cnt);   
                        	}
                        }
                    }
                    writer.setState(x, y, z, stateOut);
                    if (stateIn.requiresUpgrade()) {
                        chunkWriter.markUpgrade(index, x, y, z);
                    }
                }
            }
        }
    }
}
