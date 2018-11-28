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
        String usageString = "USAGE: java keywordcounter [input file]";
        String fileInputFormat = "FILE FORMAT: $[keyword] [frequency]\n......\n[number of popular keyword at that instance]\n......\nstop\n";
        try {
            String inputFile;

            /* Check if the input file is present in the command line arguments */
            if (args.length >= 1) {
                inputFile = args[0];
            } else {
                throw new IllegalArgumentException("[ERROR]: Input file not provided." + usageString);
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
                    int cond = parseLine(line.toLowerCase());
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
                                /* Insert the keyword in heap and the hash table*/
                                node = h.insert(frequency);
                                keywordToNodeMapping.put(keyword, node);
                                nodeToKeywordMapping.put(node, keyword);
                            } else {
                                /* Existing keyword appears */
                                /* Increase key of that operation  by retrieving the node from the hash table*/
                                node = keywordToNodeMapping.get(keyword);
                                h.increaseKey(node, frequency);
                            }
                            break;
                        case 2:
                            int numberOfTopKeywords = Integer.min(Integer.parseInt(line.trim()), h.nodes);
                            HeapNode[] maxNodes = new HeapNode[numberOfTopKeywords];
                            for (int i = 0;  i < numberOfTopKeywords; i++) {

                                /* Extract max from the heap */
                                HeapNode max = h.extractMax();

                                /* Get the corresponding keyword to the node*/
                                String key = nodeToKeywordMapping.get(max);

                                /* Write the keyword to the file */
                                if (i < numberOfTopKeywords - 1) {
                                    fr.printf("%s,", key);
                                } else {
                                    fr.printf("%s", key);
                                }
                                maxNodes[i] = max;
                            }
                            fr.printf("\n");
                            /* Insert the nodes extracted back in the heap for future tracking */
                            for (int i = 0; i < maxNodes.length; i++) {
                                if (maxNodes[i] != null) {
                                    h.insert(maxNodes[i]);
                                }
                            }
                            break;
                        case 3:
                            /* Stop received */
                            /* Exit the program */
                            System.out.println("exit");
                            break;
                    }
                }
                fr.close();
                scanner.close();
            } catch (FileNotFoundException e) {
                System.err.println("[ERROR]: Input file error! File not found");
            } catch(IOException e) {
                System.err.println(e.getMessage());
                System.err.println("[ERROR]: File entries are in incorrect format\n"+fileInputFormat);
            }
        } catch(IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Parse the line and return a integer corressponding to the action to be taken.
     *
     * @param line The line to be parsed
     * @return The integer corresponding to the action required
     *          1: encountered a keyword with a frequency
     *          2: encountered a query integer
     *          3: encountered "stop" message
     * @throws IOException when the line is not in expected format an exception is thrown
     */
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
            throw new IOException("File format invalid for "+line);
        }
    }

    /**
     * Checks whether a line is valid by checking against the regular expressions
     * @param line the line to be checked
     * @return True if the line is valid and matches a regular expression otherwise false
     */
    private static boolean isValid(String line) {
        if(line.matches("^\\$[a-zA-Z0-9!@#$%^&*()_+\\-={}\\[\\]|:;\"'<>,.?/]+\\s+[0-9]+$" ) || line.matches("^\\s*[0-9]+\\s*$") || line.matches("^\\s*stop\\s*$")){
            return true;
        }else{
            return false;
        }
    }
}
