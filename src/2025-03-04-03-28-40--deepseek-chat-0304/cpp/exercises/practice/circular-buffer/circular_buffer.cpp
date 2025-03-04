#include "circular_buffer.h"

namespace circular_buffer {

template <typename T>
CircularBuffer<T>::CircularBuffer(size_t capacity)
    : buffer_(capacity), head_(0), tail_(0), size_(0), capacity_(capacity) {}

template <typename T>
void CircularBuffer<T>::write(const T& data) {
    if (full()) {
        throw std::runtime_error("Buffer is full");
    }
    buffer_[tail_] = data;
    advance(tail_);
    ++size_;
}

template <typename T>
void CircularBuffer<T>::overwrite(const T& data) {
    buffer_[tail_] = data;
    advance(tail_);
    if (full()) {
        advance(head_);
    } else {
        ++size_;
    }
}

template <typename T>
T CircularBuffer<T>::read() {
    if (empty()) {
        throw std::runtime_error("Buffer is empty");
    }
    T data = buffer_[head_];
    advance(head_);
    --size_;
    return data;
}

template <typename T>
void CircularBuffer<T>::clear() {
    head_ = tail_ = size_ = 0;
}

template <typename T>
bool CircularBuffer<T>::empty() const {
    return size_ == 0;
}

template <typename T>
bool CircularBuffer<T>::full() const {
    return size_ == capacity_;
}

template <typename T>
size_t CircularBuffer<T>::size() const {
    return size_;
}

template <typename T>
void CircularBuffer<T>::advance(size_t& index) const {
    index = (index + 1) % capacity_;
}

// Explicit template instantiations
template class circular_buffer::circular_buffer<int>;
template class circular_buffer::circular_buffer<std::string>;

}  // namespace circular_buffer
