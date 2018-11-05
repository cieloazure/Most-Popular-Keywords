import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class HeapTest {
    @Test
    void insertOneNode() {
        Heap h = new Heap();
        h.insert(1);
        HeapNode max = h.max;
        assertEquals(max, max.rightSibling);
        assertEquals(max, max.leftSibling);
    }

    @Test
    void insertTwoNodes() {
        Heap h = new Heap();
        h.insert(1);
        h.insert(2);
        HeapNode max = h.max;
        HeapNode maxRight = max.rightSibling;
        assertEquals(1, maxRight.frequency);

        HeapNode leftOfRight = maxRight.leftSibling;
        assertEquals(2, leftOfRight.frequency);
    }

    @Test
    void insertThreeNodes() {
        Heap h = new Heap();
        h.insert(1);
        h.insert(2);
        h.insert(3);

        assertEquals(3, h.max.frequency);
        int[] values = new int[3];
        values[0] = h.max.frequency;
        values[1] = h.max.rightSibling.frequency;
        values[2] = h.max.rightSibling.rightSibling.frequency;
        Arrays.sort(values);
        assertArrayEquals(new int[]{1, 2, 3}, values);
    }

    @Test
    void increaseKeyAtTopLevelList() {
        Heap h = new Heap();
        HeapNode node = h.insert(5);
        HeapNode node2 = h.insert(3);

        h.increaseKey(node, 10);
        assertEquals(15, h.max.frequency);

        h.increaseKey(node2, 30);
        assertEquals(33, h.max.frequency);
    }

    @Test
    void heapLink() {
        Heap h = new Heap();
        HeapNode node1 = h.insert(1);
        HeapNode node2 = h.insert(2);
        h.heapLink(node2, node1);
        assertEquals(node1.child, node2);
        assertEquals(1, node1.degree);
        HeapNode node3 = h.insert(3);
        h.heapLink(node3, node1);
        assertEquals(2, node1.degree);
        assertEquals(node2, node1.child);
        assertEquals(node3, node2.rightSibling);
    }

    @Test
    void extractMax() {
        Heap h = new Heap();
        //facebook
        HeapNode node1 = h.insert(5);
        // youtube
        HeapNode node2 = h.insert(3);
        h.increaseKey(node1, 10);
        //amazon
        HeapNode node3 = h.insert(2);
        //gmail
        HeapNode node4 = h.insert(4);
        h.insert(2);
        h.increaseKey(node1, 6);
        h.increaseKey(node2, 8);
        //ebay
        HeapNode node5 = h.insert(2);
        h.insert(2);
        h.increaseKey(node1, 12);
        h.increaseKey(node2, 11);
        h.increaseKey(node3, 6);
        HeapNode max1 = h.extractMax();
        assertEquals(33, max1.frequency);
        assertEquals(22, h.max.frequency);
        HeapNode max2 = h.extractMax();
        assertEquals(22, max2.frequency);
        assertEquals(8, h.max.frequency);
        HeapNode max3 = h.extractMax();
        assertEquals(8, max3.frequency);
        assertEquals(4, h.max.frequency);

        h.insert(max1);
        assertEquals(33, h.max.frequency);
        h.insert(max2);
        h.insert(max3);
        assertEquals(h.nodes, 7);

        h.increaseKey(node1, 12);
        h.increaseKey(node3, 2);
        h.insert(3);
        h.insert(4);
        h.increaseKey(node4, 15);
        h.insert(3);
        h.increaseKey(node5, 12);
        h.insert(6);
        h.insert(5);
        assertEquals(45, h.max.frequency);
        HeapNode[] max = new HeapNode[5];
        for (int i = 0; i < 5; i++) {
            max[i] = h.extractMax();
        }
        for (int i = 0; i < 5; i++) {
            h.insert(max[i]);
        }
    }

    @Test
    void printHeap() {
        Heap h = new Heap();
        h.insert(1);
        h.insert(2);
        h.insert(3);
        h.insert(4);
        h.insert(5);
        h.printHeap();
    }

    @Test
    void printHeap2() {
        Heap h = new Heap();
        //facebook
        HeapNode node1 = h.insert(5);
        // youtube
        HeapNode node2 = h.insert(3);
        h.increaseKey(node1, 10);
        //amazon
        HeapNode node3 = h.insert(2);
        //gmail
        HeapNode node4 = h.insert(4);
        h.insert(2);
        h.increaseKey(node1, 6);
        h.increaseKey(node2, 8);
        //ebay
        HeapNode node5 = h.insert(2);
        h.insert(2);
        h.increaseKey(node1, 12);
        h.increaseKey(node2, 11);
        h.increaseKey(node3, 6);
        HeapNode max1 = h.extractMax();
        h.printHeap();
    }
}