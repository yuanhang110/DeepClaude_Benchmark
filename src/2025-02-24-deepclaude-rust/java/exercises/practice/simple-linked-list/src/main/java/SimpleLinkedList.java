class SimpleLinkedList<T> {
    private static class Node<T> {
        private final T data;
        private Node<T> next;

        Node(T data) {
            this.data = data;
            this.next = null;
        }

        Node(T data, Node<T> next) {
            this.data = data;
            this.next = next;
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
        if (values != null) {
            for (T value : values) {
                Node<T> newNode = new Node<>(value);
                if (head == null) {
                    head = newNode;
                } else {
                    Node<T> current = head;
                    while (current.next != null) {
                        current = current.next;
                    }
                    current.next = newNode;
                }
                size++;
            }
        }
    }

    void push(T value) {
        Node<T> newNode = new Node<>(value, head);
        head = newNode;
        size++;
    }

    T pop() {
        if (head == null) {
            throw new java.util.NoSuchElementException("List is empty");
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

    @SuppressWarnings("unchecked")
    T[] asArray(Class<T> clazz) {
        Object[] array = new Object[size];
        Node<T> current = head;
        int i = 0;
        while (current != null) {
            array[i++] = current.data;
            current = current.next;
        }
        return (T[]) Arrays.copyOf(array, size, clazz);
    }

    int size() {
        return size;
    }
}
