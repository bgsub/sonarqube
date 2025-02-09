
import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;


import static org.junit.jupiter.api.Assertions.*;

public class ScanForNestedForLoopsTest {
    private Path tempFile;

    @BeforeEach
    void setUp() throws IOException {
        tempFile = Files.createTempFile("test", ".java");
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(tempFile);
    }

    @Test
    void scan_results_should_be_empty_if_no_nested_for_loops() throws IOException {
        String code = "public class Test {\n" +
                "    public void method() {\n" +
                "        for (int i = 0; i < 10; i++) {\n" +
                "            System.out.println(i);\n" +
                "        }\n" +
                "        for (int i = 0; i < 10; i++) {\n" +
                "            System.out.println(i);\n" +
                "        }\n" +
                "    }\n" +
                "}";
        Files.write(tempFile, code.getBytes());

        ArrayList<String> nestedLoopFound = ScanForNestedForLoops.codeContainsNestedLoop(tempFile.toString());;

        assertTrue(nestedLoopFound.isEmpty());
    }

    @Test
    void scan_results_should_show_nested_for_loops_lines() throws IOException {
        String code = "public class Test {\n" +
                "    public void method() {\n" +
                "        for (int i = 0; i < 10; i++) {\n" +
                "            for (int j = 0; j < 5; j++) {\n" +
                "                System.out.println(i + j);\n" +
                "            }\n" +
                "        }\n" +
                "        for (int i = 0; i < 10; i++) {\n" +
                "            for (int j = 0; j < 5; j++) {\n" +
                "                System.out.println(i + j);\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}";

        Path fileName = Paths.get(tempFile.toString()).getFileName();

        String expectedResult_1 = "Nested 'for' loop found at lines 3 and 4 in file " + fileName;
        String expectedResult_2 = "Nested 'for' loop found at lines 8 and 9 in file " + fileName;

        Files.write(tempFile, code.getBytes());
        ArrayList<String> nestedLoopFound = ScanForNestedForLoops.codeContainsNestedLoop(tempFile.toString());;

        assertTrue(nestedLoopFound.contains(expectedResult_1));
        assertTrue(nestedLoopFound.contains(expectedResult_2));

    }

    @Test
    void scan_results_should_show_nested_for_loops_lines_python() throws IOException {
        tempFile = Files.createTempFile("test", ".py");
        String code = "for i in range(10):\n" +
                "    for j in range(5):\n" +
                "        print(i, j)\n";
        Files.write(tempFile, code.getBytes());

        Path fileName = Paths.get(tempFile.toString()).getFileName();


        String expectedResult = "Nested 'for' loop found at lines 1 and 2 in file " + fileName;

        Files.write(tempFile, code.getBytes());
        ArrayList<String> nestedLoopFound = ScanForNestedForLoops.codeContainsNestedLoop(tempFile.toString());

        assertTrue(nestedLoopFound.contains(expectedResult));
    }
}
