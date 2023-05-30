package me.dags.converter.registry;

import me.dags.converter.biome.Biome;
import me.dags.converter.block.BlockState;
import me.dags.converter.util.Utils;
import me.dags.converter.util.log.Logger;
import me.dags.converter.version.Version;

import java.io.InputStream;
import java.text.ParseException;
import java.util.Map;
import java.util.Scanner;

public class Mapper<T extends RegistryItem<T>> implements Registry.Mapper<T> {

    private final String version;
    private final Map<T, T> mappings;
    private final Registry<T> registry;

    private Mapper(String version, Registry<T> toReg, Map<T, T> mappings) {
        this.version = version;
        this.registry = toReg;
        this.mappings = mappings;
    }

    @Override
    public T apply(T in) {
        T out = mappings.get(in);
        if (out == null) {
            int outId = registry.getId(in);
            out = registry.getValue(outId);
        }
        return out;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public Registry<T> getOutRegistry()
    {
    	return registry;
    }

    public static <T extends RegistryItem<T>> Registry.Mapper<T> identity(Version version, final Registry<T> toreg) {
        return new Registry.Mapper<T>() {
            @Override
            public T apply(T in) {
                return in;
            }
            @Override
            public Registry<T> getOutRegistry() {
            	return toreg; 
            }
            @Override
            public String getVersion() {
                return version.getVersion();
            }
        };
    }

    public static <T extends RegistryItem<T>> Builder<T> builder(Registry<T> from, Registry<T> to) {
        return new Builder<>(from, to);
    }

    public static class Builder<T extends RegistryItem<T>> {

        private final Registry<T> from;
        private final Registry<T> to;
        private final Map<T, T> mappings = Utils.newMap(BlockState.LEGACY_MAX_ID);

        private Builder(Registry<T> from, Registry<T> to) {
            this.from = from;
            this.to = to;
        }

        public boolean hasMappings() {
            return mappings.size() > 0;
        }

        public Builder<T> parse(InputStream inputStream) throws ParseException {
            try (Scanner scanner = new Scanner(inputStream)) {
                while (scanner.hasNext()) {
                    parse(scanner.nextLine());
                }
            }
            return this;
        }

        public Builder<T> parse(String rule) throws ParseException {
            if (rule.startsWith("#")) {
                return this;
            }

            String[] args = rule.split("->");
            if (args.length != 2) {
                return this;
            }

            String in = args[0].trim();
            String out = args[1].trim();
            return parse(in, out);
        }

        public Builder<T> parse(String in, String out) throws ParseException {
            try {
                T from = this.from.getParser().parse(in).parseExtended(in);
                T to = this.to.getParser().parse(out).parseExtended(out);
                if (mappings.putIfAbsent(from, to) != null) {
                	Logger.log("Duplicate mapping: " + in + " -> " + out + " versus " + mappings.get(from) + " - overriding existing");
                	mappings.put(from, to);
                }
                //Logger.log("Parsed Mapping:", in, "->", out);
            } catch (ParseException ignored) {
            	Logger.log("Parsing Exception: line='" + in + " -> " + out + ", err=" + ignored);
            }
            return this;
        }

        public Mapper<T> build() {
            String version = from.getVersion() + "-" + to.getVersion();
            return new Mapper<>(version, to, Utils.newMap(mappings));
        }
    }
}
