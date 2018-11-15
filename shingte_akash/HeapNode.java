public class HeapNode {
    /**
     *
     * This class represents a node in a Fibonacci Heap. It includes all the fields responsible to maintain the fibonacci heap
     * with mark(childCut) fields in order to obtain amortized times for the Fibonacci heap.
     *
     */
    public HeapNode parent; /* Pointer to parent node */
    public HeapNode child; /* Pointer to child list, points to just one child in a circular doubly linked list*/
    public HeapNode leftSibling; /* Node to left in the list */
    public HeapNode rightSibling; /* Node to the right in the list */
    public int degree; /* Number of nodes in the child list */
    public boolean mark; /* Child cut field to obtain amortized analysis*/
    public int frequency; /* Frequency of the keyword */

    /**
     *  Initialize the node with a frequency
     * @param frequency The frequency of the keyword
     */
    public HeapNode(int frequency) {
        this.frequency = frequency;
        this.degree = 0;
        this.mark = false;
    }
}
