package com.cultofcthulhu.projectallocation;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
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

    public void writeCSV(List<String[]> lines) throws IOException {
        File writeTo = new File("projects.txt");
        if(writeTo.createNewFile()) {
            System.out.println("Created file");
        }
        else {
            System.out.println("File already exists");
        }

        FileWriter writer = new FileWriter("projects.txt");
        for(String[] line : lines) {
            writer.write(Arrays.toString(line));
            writer.write("\n");
        }
        writer.close();
        System.out.println("Wrote to file");
    }
}

