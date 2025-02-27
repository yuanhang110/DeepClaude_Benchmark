#if !defined(CIRCULAR_BUFFER_H)
#define CIRCULAR_BUFFER_H

#include <vector>
#include <stdexcept>

namespace circular_buffer {

template <typename T>
class circular_buffer {
public:
    explicit circular_buffer(size_t capacity)
        : buffer_(capacity), capacity_(capacity) {}
    
    void write(T item) {
        if (is_full()) {
            throw std::domain_error("Buffer is full");
        }
        
        buffer_[head_] = item;
        head_ = (head_ + 1) % capacity_;
        count_++;
    }
    
    void overwrite(T item) {
        if (is_full()) {
            tail_ = (tail_ + 1) % capacity_;
            count_--;
        }
        
        buffer_[head_] = item;
        head_ = (head_ + 1) % capacity_;
        count_++;
    }
    
    T read() {
        if (is_empty()) {
            throw std::domain_error("Buffer is empty");
        }
        
        T item = buffer_[tail_];
        tail_ = (tail_ + 1) % capacity_;
        count_--;
        
        return item;
    }
    
    bool is_empty() const {
        return count_ == 0;
    }
    
    bool is_full() const {
        return count_ == capacity_;
    }
    
    void clear() {
        head_ = 0;
        tail_ = 0;
        count_ = 0;
    }
    
private:
    std::vector<T> buffer_;
    size_t capacity_;
    size_t head_ = 0;   // Position to write next
    size_t tail_ = 0;   // Position to read next
    size_t count_ = 0;  // Number of elements
};

}  // namespace circular_buffer

#endif // CIRCULAR_BUFFER_H
