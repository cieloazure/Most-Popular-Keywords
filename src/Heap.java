import java.util.LinkedList;
import java.util.Queue;

public class Heap {
    public static final int MAX_DEGREE = 10;
    public HeapNode max;
    public int nodes;

    public Heap() {
        this.max = null;
        this.nodes = 0;
    }

    // Insert a value in new node
    public HeapNode insert(int frequency) {
        // Create a node and initialize the variables
        HeapNode newNode = new HeapNode(frequency);

        // Insert in root list
        this.max = insertInList(max, newNode);

        // If newNode has greater frequency
        if (newNode.frequency > max.frequency) {
            this.max = newNode;
        }

        // Increment the nodes
        this.nodes++;
        return newNode;
    }

    private HeapNode insertInList(HeapNode head, HeapNode newNode) {
        if (head == null) {
            // Create a root list for H containing just the x
            head = newNode;
            head.leftSibling = newNode;
            head.rightSibling = newNode;
        } else {
            // Insert x into H's root list
            if (head.rightSibling == head) {
                // head is the only node in the list
                head.rightSibling = newNode;
                newNode.leftSibling = head;

                head.leftSibling = newNode;
                newNode.rightSibling = head;
            } else {
                // There are other nodes than head
                HeapNode rightNode = head.rightSibling;
                head.rightSibling = newNode;
                newNode.leftSibling = head;

                newNode.rightSibling = rightNode;
                rightNode.leftSibling = newNode;
            }

        }
        return head;
    }

    public void increaseKey(HeapNode node, int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount should be greater than 0");
        }

        node.frequency += amount;
        HeapNode parent = node.parent;
        if (parent != null && node.frequency > parent.frequency) {
            cut(node, parent);
            cascadingCut(parent);
        }
        if (node.frequency > this.max.frequency) {
            this.max = node;
        }
    }

    private void cut(HeapNode node, HeapNode parent) {
        HeapNode childListHead = parent.child;
        childListHead = removeFromList(childListHead, node);
        // Update parent child list
        parent.child = childListHead;
        // Update node
        node.parent = null;
        node.mark = false;

        // insert into top level list
        this.max = insertInList(this.max, node);
    }

    private void cascadingCut(HeapNode node) {
        HeapNode parent = node.parent;
        if (node != null) {
            if (!node.mark) {
                node.mark = true;
            } else {
                if (parent != null) {
                    cut(node, parent);
                    cascadingCut(parent);
                }
            }
        }
    }

    private HeapNode removeFromList(HeapNode head, HeapNode node) {
        if (node == null || (head.rightSibling == head && head == node)) {
            head = null;
        } else {
            HeapNode rightSibling = node.rightSibling;
            HeapNode leftSibling = node.leftSibling;

            rightSibling.leftSibling = leftSibling;
            leftSibling.rightSibling = rightSibling;
        }
        if (node == head) {
            head = head.rightSibling;
        }
        return head;
    }

    public HeapNode extractMax() {
        HeapNode z = this.max;
        if (z != null) {
            HeapNode childListHead = z.child;
            while (childListHead != null) {
                HeapNode temp = childListHead;
                childListHead = removeFromList(childListHead, temp);
                z.child = childListHead;
                temp.parent = null;
                this.max = insertInList(this.max, temp);
            }

            this.max = removeFromList(this.max, z);
            this.nodes--;

            if (z == z.rightSibling) {
                // Just one node and that is extracted now
                this.max = null;
            } else {
                // pairwise combine
                this.max = z.rightSibling;
                consolidate();
            }
            return z;
        }
        return null;
    }

    public void consolidate() {
        // Initialize the degree table
        HeapNode[] degreeTable = new HeapNode[MAX_DEGREE];


        HeapNode iterNode = this.max;
        int nodes = countNodes(this.max);

        while (nodes > 0) {
            HeapNode x = iterNode;
            int degree = x.degree;
            // Pairwise combine
            while (degree < MAX_DEGREE && degreeTable[degree] != null) {
                HeapNode y = degreeTable[degree];
                if (x.frequency < y.frequency) {
                    HeapNode temp = y;
                    y = x;
                    x = temp;
                }
                heapLink(y, x);
                degreeTable[degree] = null;
                degree = degree + 1;
            }
            degreeTable[degree] = x;
            iterNode = x.rightSibling;
            nodes--;
        }

        // Set root level list
        this.max = null;
        for (int i = 0; i < MAX_DEGREE; i++) {
            if (degreeTable[i] != null) {
                this.max = insertInList(this.max, degreeTable[i]);
                if (degreeTable[i].frequency > this.max.frequency) {
                    this.max = degreeTable[i];
                }
            }
        }
    }

    public void heapLink(HeapNode small, HeapNode big) {
        // remove small from root list
        this.max = removeFromList(this.max, small);

        // make small child of big
        // insert small into child list of big
        HeapNode childListHead = big.child;
        childListHead = insertInList(childListHead, small);
        big.child = childListHead;
        small.parent = big;

        //Increment degree
        big.degree++;

        // set childcut field to false
        small.mark = false;
    }

    // Insert the node
    public HeapNode insert(HeapNode node) {
        node.parent = null;
        node.child = null;
        node.degree = 0;
        node.mark = false;
        this.max = insertInList(this.max, node);

        if (node.frequency > this.max.frequency) {
            this.max = node;
        }
        this.nodes++;
        return node;
    }

    private int countNodes(HeapNode head) {
        int count = 1;
        HeapNode iter = head.rightSibling;
        while (iter != head) {
            count++;
            iter = iter.rightSibling;
        }
        return count;
    }

    public void printHeap() {
        HeapNode head = this.max;
        Queue<HeapNode> printStack = new LinkedList<>();
        HeapNode iter = head;
        while (iter != null) {
            int nodes = countNodes(iter);
            int count = nodes;
            while (nodes > 0) {
                if (nodes == count) {
                    System.out.printf("%d", iter.frequency);
                } else {
                    System.out.printf("->%d", iter.frequency);
                }
                iter = iter.rightSibling;
                printStack.add(iter);
                nodes--;
            }
            iter = printStack.remove().child;
            while (iter == null && !printStack.isEmpty()) {
                iter = printStack.remove().child;
            }
        }
    }

}
