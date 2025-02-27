#pragma once

#include <stdexcept> // for std::out_of_range

namespace linked_list {

template <typename T>
class List {
private:
    struct Node {
        T value;
        Node* prev;
        Node* next;
        Node(const T& val, Node* p = nullptr, Node* n = nullptr)
            : value(val), prev(p), next(n) {}
    };

    Node* head;
    Node* tail;
    size_t node_count;

public:
    List() : head(nullptr), tail(nullptr), node_count(0) {}

    ~List() {
        Node* current = head;
        while (current) {
            Node* next = current->next;
            delete current;
            current = next;
        }
    }

    void push(const T& value) {
        if (!tail) {
            head = tail = new Node(value);
        } else {
            tail->next = new Node(value, tail, nullptr);
            tail = tail->next;
        }
        ++node_count;
    }

    T pop() {
        if (!tail) {
            throw std::out_of_range("List is empty");
        }
        Node* old_tail = tail;
        T val = old_tail->value;
        tail = tail->prev;
        if (tail) {
            tail->next = nullptr;
        } else {
            head = nullptr;
        }
        delete old_tail;
        --node_count;
        return val;
    }

    T shift() {
        if (!head) {
            throw std::out_of_range("List is empty");
        }
        Node* old_head = head;
        T val = old_head->value;
        head = head->next;
        if (head) {
            head->prev = nullptr;
        } else {
            tail = nullptr;
        }
        delete old_head;
        --node_count;
        return val;
    }

    void unshift(const T& value) {
        if (!head) {
            head = tail = new Node(value);
        } else {
            head->prev = new Node(value, nullptr, head);
            head = head->prev;
        }
        ++node_count;
    }

    size_t count() const {
        return node_count;
    }

    bool erase(const T& value) {
        Node* current = head;
        while (current) {
            if (current->value == value) {
                if (current->prev) {
                    current->prev->next = current->next;
                } else {
                    head = current->next;
                }
                if (current->next) {
                    current->next->prev = current->prev;
                } else {
                    tail = current->prev;
                }
                delete current;
                --node_count;
                return true;
            }
            current = current->next;
        }
        return false;
    }
};

}  // namespace linked_list
