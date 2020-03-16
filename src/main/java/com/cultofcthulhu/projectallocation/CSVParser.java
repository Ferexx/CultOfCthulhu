package com.cultofcthulhu.projectallocation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVParser {

    public List<String[]> lines = new ArrayList<>();

    public CSVParser(File toParse) {
        String line = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(toParse));
            while((line = br.readLine())!=null) {
                String[] values = line.split(",");
                lines.add(values);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void writeCSV(List<String[]> lines) {

    }
}

