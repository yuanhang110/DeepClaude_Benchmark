class CircularBuffer<T> {
    private final T[] buffer;
    private int head = 0;
    private int tail = 0;
    private boolean isFull = false;

    @SuppressWarnings("unchecked")
    CircularBuffer(final int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Buffer size must be positive");
        }
        buffer = (T[]) new Object[size];
    }

    T read() throws BufferIOException {
        if (isEmpty()) {
            throw new BufferIOException("Buffer is empty");
        }
        T data = buffer[head];
        head = (head + 1) % buffer.length;
        isFull = false;
        return data;
    }

    private boolean isEmpty() {
        return !isFull && head == tail;
    }

    void write(T data) throws BufferIOException {
        if (isFull) {
            throw new BufferIOException("Buffer is full");
        }
        buffer[tail] = data;
        tail = (tail + 1) % buffer.length;
        isFull = tail == head;
    }

    void overwrite(T data) {
        if (isFull) {
            head = (head + 1) % buffer.length; // Move head forward, losing oldest data
        }
        buffer[tail] = data;
        tail = (tail + 1) % buffer.length;
        isFull = tail == head;
    }

    void clear() {
        head = 0;
        tail = 0;
        isFull = false;
    }

}
