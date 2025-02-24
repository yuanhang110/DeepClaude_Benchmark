class BufferIOException extends RuntimeException {
    BufferIOException(String message) {
        super(message);
    }
}

class CircularBuffer<T> {

    private final T[] buffer;
    private int readIndex = 0;
    private int writeIndex = 0;
    private int count = 0;
    
    @SuppressWarnings("unchecked")
    CircularBuffer(final int size) {
        buffer = (T[]) new Object[size];
    }

    T read() throws BufferIOException {
        if (count == 0) {
            throw new BufferIOException("Cannot read from empty buffer");
        }
        T value = buffer[readIndex];
        readIndex = (readIndex + 1) % buffer.length;
        count--;
        return value;
    }

    void write(T data) throws BufferIOException {
        if (count == buffer.length) {
            throw new BufferIOException("Cannot write to full buffer");
        }
        buffer[writeIndex] = data;
        writeIndex = (writeIndex + 1) % buffer.length;
        count++;
    }

    void overwrite(T data) {
        if (count == buffer.length) {
            readIndex = (readIndex + 1) % buffer.length;
            count--;
        }
        buffer[writeIndex] = data;
        writeIndex = (writeIndex + 1) % buffer.length;
        count++;
    }

    void clear() {
        readIndex = 0;
        writeIndex = 0;
        count = 0;
    }

}
