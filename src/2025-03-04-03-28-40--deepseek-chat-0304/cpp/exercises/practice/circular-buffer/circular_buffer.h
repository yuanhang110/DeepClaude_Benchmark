#if !defined(CIRCULAR_BUFFER_H)
#define CIRCULAR_BUFFER_H

#include <vector>
#include <stdexcept>

namespace circular_buffer {

template <typename T>
class circular_buffer;

template <typename T>
class CircularBuffer {
public:
    explicit CircularBuffer(size_t capacity);
    
    void write(const T& data);
    void overwrite(const T& data);
    T read();
    void clear();
    bool empty() const;
    bool full() const;
    size_t size() const;

private:
    std::vector<T> buffer_;
    size_t head_;
    size_t tail_;
    size_t size_;
    const size_t capacity_;
    
    void advance(size_t& index) const;
};

// Type alias to match expected name
template <typename T>
using circular_buffer = CircularBuffer<T>;

}  // namespace circular_buffer

#endif // CIRCULAR_BUFFER_H
