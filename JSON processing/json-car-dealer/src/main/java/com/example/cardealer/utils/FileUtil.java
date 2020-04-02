package com.example.cardealer.utils;

import java.io.IOException;

public interface FileUtil {
    String readFileContinent(String filePath) throws IOException;

    void write(String content, String filePath) throws IOException;
}
