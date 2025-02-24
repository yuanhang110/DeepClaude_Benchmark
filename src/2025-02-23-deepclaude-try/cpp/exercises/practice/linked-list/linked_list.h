#pragma once

#include <stdexcept> // for std::out_of_range

namespace linked_list {

template <typename T>
class List {
private:
    struct Node {
        T value;
        Node* next;
        Node* prev;
        explicit Node(const T& val) : value(val), next(nullptr), prev(nullptr) {}
    };

    Node* head;
    Node* tail;
    size_t count_;

public:
    List() : head(nullptr), tail(nullptr), count_(0) {}

    ~List() {
        Node* current = head;
        while (current) {
            Node* next = current->next;
            delete current;
            current = next;
        }
    }

    void push(const T& value) {
        Node* newNode = new Node(value);
        if (!tail) {
            head = tail = newNode;
        } else {
            tail->next = newNode;
            newNode->prev = tail;
            tail = newNode;
        }
        count_++;
    }

    T pop() {
        if (!tail) {
            throw std::out_of_range("List is empty");
        }
        T value = tail->value;
        Node* oldTail = tail;
        tail = tail->prev;
        if (tail) {
            tail->next = nullptr;
        } else {
            head = nullptr;
        }
        delete oldTail;
        count_--;
        return value;
    }

    T shift() {
        if (!head) {
            throw std::out_of_range("List is empty");
        }
        T value = head->value;
        Node* oldHead = head;
        head = head->next;
        if (head) {
            head->prev = nullptr;
        } else {
            tail = nullptr;
        }
        delete oldHead;
        count_--;
        return value;
    }

    void unshift(const T& value) {
        Node* newNode = new Node(value);
        if (!head) {
            head = tail = newNode;
        } else {
            newNode->next = head;
            head->prev = newNode;
            head = newNode;
        }
        count_++;
    }

    size_t count() const {
        return count_;
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
                count_--;
                return true;
            }
            current = current->next;
        }
        return false;
    }
};

}  // namespace linked_list
