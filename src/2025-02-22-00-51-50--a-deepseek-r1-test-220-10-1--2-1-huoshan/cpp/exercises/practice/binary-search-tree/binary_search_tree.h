#if !defined(BINARY_SEARCH_TREE_H)
#define BINARY_SEARCH_TREE_H

namespace binary_search_tree {

template <typename T>
class binary_tree {
    struct Node {
        T data;
        Node* left;
        Node* right;
        
        Node(T value) : data(value), left(nullptr), right(nullptr) {}
    };

    Node* root = nullptr;

    Node** find_insertion_point(T value) {
        Node** current = &root;
        while (*current != nullptr) {
            if (value <= (*current)->data) {
                current = &(*current)->left;
            } else {
                current = &(*current)->right;
            }
        }
        return current;
    }

public:
    binary_tree() = default;
    
    void insert(T value) {
        *find_insertion_point(value) = new Node(value);
    }

    bool contains(T value) const {
        Node* current = root;
        while (current != nullptr) {
            if (value == current->data) {
                return true;
            }
            current = (value < current->data) ? current->left : current->right;
        }
        return false;
    }

    // Test interface methods
    T data() const { return root->data; }
    const binary_tree left() const { return binary_tree(root->left); }
    const binary_tree right() const { return binary_tree(root->right); }

    // Helper constructor for child nodes
    explicit binary_tree(Node* node) : root(node) {}
};

}  // namespace binary_search_tree

#endif // BINARY_SEARCH_TREE_H
