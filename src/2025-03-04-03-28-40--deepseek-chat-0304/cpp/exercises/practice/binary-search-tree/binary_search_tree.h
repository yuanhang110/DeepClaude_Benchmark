#if !defined(BINARY_SEARCH_TREE_H)
#define BINARY_SEARCH_TREE_H

namespace binary_search_tree {

template <typename T>
struct Node {
    T data;
    Node* left;
    Node* right;
    
    Node(T value) : data(value), left(nullptr), right(nullptr) {}
};

template <typename T>
class binary_tree {
public:
    binary_tree() : root(nullptr) {}
    ~binary_tree() { clear(root); }
    
    void insert(T value) {
        root = insert(root, value);
    }
    
    bool search(T value) const {
        return search(root, value);
    }
    
    T data() const {
        if (!root) throw std::runtime_error("Tree is empty");
        return root->data;
    }
    
    binary_tree* left() const {
        if (!root || !root->left) return nullptr;
        auto left_tree = new binary_tree();
        left_tree->root = root->left;
        return left_tree;
    }
    
    binary_tree* right() const {
        if (!root || !root->right) return nullptr;
        auto right_tree = new binary_tree();
        right_tree->root = root->right;
        return right_tree;
    }

private:
    Node<T>* root;
    
    class iterator {
        std::stack<Node<T>*> stack;
        
    public:
        iterator(Node<T>* root) {
            while (root) {
                stack.push(root);
                root = root->left;
            }
        }
        
        T operator*() const {
            return stack.top()->data;
        }
        
        iterator& operator++() {
            Node<T>* node = stack.top()->right;
            stack.pop();
            while (node) {
                stack.push(node);
                node = node->left;
            }
            return *this;
        }
        
        bool operator!=(const iterator& other) const {
            return !stack.empty() || !other.stack.empty();
        }
    };
    
public:
    iterator begin() const {
        return iterator(root);
    }
    
    iterator end() const {
        return iterator(nullptr);
    }
    
    Node<T>* insert(Node<T>* node, T value) {
        if (!node) return new Node<T>(value);
        
        if (value <= node->data)
            node->left = insert(node->left, value);
        else
            node->right = insert(node->right, value);
            
        return node;
    }
    
    bool search(const Node<T>* node, T value) const {
        if (!node) return false;
        if (value == node->data) return true;
        
        if (value < node->data)
            return search(node->left, value);
        else
            return search(node->right, value);
    }
    
    void clear(Node<T>* node) {
        if (node) {
            clear(node->left);
            clear(node->right);
            delete node;
        }
    }
};

}  // namespace binary_search_tree

#endif // BINARY_SEARCH_TREE_H
