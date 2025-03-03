#if !defined(CIRCULAR_BUFFER_H)
#define CIRCULAR_BUFFER_H

#include <vector>
#include <stdexcept>

namespace circular_buffer {

template<typename T>
class circular_buffer {
public:
    explicit circular_buffer(size_t capacity) 
        : buffer_(capacity), head_(0), tail_(0), size_(0), capacity_(capacity) {}

    void write(T value) {
        if (isFull()) {
            throw std::domain_error("Buffer is full");
        }
        buffer_[tail_] = value;
        increment_tail();
        size_++;
    }

    void overwrite(T value) {
        if (isFull()) {
            increment_head(); // Overwrite the oldest element
        } else {
            size_++;
        }
        buffer_[tail_] = value;
        increment_tail();
    }

    T read() {
        if (isEmpty()) {
            throw std::domain_error("Buffer is empty");
        }
        T value = buffer_[head_];
        increment_head();
        size_--;
        return value;
    }

    void clear() {
        head_ = 0;
        tail_ = 0;
        size_ = 0;
    }

    bool isEmpty() const {
        return size_ == 0;
    }

    bool isFull() const {
        return size_ == capacity_;
    }

private:
    std::vector<T> buffer_;
    size_t head_;       // Points to the oldest element
    size_t tail_;       // Points to where the next element will be written
    size_t size_;       // Current number of elements
    size_t capacity_;   // Maximum capacity of the buffer

    void increment_head() {
        head_ = (head_ + 1) % capacity_;
    }

    void increment_tail() {
        tail_ = (tail_ + 1) % capacity_;
    }
};

}  // namespace circular_buffer

#endif // CIRCULAR_BUFFER_H
