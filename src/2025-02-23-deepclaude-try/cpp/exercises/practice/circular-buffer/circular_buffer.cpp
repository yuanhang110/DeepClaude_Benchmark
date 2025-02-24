#include "circular_buffer.h"

namespace circular_buffer {

circular_buffer::circular_buffer(size_t capacity) 
    : capacity_(capacity), data_(capacity) {}

int circular_buffer::read() {
    if (is_empty()) {
        throw std::runtime_error("Buffer is empty");
    }
    int value = data_[start_];
    start_ = (start_ + 1) % capacity_;
    --size_;
    return value;
}

void circular_buffer::write(int value) {
    if (is_full()) {
        throw std::runtime_error("Buffer is full");
    }
    data_[end_] = value;
    end_ = (end_ + 1) % capacity_;
    ++size_;
}

void circular_buffer::overwrite(int value) {
    if (is_full()) {
        start_ = (start_ + 1) % capacity_;
        --size_;
    }
    data_[end_] = value;
    end_ = (end_ + 1) % capacity_;
    ++size_;
}

bool circular_buffer::is_empty() const {
    return size_ == 0;
}

bool circular_buffer::is_full() const {
    return size_ == capacity_;
}

}  // namespace circular_buffer
