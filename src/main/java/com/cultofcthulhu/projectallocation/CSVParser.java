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
        File writeTo = new File("files/projects.txt");
        if(writeTo.createNewFile()) {
            System.out.println("Created file");
        }
        else {
            System.out.println("File already exists");
        }

        FileWriter writer = new FileWriter("files/projects.txt");
        for(String[] line : lines) {
            String string = Arrays.toString(line);
            string = string.substring(1, string.length()-1);
            writer.write(string);
            writer.write("\n");
        }
        writer.close();
        System.out.println("Wrote to file");
    }
}

