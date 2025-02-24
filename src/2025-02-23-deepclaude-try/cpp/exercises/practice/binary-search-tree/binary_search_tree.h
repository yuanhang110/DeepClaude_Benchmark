#if !defined(BINARY_SEARCH_TREE_H)
#define BINARY_SEARCH_TREE_H

namespace binary_search_tree {
    template<typename T>
    class Node {
    private:
        T data_;
        Node* left_;
        Node* right_;
        
    public:
        Node(const T& value) : data_(value), left_(nullptr), right_(nullptr) {}
        
        const T& data() const { return data_; }
        Node* left() const { return left_; }
        Node* right() const { return right_; }
        
        friend class binary_tree<T>;
    };

    template<typename T>
    class binary_tree {
    private:
        Node<T>* root_;

        // Helper functions
        Node<T>* insert_impl(Node<T>* current, const T& value);
        bool contains_impl(Node<T>* current, const T& value) const;

    public:
        binary_tree() : root_(nullptr) {}
        ~binary_tree() {
            // TODO: Implement destructor to avoid memory leaks
        }
        
        void insert(const T& value);
        bool contains(const T& value) const;
        Node<T>* root() const { return root_; }
    };
}  // namespace binary_search_tree


// Template implementation
namespace binary_search_tree {
    template<typename T>
    Node<T>* binary_tree<T>::insert_impl(Node<T>* current, const T& value) {
        if (current == nullptr) {
            return new Node<T>(value);
        }
        
        if (value <= current->data_) {
            current->left_ = insert_impl(current->left_, value);
        } else {
            current->right_ = insert_impl(current->right_, value);
        }
        
        return current;
    }

    template<typename T>
    void binary_tree<T>::insert(const T& value) {
        root_ = insert_impl(root_, value);
    }

    template<typename T>
    bool binary_tree<T>::contains_impl(Node<T>* current, const T& value) const {
        if (current == nullptr) {
            return false;
        }
        
        if (current->data_ == value) {
            return true;
        }
        
        return value < current->data_ ? 
            contains_impl(current->left_, value) : 
            contains_impl(current->right_, value);
    }

    template<typename T>
    bool binary_tree<T>::contains(const T& value) const {
        return contains_impl(root_, value);
    }
}  // namespace binary_search_tree

#endif // BINARY_SEARCH_TREE_H
