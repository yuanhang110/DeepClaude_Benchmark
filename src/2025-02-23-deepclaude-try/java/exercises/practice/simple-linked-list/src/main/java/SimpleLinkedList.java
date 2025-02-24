import java.util.Arrays;

class SimpleLinkedList<T> {
    private static class Node<T> {
        private final T data;
        private Node<T> next;

        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node<T> head;
    private int size;

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
        Node<T> newNode = new Node<>(value);
        newNode.next = head;
        head = newNode;
        size++;
    }

    T pop() {
        if (head == null) {
            throw new RuntimeException("List is empty");
        }
        T data = head.data;
        head = head.next;
        size--;
        return data;
    }

    void reverse() {
        Node<T> prev = null;
        Node<T> current = head;
        Node<T> next = null;
        
        while (current != null) {
            next = current.next;
            current.next = prev;
            prev = current;
            current = next;
        }
        
        head = prev;
    }

    T[] asArray(Class<T> clazz) {
        Object[] temp = new Object[size];
        Node<T> current = head;
        int index = 0;
        while (current != null) {
            temp[index++] = current.data;
            current = current.next;
        }
        @SuppressWarnings("unchecked")
        T[] array = (T[]) Arrays.copyOf(temp, size, clazz);
        return array;
    }

    int size() {
        return size;
    }
}
