#pragma once
#include <cstddef>  // Add for size_t

namespace linked_list {

template <typename T>
class List {
private:
    struct Node {
        T value;
        Node* prev;
        Node* next;
        
        Node(const T& val) : value(val), prev(nullptr), next(nullptr) {}
    };

    Node* head;
    Node* tail;
    std::size_t node_count;

public:
    List() : head(nullptr), tail(nullptr), node_count(0) {}
    ~List() { clear(); }

    // Add to end
    void push_back(const T& value) {
        Node* new_node = new Node(value);
        if (!head) {
            head = tail = new_node;
        } else {
            tail->next = new_node;
            new_node->prev = tail;
            tail = new_node;
        }
        node_count++;
    }

    // Add to beginning 
    void push_front(const T& value) {
        Node* new_node = new Node(value);
        if (!head) {
            head = tail = new_node;
        } else {
            head->prev = new_node;
            new_node->next = head;
            head = new_node;
        }
        node_count++;
    }

    // Remove from end
    T pop_back() {
        if (!tail) throw "List is empty";
        T value = tail->value;
        Node* to_delete = tail;
        
        if (tail->prev) {
            tail = tail->prev;
            tail->next = nullptr;
        } else {
            head = tail = nullptr;
        }
        
        delete to_delete;
        node_count--;
        return value;
    }

    // Remove from beginning
    T pop_front() {
        if (!head) throw "List is empty";
        T value = head->value;
        Node* to_delete = head;
        
        if (head->next) {
            head = head->next;
            head->prev = nullptr;
        } else {
            head = tail = nullptr;
        }
        
        delete to_delete;
        node_count--;
        return value;
    }

    // Remove first occurrence
    bool erase(const T& value) {
        Node* current = head;
        while (current) {
            if (current->value == value) {
                if (current->prev) current->prev->next = current->next;
                if (current->next) current->next->prev = current->prev;
                
                if (current == head) head = current->next;
                if (current == tail) tail = current->prev;
                
                delete current;
                node_count--;
                return true;
            }
            current = current->next;
        }
        return false;
    }

    std::size_t count() const { return node_count; }
    
    // Rename methods to match required interface
    void push(const T& value) { push_back(value); }
    T pop() { return pop_back(); }
    T shift() { return pop_front(); }
    void unshift(const T& value) { push_front(value); }

private:
    void clear() {
        Node* current = head;
        while (current) {
            Node* next = current->next;
            delete current;
            current = next;
        }
        head = tail = nullptr;
        node_count = 0;
    }
};

}  // namespace linked_list
