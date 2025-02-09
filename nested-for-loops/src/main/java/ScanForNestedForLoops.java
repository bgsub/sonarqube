package org.sonar.nested

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

public class ScanForNestedForLoops {
    private static final Pattern FOR_LOOP_PATTERN = Pattern.compile("\\bfor\\s*\\(.*?;.*?;.*?\\)");
    private static final Pattern PYTHON_FOR_LOOP_PATTERN = Pattern.compile("\\bfor\\s+\\w+\\s+in\\s+.*:");

    public static ArrayList<String> codeContainsNestedLoop(String filePath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        Integer loopStack = 0;
        ArrayList<String> scanResult = new ArrayList<>();

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            Matcher matcher;

            if (filePath.endsWith(".java") || filePath.endsWith(".js") || filePath.endsWith(".ts")) {
                matcher = FOR_LOOP_PATTERN.matcher(line);
            } else if (filePath.endsWith(".py")) {
                matcher = PYTHON_FOR_LOOP_PATTERN.matcher(line);
            }
            else {
                continue;
            }

            if (matcher.find()) {
                loopStack += 1;
                if (loopStack > 1 ) {
                    scanResult.add("Nested 'for' loop found at lines " + (i) + " and " + (i + 1) + " in file " + Paths.get(filePath).getFileName());
                }
            }

            if (line.equals("}")) {
                if (loopStack != 0) {
                    loopStack -= 1;
                }
            }
        }
        return scanResult;
    }

    public static void scanDirectory(String directoryPath) throws IOException {
        Files.walk(Paths.get(directoryPath))
                .filter(Files::isRegularFile)
                .filter(path -> path.toString().endsWith(".java") || path.toString().endsWith(".js") || path.toString().endsWith(".ts") || path.toString().endsWith(".py") || path.toString().endsWith(".txt"))
                .forEach(path -> {
                    try {
                        codeContainsNestedLoop(path.toString());
                    } catch (IOException e) {
                        System.err.println("Error reading file: " + path + " - " + e.getMessage());
                    }
                });
    }
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java ScanForNestedForLoops <source-file-or-directory-path>");
            return;
        }

        File target = new File(args[0]);

        try {
            if (target.isDirectory()) {
                scanDirectory(args[0]);
            } else {
                codeContainsNestedLoop(args[0]);
            }
        } catch (IOException e) {
            System.err.println("Error processing: " + e.getMessage());
        }
    }
}
