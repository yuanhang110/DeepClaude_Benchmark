#include "linked_list.h"
#include <stdexcept>

namespace linked_list {

template <typename T>
List<T>::~List() {
    while (head) {
        Node<T>* temp = head;
        head = head->next;
        delete temp;
    }
}

template <typename T>
void List<T>::push(T value) {
    Node<T>* newNode = new Node<T>(value);
    if (!tail) {
        head = tail = newNode;
    } else {
        tail->next = newNode;
        newNode->prev = tail;
        tail = newNode;
    }
    size++;
}

template <typename T>
T List<T>::pop() {
    if (!tail) {
        throw std::out_of_range("Cannot pop from empty list");
    }
    Node<T>* temp = tail;
    T value = temp->value;
    tail = tail->prev;
    if (tail) {
        tail->next = nullptr;
    } else {
        head = nullptr;
    }
    delete temp;
    size--;
    return value;
}

template <typename T>
void List<T>::unshift(T value) {
    Node<T>* newNode = new Node<T>(value);
    if (!head) {
        head = tail = newNode;
    } else {
        newNode->next = head;
        head->prev = newNode;
        head = newNode;
    }
    size++;
}

template <typename T>
T List<T>::shift() {
    if (!head) {
        throw std::out_of_range("Cannot shift from empty list");
    }
    Node<T>* temp = head;
    T value = temp->value;
    head = head->next;
    if (head) {
        head->prev = nullptr;
    } else {
        tail = nullptr;
    }
    delete temp;
    size--;
    return value;
}

template <typename T>
int List<T>::count() const {
    return size;
}

template <typename T>
bool List<T>::erase(T value) {
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
            size--;
            return true;
        }
        current = current->next;
    }
    return false;
}

}  // namespace linked_list
