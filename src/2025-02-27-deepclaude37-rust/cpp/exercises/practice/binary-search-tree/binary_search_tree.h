#if !defined(BINARY_SEARCH_TREE_H)
#define BINARY_SEARCH_TREE_H

#include <memory>
#include <vector>
#include <iterator>

namespace binary_search_tree {

template <typename T>
class binary_tree {
private:
    T data_;
    std::unique_ptr<binary_tree<T>> left_;
    std::unique_ptr<binary_tree<T>> right_;

    // Helper for in-order traversal
    void collect_inorder(std::vector<T>& result) const {
        if (left_) {
            left_->collect_inorder(result);
        }
        result.push_back(data_);
        if (right_) {
            right_->collect_inorder(result);
        }
    }

public:
    // Iterator class for in-order traversal
    class iterator {
    private:
        std::vector<T> values_;
        size_t position_;

    public:
        using iterator_category = std::forward_iterator_tag;
        using value_type = T;
        using difference_type = std::ptrdiff_t;
        using pointer = T*;
        using reference = T&;

        iterator(const binary_tree<T>* tree, bool end = false) : position_(0) {
            if (!tree) return;
            if (!end) {
                tree->collect_inorder(values_);
            } else {
                position_ = 0;
            }
        }

        iterator& operator++() {
            ++position_;
            return *this;
        }

        iterator operator++(int) {
            iterator tmp = *this;
            ++(*this);
            return tmp;
        }

        bool operator==(const iterator& other) const {
            return position_ == other.position_ || 
                  (position_ >= values_.size() && other.position_ >= other.values_.size());
        }

        bool operator!=(const iterator& other) const {
            return !(*this == other);
        }

        const T& operator*() const {
            return values_[position_];
        }
    };

    explicit binary_tree(T value) : data_(value) {}

    T data() const { return data_; }
    binary_tree* left() const { return left_.get(); }
    binary_tree* right() const { return right_.get(); }

    void insert(T value);
    bool contains(T value) const;

    iterator begin() const {
        return iterator(this);
    }

    iterator end() const {
        return iterator(this, true);
    }
};

template <typename T>
void binary_tree<T>::insert(T value) {
    if (value <= data_) {
        if (!left_) {
            left_ = std::make_unique<binary_tree<T>>(value);
        } else {
            left_->insert(value);
        }
    } else {
        if (!right_) {
            right_ = std::make_unique<binary_tree<T>>(value);
        } else {
            right_->insert(value);
        }
    }
}

template <typename T>
bool binary_tree<T>::contains(T value) const {
    if (value == data_) {
        return true;
    }
    if (value <= data_) {
        return left_ ? left_->contains(value) : false;
    } else {
        return right_ ? right_->contains(value) : false;
    }
}

}  // namespace binary_search_tree

#endif // BINARY_SEARCH_TREE_H
