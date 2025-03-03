class CircularBuffer<T> {
    static class BufferIOException extends Exception {
        BufferIOException(String message) {
            super(message);
        }
    }
    private final Object[] buffer;
    private int head;
    private int tail;
    private int count;
    private final int size;

    CircularBuffer(final int size) {
        this.size = size;
        this.buffer = new Object[size];
        this.head = 0;
        this.tail = 0;
        this.count = 0;
    }

    T read() throws BufferIOException {
        if (count == 0) {
            throw new BufferIOException("Buffer is empty");
        }
        @SuppressWarnings("unchecked")
        T data = (T) buffer[head];
        head = (head + 1) % size;
        count--;
        return data;
    }

    void write(T data) throws BufferIOException {
        if (count == size) {
            throw new BufferIOException("Buffer is full");
        }
        buffer[tail] = data;
        tail = (tail + 1) % size;
        count++;
    }

    void overwrite(T data) {
        if (count == size) {
            head = (head + 1) % size;
            count--;
        }
        buffer[tail] = data;
        tail = (tail + 1) % size;
        count++;
    }

    void clear() {
        head = 0;
        tail = 0;
        count = 0;
    }
}
