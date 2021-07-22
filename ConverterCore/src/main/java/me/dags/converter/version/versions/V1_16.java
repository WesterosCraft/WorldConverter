package me.dags.converter.version.versions;

import me.dags.converter.version.format.BiomeFormat;

public class V1_16 extends V1_14 {

    @Override
    public int getId() {
        return 2586;
    }

    @Override
    public String getVersion() {
        return "1.16";
    }

    @Override
    public BiomeFormat getBiomeFormat() {
        return BiomeFormat.LATEST;
    }
}
