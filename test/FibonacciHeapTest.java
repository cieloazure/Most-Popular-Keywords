import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class FibonacciHeapTest {
    @Test
    void insertOneNode() {
        FibonacciHeap h = new FibonacciHeap();
        h.insert(1);
        HeapNode max = h.max;
        assertEquals(max, max.rightSibling);
        assertEquals(max, max.leftSibling);
    }

    @Test
    void insertTwoNodes() {
        FibonacciHeap h = new FibonacciHeap();
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
        FibonacciHeap h = new FibonacciHeap();
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
        FibonacciHeap h = new FibonacciHeap();
        HeapNode node = h.insert(5);
        HeapNode node2 = h.insert(3);

        h.increaseKey(node, 10);
        assertEquals(15, h.max.frequency);

        h.increaseKey(node2, 30);
        assertEquals(33, h.max.frequency);
    }

    @Test
    void testCutInIncreaseKey() {
        FibonacciHeap h = new FibonacciHeap();
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
        HeapNode node6 = h.insert(2);
        h.increaseKey(node1, 12);
        h.increaseKey(node2, 11);
        h.increaseKey(node3, 6);
        HeapNode max1 = h.extractMax();
        assertEquals(33, max1.frequency);
        h.increaseKey(node5, 3);
    }

    @Test
    void testCascadingCutInIncreaseKey() {
        FibonacciHeap h = new FibonacciHeap();
        HeapNode n1 = new HeapNode(50);
        HeapNode n2 = new HeapNode(25);
        n1.child = insertInList(n1.child, n2);
        n2.parent = n1;
        HeapNode n3 = new HeapNode(12);
        n3.mark = true;
        n2.child = insertInList(n2.child, n3);
        n3.parent = n2;
        HeapNode n4 = new HeapNode(7);
        n4.mark = true;
        n3.child = insertInList(n3.child, n4);
        n4.parent = n3;
        HeapNode n5 = new HeapNode(6);
        n4.child = insertInList(n4.child, n5);
        n5.parent = n4;
        h.max = insertInList(h.max, n1);
        assertFalse(n2.mark);
        h.increaseKey(n5, 2);
        assertTrue(n2.mark);

        int[] expectedValues = new int[]{50, 12, 7, 8};

        int[] actualValues = new int[4];
        actualValues[0] = h.max.frequency;
        HeapNode iter = h.max.rightSibling;

        int i = 1;
        while (iter != h.max) {
            actualValues[i++] = iter.frequency;
            iter = iter.rightSibling;
        }

        assertArrayEquals(expectedValues, actualValues);

    }

    @Test
    void increaseKeyByIllegalAmount() {
        FibonacciHeap h = new FibonacciHeap();
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
        HeapNode node6 = h.insert(2);
        h.increaseKey(node1, 12);
        h.increaseKey(node2, 11);
        h.increaseKey(node3, 6);
        HeapNode max1 = h.extractMax();
        assertEquals(33, max1.frequency);
        assertThrows(IllegalArgumentException.class, () -> {

            h.increaseKey(node5, -3);
        });
    }


    @Test
    void extractMax() {
        FibonacciHeap h = new FibonacciHeap();
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
    void extractMaxWithOneNode() {
        FibonacciHeap h = new FibonacciHeap();
        h.insert(1);
        HeapNode n = h.extractMax();
        assertEquals(1, n.frequency);
    }

    @Test
    void extractMaxWithNoNodes() {
        FibonacciHeap h = new FibonacciHeap();
        HeapNode n = h.extractMax();
        assertNull(n);
    }

    @Test
    void printHeap() {
        FibonacciHeap h = new FibonacciHeap();
        h.insert(1);
        h.insert(2);
        h.insert(3);
        h.insert(4);
        h.insert(5);
        h.printHeap();
    }

    @Test
    void printHeap2() {
        FibonacciHeap h = new FibonacciHeap();
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

}