package com.fabianbg.ten_pin_bowling.application.util;

import java.io.IOException;
import java.util.stream.Stream;

public interface IFileUtils {

    public Stream<String> readFile(String path) throws IOException;
}