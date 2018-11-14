import java.util.LinkedList;
import java.util.Queue;

public class FibonacciHeap {

    public static final int MAX_DEGREE = 50;

    public HeapNode max; /* Field to point to maximum element in the heap */
    public int nodes; /* Field to keep track of number of nodes in the heap */

    /**
     * Initialize the variables
     */
    public FibonacciHeap() {
        this.max = null;
        this.nodes = 0;
    }

    /**
     * Insert a frequency node in the max heap. The method creates a new node with specified frequency and inserts
     * the node in the top level list of the fibonacci heap. A node corresponding to a keyword is created just once
     * If a keyword appears again in the input, the same node's frequency will be increased and the structure of the
     * fibonacci heap will be altered. The complexity of this function is O(1).
     *
     * @param frequency The number of times the keyword appears in the system
     * @return          The reference to the node corresponding to a keyword is returned
     */
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

    /**
     * A private function to insert a node in a circular doubly linked list. It returns the inserted node.
     *
     * @param head    The head node of the list
     * @param newNode The node to be inserted
     * @return        The newly inserted node in the list
     */
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

    /**
     * An operation to increase the frequency of the node to a new number. Increasing a frequency of the node may
     * violate the max heap property hence the node is severed from it's parent and inserted in a top level list.
     * In order to get a better amortized complexity a mark<code/> field is maintained on each node to track its
     * history. If a node loses one child the mark field is set to true otherwise it stays
     * false. If a node loses a second child the node is severed from it's parent as well and a cascading cut will take
     * place in this way.
     *
     * @param node    The node whose frequency is to be increased
     * @param amount  Amount by which the frequency is to be increased
     */
    public void increaseKey(HeapNode node, int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount should be greater than 0");
        }

        node.frequency += amount; /* Increase the frequency of the node by a amount */
        HeapNode parent = node.parent;
        if (parent != null && node.frequency > parent.frequency) {  /* Check if the node violates the max heap property */
            cut(node, parent); /* Remove the node and put it in the root level list */
            cascadingCut(parent); /* Check if the parent has lost second child and initiate a cascading cut */
        }
        if (node.frequency > this.max.frequency) {  /* Check if the node has greater frequency than the old max */
            this.max = node;
        }
    }

    /**
     * A private function to cut the node from it's parent and insert it into root level list
     *
     * @param node      The node to be severed from it's parent and inserted into root level list
     * @param parent    The parent of the node from which it is severed
     */
    private void cut(HeapNode node, HeapNode parent) {
        /* Get the child list of the parent from which the node is to be removed */
        HeapNode childListHead = parent.child;

        /*Remove the node from the above list and get a modified child list*/
        childListHead = removeFromList(childListHead, node);

        /* Update parent child list pointer of the parent to point to modified list*/
        parent.child = childListHead;

        /* Update node values to reflect being present in the top level list*/
        node.parent = null;
        node.mark = false;

        /* Insert into node into top level list */
        this.max = insertInList(this.max, node);
    }

    /**
     * A private function which initiates a cascading cut and makes changes in a node's mark if they have
     * already lost a child. This functions is called on a parent of the node. If the parent has lost it's first child
     * the mark of the parent will be set to true and no further cascading cut will take place. If the
     * parent has lost it's second child the node will be cut from the parent and a recursive call to cascading cut
     * will be called using the node's parent.
     *
     * @param node The node, usually the parent of an already cut node
     */
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

    /**
     * A private function to remove a node from a circular doubly linked list. Returns the newly modified list. If there are no more
     * nodes remaining it will return null.
     *
     * @param head
     * @param node
     * @return
     */
    private HeapNode removeFromList(HeapNode head, HeapNode node) {
        if (node == null || (head.rightSibling == head && head == node)) {
            /* Only one node in the list*/
            head = null;
        } else {
            /* Multiple nodes */
            HeapNode rightSibling = node.rightSibling;
            HeapNode leftSibling = node.leftSibling;

            rightSibling.leftSibling = leftSibling;
            leftSibling.rightSibling = rightSibling;
        }
        if (node == head) {
            /* When the node points to the head of the list, we need to update the head*/
            head = head.rightSibling;
        }
        return head;
    }

    /**
     * This method removes the maximum node of the Fibonacci heap and returns that node. When the node is removed, the
     * children of the node are added to the root level list. The children are then all pairwise combined in consolidate
     * method.
     *
     * @return The maximum node removed
     */
    public HeapNode extractMax() {
        HeapNode z = this.max;
        if (z != null) {
            /* Remove the children of the max from the child list and add them in root level list*/
            HeapNode childListHead = z.child;
            while (childListHead != null) {
                HeapNode temp = childListHead;
                childListHead = removeFromList(childListHead, temp);
                z.child = childListHead;
                temp.parent = null;
                this.max = insertInList(this.max, temp);
            }

            /* Remove the max node  from root level list*/
            this.max = removeFromList(this.max, z);
            this.nodes--;

            if (z == z.rightSibling) {
                /* Just one node and that is extracted now */
                this.max = null;
            } else {
                /* pairwise combine */
                this.max = z.rightSibling;
                consolidate();
            }
            return z;
        }
        return null;
    }

    /**
     * This method is responsible of pairwise combining of nodes based on degree field. It uses a degree table
     * of MAX_DEGREE size whose upper bound is O(lg n) in the number of nodes expected in the heap.
     */
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

    /**
     * Links a node whose frequency is smaller to a node whose frequency is greater. The small node becomes the
     * child of the big node.
     *
     * @param small Lesser frequency node to be made child of big
     * @param big   Higher frequency node to whom small is to be linked
     */
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

    /**
     * Insert a node without creating a new node. This method is useful when an already extracted node is to be inserted back
     * into the heap. The node will be inserted in the root level list and takes O(1) time.
     *
     * @param node The already created node which was removed earlier is now inserted back into the heap.
     * @return      The node when it is inserted into the root level list
     */
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

    /**
     * Count the number of nodes in circular doubly linked list
     *
     * @param head
     * @return The number of nodes present in the list
     */
    private int countNodes(HeapNode head) {
        int count = 1;
        HeapNode iter = head.rightSibling;
        while (iter != head) {
            count++;
            iter = iter.rightSibling;
        }
        return count;
    }

    /**
     * Method to print the list to standard output
     */
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
