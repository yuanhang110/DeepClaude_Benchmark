#pragma once
#include <stdexcept>

namespace linked_list {

template <typename T>
class List {
private:
    struct Node {
        T value;
        Node* next;
        Node* prev;
        
        Node(const T& val) : value(val), next(nullptr), prev(nullptr) {}
    };

    Node* head;
    Node* tail;
    int count_;

public:
    List() : head(nullptr), tail(nullptr), count_(0) {}
    
    ~List() {
        while (head) {
            Node* temp = head;
            head = head->next;
            delete temp;
        }
    }

    void push(const T& value) {
        Node* newNode = new Node(value);
        if (!head) {
            head = tail = newNode;
        } else {
            newNode->prev = tail;
            tail->next = newNode;
            tail = newNode;
        }
        count_++;
    }

    T pop() {
        if (!head) {
            throw std::out_of_range("List is empty");
        }
        
        T value = tail->value;
        Node* toDelete = tail;
        
        if (head == tail) {
            head = tail = nullptr;
        } else {
            tail = tail->prev;
            tail->next = nullptr;
        }
        
        delete toDelete;
        count_--;
        return value;
    }

    T shift() {
        if (!head) {
            throw std::out_of_range("List is empty");
        }
        
        T value = head->value;
        Node* toDelete = head;
        
        if (head == tail) {
            head = tail = nullptr;
        } else {
            head = head->next;
            head->prev = nullptr;
        }
        
        delete toDelete;
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

    int count() const {
        return count_;
    }

    bool erase(const T& value) {
        Node* current = head;
        
        while (current) {
            if (current->value == value) {
                // Adjust pointers based on position
                if (current == head) {
                    head = current->next;
                } else {
                    current->prev->next = current->next;
                }
                
                if (current == tail) {
                    tail = current->prev;
                } else {
                    current->next->prev = current->prev;
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
