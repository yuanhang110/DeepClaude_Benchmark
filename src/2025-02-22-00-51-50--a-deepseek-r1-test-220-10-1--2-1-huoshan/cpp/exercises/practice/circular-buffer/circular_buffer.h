#if !defined(CIRCULAR_BUFFER_H)
#define CIRCULAR_BUFFER_H

#include <stdexcept>
#include <vector>

template <typename T>
class circular_buffer {
public:
    explicit circular_buffer(size_t capacity) 
        : capacity(capacity), buffer(new T[capacity]) {}
        
    ~circular_buffer() {
        delete[] buffer;
    }

    T read() {
        if (count == 0) {
            throw std::domain_error("Cannot read from empty buffer");
        }
        
        T value = buffer[head];
        head = (head + 1) % capacity;
        --count;
        return value;
    }

    void write(const T& data) {
        if (count == capacity) {
            throw std::domain_error("Cannot write to full buffer");
        }
        
        buffer[tail] = data;
        tail = (tail + 1) % capacity;
        ++count;
    }

    void overwrite(const T& data) {
        if (count < capacity) {
            write(data);
        } else {
            buffer[tail] = data;
            tail = (tail + 1) % capacity;
            head = (head + 1) % capacity;
        }
    }

    void clear() {
        head = 0;
        tail = 0;
        count = 0;
    }

    size_t size() const { return count; }
    bool empty() const { return count == 0; }
    bool full() const { return count == capacity; }

private:
    T* buffer;
    size_t head = 0;
    size_t tail = 0;
    size_t count = 0;
    const size_t capacity;
};

}  // namespace circular_buffer

#endif // CIRCULAR_BUFFER_H
