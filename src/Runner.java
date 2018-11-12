import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Scanner;

public class Runner {
    public static void run(String args[]) {
        try {
            String inputFile;
            if (args.length >= 1) {
                inputFile = args[0];
            } else {
                throw new IllegalArgumentException("Input file not provided");
            }
            try {
                Scanner scanner = new Scanner(new File(inputFile));
                HashMap<String, HeapNode> keywordToNodeMapping = new HashMap<>();
                HashMap<HeapNode, String> nodeToKeywordMapping = new HashMap<>();
                Heap h = new Heap();
                File file = new File("output_file.txt");
                PrintWriter fr = new PrintWriter(file);
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    int cond = parseLine(line);
                    switch (cond) {
                        case 1:
                            String tokens[] = line.split("\\s+");
                            String keyword = null;
                            int frequency = 0;
                            if (tokens.length == 2) {
                                if (tokens[0].startsWith("$")) {
                                    keyword = tokens[0].substring(1);
                                }
                                frequency = Integer.parseInt(tokens[1]);
                            }

                            //System.out.println(keyword);
                            HeapNode node;
                            if (!keywordToNodeMapping.containsKey(keyword)) {
                                node = h.insert(frequency);
                                keywordToNodeMapping.put(keyword, node);
                                nodeToKeywordMapping.put(node, keyword);
                            } else {
                                node = keywordToNodeMapping.get(keyword);
                                h.increaseKey(node, frequency);
                            }
                            break;
                        case 2:
                            int numberOfTopKeywords = Integer.min(Integer.parseInt(line), h.nodes);
                            HeapNode[] maxNodes = new HeapNode[numberOfTopKeywords];
                            for (int i = 0; h.max != null && i < numberOfTopKeywords; i++) {
                                HeapNode max = h.extractMax();
                                String key = nodeToKeywordMapping.get(max);
                                if (i < numberOfTopKeywords - 1) {
                                    fr.printf("%s,", key);
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
                System.out.println("Input file error! File not found");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Input file not provided");
        }
    }

    public static int parseLine(String line) {
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
    }
}
