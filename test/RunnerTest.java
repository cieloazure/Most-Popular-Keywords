import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;


class RunnerTest {

    @Test
    void runOnGivenInputFile() {
        File f = new File("input.txt");
        Runner.run(new String[] {f.toString()});
    }

    @Test
    void runOnAFileWithOneMillionRecords() {
        File f = createTestFile();
        // f.deleteOnExit();
        Runner.run(new String[] {f.toString()});
    }

    @Test
    void runOnAFileWithDuplicatedRecords() {
        File f = createTestFile2();
        // f.deleteOnExit();
        Runner.run(new String[] {f.toString()});
    }

    @Test
    void runOnAIncorrectFormatFile(){
        File f = createTestFile3();
        Runner.run(new String[] {f.toString()});
        File f1 = createTestFile4();
        Runner.run(new String[] {f1.toString()});
        File f2 = createTestFile5();
        Runner.run(new String[] {f2.toString()});
    }

    @Test
    void runWithoutAFile() {
        Runner.run(new String[] {});
    }

    @Test
    void runWithAAbsentFile() {
        Runner.run(new String[] {"not_present.txt"});
    }

    @Test
    void runWithIncorrectExtension() {
        Runner.run(new String[] {"hello.c"});
    }

    @Test
    void runMoreOutputThanInput() {
        File f = createTestFile6();
        Runner.run(new String[] {f.toString()});
    }

    @Test
    void runWithOutputEntriesInterspersedWithInput() {
        File f = createTestFile7();
        Runner.run(new String[] {f.toString()});
    }


    private File createTestFile() {
        try {

            File file = new File("test_input.txt");
            PrintWriter fr = new PrintWriter(file);
            Random generator = new Random();

            for(int i = 0; i < 1000000; i++){
                RandomString sgenerator = new RandomString(Math.abs(generator.nextInt(10)) + 10, ThreadLocalRandom.current());
                fr.printf("$%s %d\n", sgenerator.nextString(), Math.abs(generator.nextInt(1000) + 1));
            }
            fr.printf("%d\n",20);
            fr.printf("%s","stop");
            fr.close();
            return file;
        } catch(IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private File createTestFile2() {
        try {

            File file = new File("test_input.txt");
            PrintWriter fr = new PrintWriter(file);
            Random generator = new Random();

            for(int i = 0; i < 1000000; i++){
                RandomString sgenerator = new RandomString(Math.abs(generator.nextInt(10)) + 1, ThreadLocalRandom.current());
                fr.printf("$%s %d\n", sgenerator.nextString(), Math.abs(generator.nextInt(Integer.MAX_VALUE) + 1));
            }
            fr.printf("%d\n",20);
            fr.printf("%s","stop");
            fr.close();
            return file;
        } catch(IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private File createTestFile3() {
        try {

            File file = new File("test_input.txt");
            PrintWriter fr = new PrintWriter(file);
            Random generator = new Random();

            for(int i = 0; i < 1000; i++){
                RandomString sgenerator = new RandomString(Math.abs(generator.nextInt(10)) + 10, ThreadLocalRandom.current());
                fr.printf("%s %d\n", sgenerator.nextString(), Math.abs(generator.nextInt(1000) + 1));
            }
            fr.printf("%d\n",20);
            fr.printf("%s","stop");
            fr.close();
            return file;
        } catch(IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private File createTestFile4() {
        try {

            File file = new File("test_input.txt");
            PrintWriter fr = new PrintWriter(file);
            Random generator = new Random();

            for(int i = 0; i < 1000; i++){
                RandomString sgenerator = new RandomString(Math.abs(generator.nextInt(10)) + 10, ThreadLocalRandom.current());
                fr.printf("$%s %d\n", sgenerator.nextString(), Math.abs(generator.nextInt(1000) + 1));
            }
            fr.printf("%d %d\n",20,30);
            fr.printf("%s","stop");
            fr.close();
            return file;
        } catch(IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private File createTestFile5() {
        try {

            File file = new File("test_input.txt");
            PrintWriter fr = new PrintWriter(file);
            Random generator = new Random();

            for(int i = 0; i < 1000; i++){
                RandomString sgenerator = new RandomString(Math.abs(generator.nextInt(10)) + 10, ThreadLocalRandom.current());
                fr.printf("$%s %d\n", sgenerator.nextString(), Math.abs(generator.nextInt(1000) + 1));
            }
            fr.printf("%d\n",20);
            fr.printf("%s","exit");
            fr.close();
            return file;
        } catch(IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private File createTestFile6() {
        try {

            File file = new File("test_input.txt");
            PrintWriter fr = new PrintWriter(file);
            Random generator = new Random();

            for(int i = 0; i < 10; i++){
                RandomString sgenerator = new RandomString(Math.abs(generator.nextInt(10)) + 10, ThreadLocalRandom.current());
                fr.printf("$%s %d\n", sgenerator.nextString(), Math.abs(generator.nextInt(1000) + 1));
            }
            fr.printf("%d\n",20);
            fr.printf("%s","stop");
            fr.close();
            return file;
        } catch(IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private File createTestFile7() {
        try {

            File file = new File("test_input.txt");
            PrintWriter fr = new PrintWriter(file);
            Random generator = new Random();

            String[] toBeRepeated = new String[] {"facebook","amazon","google","apple"};

            for(int i = 0; i < 10000; i++){
                RandomString sgenerator = new RandomString(Math.abs(generator.nextInt(10)) + 1, ThreadLocalRandom.current());
                fr.printf("$%s %d\n", sgenerator.nextString(), Math.abs(generator.nextInt(1000) + 1));
                if(generator.nextBoolean()){
                    fr.printf("%d\n", generator.nextInt(20) + 1);
                }

                if(generator.nextBoolean()) {
                    fr.printf("$%s %d\n", toBeRepeated[generator.nextInt(toBeRepeated.length)], Math.abs(generator.nextInt(1000) + 1));
                }
            }
            fr.printf("%s","stop");
            fr.close();
            return file;
        } catch(IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}