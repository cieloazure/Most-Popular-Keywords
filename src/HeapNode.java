public class HeapNode {
    public HeapNode parent; /* Pointer to parent node */
    public HeapNode child; /* Pointer to child list, points to just one child in a circular doubly linked list*/
    public HeapNode leftSibling; /* Node to left in the list */
    public HeapNode rightSibling; /* Node to the right in the list */
    public int degree; /* Number of nodes in the child list */
    public boolean mark; /* Child cut field to obtain amortized analysis*/
    public int frequency; /* Frequency of the keyword */

    /**
     *  Initialize the node with a frequency
     * @param frequency
     */
    public HeapNode(int frequency) {
        this.frequency = frequency;
        this.degree = 0;
        this.mark = false;
    }
}
