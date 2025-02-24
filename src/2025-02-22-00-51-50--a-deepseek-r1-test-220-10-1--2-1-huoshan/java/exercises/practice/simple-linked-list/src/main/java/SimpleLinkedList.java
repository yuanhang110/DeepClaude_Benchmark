class SimpleLinkedList<T> {
    private Node head;
    private int size;
    
    private class Node {
        T data;
        Node next;
        
        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    SimpleLinkedList() {
        head = null;
        size = 0;
    }

    SimpleLinkedList(T[] values) {
        this();
        for (int i = values.length - 1; i >= 0; i--) {
            push(values[i]);
        }
    }

    void push(T value) {
        Node newNode = new Node(value);
        newNode.next = head;
        head = newNode;
        size++;
    }

    T pop() {
        if (head == null) {
            throw new java.util.NoSuchElementException();
        }
        T value = head.data;
        head = head.next;
        size--;
        return value;
    }

    void reverse() {
        Node prev = null;
        Node current = head;
        Node next = null;
        
        while (current != null) {
            next = current.next;
            current.next = prev;
            prev = current;
            current = next;
        }
        
        head = prev;
    }

    T[] asArray(Class<T> clazz) {
        @SuppressWarnings("unchecked")
        T[] arr = (T[]) java.lang.reflect.Array.newInstance(clazz, size);
        Node current = head;
        int i = 0;
        while (current != null) {
            arr[i++] = current.data;
            current = current.next;
        }
        
        return arr;
    }

    int size() {
        return size;
    }
}
