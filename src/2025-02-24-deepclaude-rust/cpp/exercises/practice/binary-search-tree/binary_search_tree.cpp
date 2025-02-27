#include "binary_search_tree.h"

namespace binary_search_tree {

void BinarySearchTree::insert(int value) {
    Node** current = &root;
    while (*current != nullptr) {
        if (value <= (*current)->data) {
            current = &((*current)->left);
        } else {
            current = &((*current)->right);
        }
    }
    *current = new Node(value);
}

bool BinarySearchTree::contains(int value) const {
    Node* current = root;
    while (current != nullptr) {
        if (current->data == value) {
            return true;
        } else if (value < current->data) {
            current = current->left;
        } else {
            current = current->right;
        }
    }
    return false;
}

}  // namespace binary_search_tree
