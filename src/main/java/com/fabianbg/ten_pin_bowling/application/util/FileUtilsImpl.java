package com.fabianbg.ten_pin_bowling.application.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class FileUtilsImpl implements IFileUtils {

    @Override
    public Stream<String> readFile(String path) throws IOException {
        return Files.lines(Paths.get(path));
    }
}