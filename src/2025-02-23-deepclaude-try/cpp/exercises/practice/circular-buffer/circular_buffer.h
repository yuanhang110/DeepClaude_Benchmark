#if !defined(CIRCULAR_BUFFER_H)
#define CIRCULAR_BUFFER_H

#include <vector>
#include <stdexcept>

namespace circular_buffer {

class circular_buffer {
public:
    explicit circular_buffer(size_t capacity);
    int read();
    void write(int value);
    void overwrite(int value);
    bool is_empty() const;
    bool is_full() const;

private:
    size_t capacity_;
    size_t start_ = 0;
    size_t end_ = 0;
    size_t size_ = 0;
    std::vector<int> data_;
};

}  // namespace circular_buffer

#endif // CIRCULAR_BUFFER_H
