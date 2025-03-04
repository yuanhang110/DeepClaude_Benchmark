class CircularBuffer<T> {
    private final T[] buffer;
    private int head = 0;
    private int tail = 0;
    private int count = 0;

    @SuppressWarnings("unchecked")
    CircularBuffer(final int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Buffer size must be positive");
        }
        // Safer array creation
        this.buffer = (T[]) java.lang.reflect.Array.newInstance(Object.class, size);
    }

    T read() throws BufferIOException {
        if (count == 0) {
            throw new BufferIOException("Tried to read from empty buffer");
        }
        T data = buffer[head];
        buffer[head] = null; // Clear the slot
        head = (head + 1) % buffer.length;
        count--;
        return data;
    }

    void write(T data) throws BufferIOException {
        if (data == null) {
            throw new IllegalArgumentException("Cannot write null to buffer");
        }
        if (count == buffer.length) {
            throw new BufferIOException("Tried to write to full buffer");
        }
        buffer[tail] = data;
        tail = (tail + 1) % buffer.length;
        count++;
    }

    void overwrite(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Cannot write null to buffer");
        }
        if (count < buffer.length) {
            write(data);
        } else {
            buffer[head] = data;
            head = (head + 1) % buffer.length;
            tail = (tail + 1) % buffer.length;
        }
    }

    void clear() {
        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = null;
        }
        head = 0;
        tail = 0;
        count = 0;
    }
}
