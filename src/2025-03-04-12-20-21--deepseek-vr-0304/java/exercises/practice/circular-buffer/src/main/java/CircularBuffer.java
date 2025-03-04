class CircularBuffer<T> {

    private final Object[] buffer;
    private int head;
    private int tail;
    private int count;

    CircularBuffer(final int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Buffer size must be positive");
        }
        this.buffer = new Object[size];
        this.head = 0;
        this.tail = 0;
        this.count = 0;
    }

    T read() throws BufferIOException {
        if (isEmpty()) {
            throw new BufferIOException("Buffer is empty");
        }
        @SuppressWarnings("unchecked")
        T data = (T) buffer[tail];
        tail = (tail + 1) % buffer.length;
        count--;
        return data;
    }

    private boolean isEmpty() {
        return count == 0;
    }

    void write(T data) throws BufferIOException {
        if (isFull()) {
            throw new BufferIOException("Buffer is full");
        }
        buffer[head] = data;
        head = (head + 1) % buffer.length;
        count++;
    }

    private boolean isFull() {
        return count == buffer.length;
    }

    void overwrite(T data) {
        if (isFull()) {
            // Overwrite the oldest element at tail
            buffer[tail] = data;
            tail = (tail + 1) % buffer.length;
            head = tail; // Keep buffer full
        } else {
            // Normal write
            buffer[head] = data;
            head = (head + 1) % buffer.length;
            count++;
        }
    }

    void clear() {
        head = 0;
        tail = 0;
        count = 0;
    }

}
