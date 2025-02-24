class CircularBuffer<T> {
    private final Object[] buffer;
    private final int capacity;
    private int head;
    private int tail;
    private boolean isFull;

    CircularBuffer(final int size) {
        this.capacity = size;
        this.buffer = new Object[size];
        this.head = 0;
        this.tail = 0;
        this.isFull = false;
    }

    T read() throws BufferIOException {
        if (isEmpty()) {
            throw new BufferIOException("Tried to read from empty buffer");
        }
        
        @SuppressWarnings("unchecked")
        T data = (T) buffer[head];
        head++;
        if (head == capacity) {
            head = 0;
        }
        isFull = false;
        return data;
    }

    void write(T data) throws BufferIOException {
        if (isFull) {
            throw new BufferIOException("Tried to write to full buffer");
        }
        
        buffer[tail] = data;
        tail++;
        if (tail == capacity) {
            tail = 0;
        }
        isFull = (tail == head);
    }

    void overwrite(T data) {
        if (isFull) {
            head = (head + 1) % capacity;
        }
        
        buffer[tail] = data;
        tail = (tail + 1) % capacity;
        isFull = tail == head;
    }

    void clear() {
        head = 0;
        tail = 0;
        isFull = false;
    }

    private boolean isEmpty() {
        return !isFull && head == tail;
    }

}
