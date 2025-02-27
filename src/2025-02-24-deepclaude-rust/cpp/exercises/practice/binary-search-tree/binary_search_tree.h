#if !defined(BINARY_SEARCH_TREE_H)
#define BINARY_SEARCH_TREE_H

namespace binary_search_tree {
    struct Node {
        int data;
        Node* left;
        Node* right;
        Node(int d) : data(d), left(nullptr), right(nullptr) {}
    };

    class BinarySearchTree {
    public:
        BinarySearchTree() : root(nullptr) {}
        void insert(int value);
        bool contains(int value) const;

    private:
        Node* root;
    };
}  // namespace binary_search_tree

#endif // BINARY_SEARCH_TREE_H
