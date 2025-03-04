#pragma once

#include <stdexcept>
#include <cstddef>

namespace linked_list {

template <typename T>
struct Node {
    T value;
    Node* prev;
    Node* next;
    Node(const T& v) : value(v), prev(nullptr), next(nullptr) {}
};

template <typename T>
class List {
private:
    Node<T>* head;
    Node<T>* tail;
    size_t node_count;

public:
    List() : head(nullptr), tail(nullptr), node_count(0) {}

    ~List() {
        Node<T>* current = head;
        while (current) {
            Node<T>* next = current->next;
            delete current;
            current = next;
        }
    }

    void push(const T& value) {
        Node<T>* newNode = new Node<T>(value);
        if (!tail) {
            head = tail = newNode;
        } else {
            tail->next = newNode;
            newNode->prev = tail;
            tail = newNode;
        }
        node_count++;
    }

    T pop() {
        if (!tail) throw std::out_of_range("List is empty");
        Node<T>* oldTail = tail;
        T value = oldTail->value;
        tail = oldTail->prev;
        if (tail) {
            tail->next = nullptr;
        } else {
            head = nullptr;
        }
        delete oldTail;
        node_count--;
        return value;
    }

    T shift() {
        if (!head) throw std::out_of_range("List is empty");
        Node<T>* oldHead = head;
        T value = oldHead->value;
        head = oldHead->next;
        if (head) {
            head->prev = nullptr;
        } else {
            tail = nullptr;
        }
        delete oldHead;
        node_count--;
        return value;
    }

    void unshift(const T& value) {
        Node<T>* newNode = new Node<T>(value);
        if (!head) {
            head = tail = newNode;
        } else {
            head->prev = newNode;
            newNode->next = head;
            head = newNode;
        }
        node_count++;
    }

    size_t count() const {
        return node_count;
    }

    bool erase(const T& value) {
        Node<T>* current = head;
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
                node_count--;
                return true;
            }
            current = current->next;
        }
        return false;
    }
};

}  // namespace linked_list
