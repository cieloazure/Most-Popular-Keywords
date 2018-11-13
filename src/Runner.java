import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Scanner;

public class Runner {
    /**
     * The function to run the application of the popular keyword. This method will parse each line in the file and take
     * appropriate action. There are 3 actions that may be performed:
     * 1. A keyword with a frequency: Add a node corressponding to that keyword in the heap and track it's frequency
     * 2. Get most popular keywords at any time
     * 3. Stop the execution
     *
     * @param args Should have a valid file in txt format
     */
    public static void run(String args[]) {
        String usageString = "USAGE: java KeywordCounter [input file]";
        String fileInputFormat = "FILE FORMAT: $[keyword] [frequency]\n......\n[number of popular keyword at that instance]\n......\nstop\n";
        try {
            String inputFile;
            if (args.length >= 1) {
                inputFile = args[0];
            } else {
                throw new IllegalArgumentException("[Error]: Input file not provided." + usageString);
            }
            try {

                Scanner scanner = new Scanner(new File(inputFile));
                /* Hash table for finding a node for a specific keyword */
                HashMap<String, HeapNode> keywordToNodeMapping = new HashMap<>();
                /* Hash table for finding keyword from the node */
                HashMap<HeapNode, String> nodeToKeywordMapping = new HashMap<>();
                FibonacciHeap h = new FibonacciHeap();
                File file = new File("output_file.txt");
                PrintWriter fr = new PrintWriter(file);

                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    int cond = parseLine(line);
                    switch (cond) {
                        case 1:
                            /*A keyword appears in the file along with its frequency*/

                            String tokens[] = line.split("\\s+");
                            String keyword = null;
                            int frequency = 0;
                            if (tokens.length == 2) {
                                if (tokens[0].startsWith("$")) {
                                    keyword = tokens[0].substring(1);
                                }
                                frequency = Integer.parseInt(tokens[1]);
                            }

                            HeapNode node;
                            if (!keywordToNodeMapping.containsKey(keyword)) {
                                /* New keyword appears */
                                /* Insert the keyword in heap */
                                node = h.insert(frequency);
                                keywordToNodeMapping.put(keyword, node);
                                nodeToKeywordMapping.put(node, keyword);
                            } else {
                                /* Existing keyword appears */
                                /* Increase key of that operation */
                                node = keywordToNodeMapping.get(keyword);
                                h.increaseKey(node, frequency);
                            }
                            break;
                        case 2:
                            int numberOfTopKeywords = Integer.min(Integer.parseInt(line.trim()), h.nodes);
                            HeapNode[] maxNodes = new HeapNode[numberOfTopKeywords];
                            for (int i = 0; h.max != null && i < numberOfTopKeywords; i++) {
                                HeapNode max = h.extractMax();
                                String key = nodeToKeywordMapping.get(max);
                                if (i < numberOfTopKeywords - 1) {
                                    if(h.max == null){
                                        fr.printf("%s", key);
                                    } else{
                                        fr.printf("%s,", key);
                                    }
                                } else {
                                    fr.printf("%s", key);
                                }
                                maxNodes[i] = max;
                            }
                            fr.printf("\n");
                            for (int i = 0; i < maxNodes.length; i++) {
                                if (maxNodes[i] != null) {
                                    h.insert(maxNodes[i]);
                                }
                            }
                            break;
                        case 3:
                            System.out.println("exit");
                            break;
                    }
                }
                fr.close();
                scanner.close();
            } catch (FileNotFoundException e) {
                System.err.println("[ERROR]: Input file error! File not found");
                // e.printStackTrace();
            } catch(IOException e) {
                System.err.println("[ERROR]: File entries are in incorrect format\n"+fileInputFormat);
                // e.printStackTrace();
            }
        } catch(IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }

    private static int parseLine(String line) throws IOException{
        if (isValid(line)) {
            if (line.charAt(0) == '$') {
                // line starts with '$'
                return 1;
            } else if (line.contains("stop")) {
                // line says 'stop'
                return 3;
            } else {
                // just a number
                return 2;
            }
        }else {
            throw new IOException("File format invalid");
        }
    }

    private static boolean isValid(String line) {
        if(line.matches("^\\$[a-zA-Z0-9!@#$%^&*()_+\\-={}\\[\\]|:;\"'<>,.?/]+\\s+[0-9]+$" ) || line.matches("^\\s*[0-9]+\\s*$") || line.matches("^\\s*stop\\s*$")){
            return true;
        }else{
            return false;
        }
    }
}
