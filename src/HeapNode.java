public class HeapNode {
    public HeapNode parent;
    public HeapNode child;
    public HeapNode leftSibling;
    public HeapNode rightSibling;
    public int degree;
    public boolean mark;
    public int frequency;

    public HeapNode(int frequency) {
        this.frequency = frequency;
        this.degree = 0;
        this.mark = false;
    }
}
