package me.dags.converter;

import me.dags.converter.converter.Converter;
import me.dags.converter.converter.ConverterData;
import me.dags.converter.converter.ReaderFactory;
import me.dags.converter.converter.WriterFactory;
import me.dags.converter.converter.config.Config;
import me.dags.converter.converter.config.CustomData;
import me.dags.converter.converter.directory.DirectoryConverter;
import me.dags.converter.converter.directory.ExtentConverter;
import me.dags.converter.converter.world.ChunkConverter;
import me.dags.converter.converter.world.WorldConverter;
import me.dags.converter.data.entity.ItemFrameConverter;
import me.dags.converter.data.entity.PruneMobsConverter;
import me.dags.converter.datagen.Mappings;
import me.dags.converter.extent.Format;
import me.dags.converter.util.IO;
import me.dags.converter.util.Threading;
import me.dags.converter.util.log.Logger;
import me.dags.converter.version.Version;

import java.awt.*;
import java.io.File;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;
import java.util.function.Consumer;

public class Main {

    private static boolean headless = false;

    public static boolean isHeadless() {
        return headless;
    }

    private static boolean nogui(String[] args) {
        for (String arg : args) {
            if (arg.equalsIgnoreCase("-nogui")) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        try {
            if (IO.isJar()) {
                Logger.add(new PrintStream(IO.logFile()));
            }

            Logger.log("Running from directory:", new File("").getAbsolutePath());
            Logger.log("CPU Cores:", Threading.coreCount());
            Logger.log("Max Memory:", Threading.maxeMemory());
            Logger.flush();

            if (GraphicsEnvironment.isHeadless() || nogui(args)) {
                headless = true;
                HeadlessConverter.run(args);
            } else {
                headless = false;
                GUIConverter.run();
            }
            ChunkConverter.dumpCounters();
            ItemFrameConverter.dumpMissingItemFrames();
            PruneMobsConverter.dumpPrunedEntities();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public static File convert(Config config, Consumer<Float> progress) throws Exception {
        File source = config.input.file;
        if (config.output.file == null) {
        	config.output.file = getDestDir(source);
        }
        File dest = config.output.file;

        Logger.newLine();
        Logger.log("Running with config:");
        Logger.log(" - Input:");
        Logger.log("   + File:   ", config.input.file);
        Logger.log("   + Format: ", config.input.format);
        Logger.log("   + Version:", config.input.version);
        Logger.log(" - Output:");
        Logger.log("   + File:   ", config.output.file);
        Logger.log("   + Format: ", config.output.format);
        Logger.log("   + Version:", config.output.version);
        Logger.log("   + Custom:", config.custom.dataOut.getPath());
        Logger.newLine();

        Version from = config.input.version;
        Version to = config.output.version;
        Format formatIn = config.input.format;
        Format formatOut = config.output.format;

        try {
            if (formatIn == Format.WORLD) {
                return convertWorld(source, dest, from, to, config.custom, progress);
            } else {
                return convertExtent(source, dest, formatIn, formatOut, from, to, config.custom, progress);
            }
        } finally {
            Logger.flush();
        }
    }

    private static File convertWorld(File source, File dest, Version from, Version to, CustomData customData, Consumer<Float> progress) throws Exception {
        WorldConverter world = new WorldConverter(source, customData);
        List<Future<Void>> tasks = world.convert(from, to, dest);
        await(tasks, progress);
        return dest;
    }

    private static File convertExtent(File source, File dest, Format in, Format out, Version from, Version to, CustomData customData, Consumer<Float> progress) throws Exception {
        ReaderFactory reader = in.reader(from);
        WriterFactory writer = out.writer(to);
        Mappings mappings = Mappings.build(from, to).builtIn();
        ConverterData data = ConverterData.create(customData, mappings);
        Converter converter = new ExtentConverter(reader, writer, data, Collections.emptyList());
        List<Future<Void>> tasks = new DirectoryConverter(converter).convert(source, in, dest, out);
        await(tasks, progress);
        return dest;
    }

    private static void await(List<Future<Void>> tasks, Consumer<Float> progress) {
        float total = tasks.size();
        int lastSize = tasks.size();
        while (!tasks.isEmpty()) {
            tasks.removeIf(Future::isDone);
            int size = tasks.size();
            if (size == lastSize) {
                continue;
            }
            lastSize = size;
            progress.accept((total - size) / total);
        }
    }

    private static File getDestDir(File file) {
        File parent = file.getParentFile();
        String name = file.getName();
        if (!file.isDirectory()) {
            int index = name.lastIndexOf('.');
            name = name.substring(0, index);
        }
        return new File(parent, name + "-converted");
    }
}
