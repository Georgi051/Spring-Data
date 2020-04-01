package com.example.springdataintro.utils;

import org.springframework.stereotype.Component;

import java.io.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Component
public class FileUtilImpl implements FileUtil{

    @Override
    public String[] readFileContent(String filePath) throws IOException {
        File file = new File(filePath);

        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

        Set<String> result = new LinkedHashSet<>();
        String line;
        while ((line = bufferedReader.readLine()) != null){
            if (!"".equals(line)){
                result.add(line);
            }
        }
        return result.toArray(String[]::new);
    }
}
