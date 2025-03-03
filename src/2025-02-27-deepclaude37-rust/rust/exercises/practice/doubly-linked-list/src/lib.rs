// this module adds some functionality based on the required implementations
// here like: `LinkedList::pop_back` or `Clone for LinkedList<T>`
// You are free to use anything in it, but it's mainly for the test framework.
mod pre_implemented;

use std::ptr::NonNull;
use std::marker::PhantomData;

// Node structure for our doubly linked list
struct Node<T> {
    data: T,
    prev: Option<NonNull<Node<T>>>,
    next: Option<NonNull<Node<T>>>,
}

pub struct LinkedList<T> {
    head: Option<NonNull<Node<T>>>,
    tail: Option<NonNull<Node<T>>>,
    len: usize,
    // PhantomData to claim ownership of T
    _marker: PhantomData<T>,
}

pub struct Cursor<'a, T> {
    list: &'a mut LinkedList<T>,
    current: Option<NonNull<Node<T>>>,
}

pub struct Iter<'a, T> {
    current: Option<NonNull<Node<T>>>,
    _marker: PhantomData<&'a T>,
}

impl<T> LinkedList<T> {
    pub fn new() -> Self {
        Self {
            head: None,
            tail: None,
            len: 0,
            _marker: PhantomData,
        }
    }

    // You may be wondering why it's necessary to have is_empty()
    // when it can easily be determined from len().
    // It's good custom to have both because len() can be expensive for some types,
    // whereas is_empty() is almost always cheap.
    // (Also ask yourself whether len() is expensive for LinkedList)
    pub fn is_empty(&self) -> bool {
        self.len == 0
    }

    pub fn len(&self) -> usize {
        self.len
    }


    /// Return a cursor positioned on the front element
    pub fn cursor_front(&mut self) -> Cursor<'_, T> {
        let current = self.head;
        Cursor {
            list: self,
            current,
        }
    }

    /// Return a cursor positioned on the back element
    pub fn cursor_back(&mut self) -> Cursor<'_, T> {
        let current = self.tail;
        Cursor {
            list: self,
            current,
        }
    }

    /// Return an iterator that moves from front to back
    pub fn iter(&self) -> Iter<'_, T> {
        Iter {
            current: self.head,
            _marker: PhantomData,
        }
    }
}

// Implementation of Drop to clean up resources when the LinkedList is dropped
impl<T> Drop for LinkedList<T> {
    fn drop(&mut self) {
        // Manually deallocate all nodes
        while let Some(node_ptr) = self.head {
            unsafe {
                // Update head to next node
                let node = Box::from_raw(node_ptr.as_ptr());
                self.head = node.next;
                // Node is deallocated when Box is dropped
            }
        }
        // Clear tail pointer as well
        self.tail = None;
        self.len = 0;
    }
}

// the cursor is expected to act as if it is at the position of an element
// and it also has to work with and be able to insert into an empty list.
impl<T> Cursor<'_, T> {
    /// Take a mutable reference to the current element
    pub fn peek_mut(&mut self) -> Option<&mut T> {
        // Safety: We're returning a reference to data in a valid node
        self.current.map(|ptr| unsafe { &mut (*ptr.as_ptr()).data })
    }

    /// Move one position forward (towards the back) and
    /// return a reference to the new position
    #[allow(clippy::should_implement_trait)]
    pub fn next(&mut self) -> Option<&mut T> {
        // Safety: We're following the next pointer which must be valid or None
        if let Some(current) = self.current {
            unsafe {
                self.current = (*current.as_ptr()).next;
                self.peek_mut()
            }
        } else {
            None
        }
    }

    /// Move one position backward (towards the front) and
    /// return a reference to the new position
    pub fn prev(&mut self) -> Option<&mut T> {
        // Safety: We're following the prev pointer which must be valid or None
        if let Some(current) = self.current {
            unsafe {
                self.current = (*current.as_ptr()).prev;
                self.peek_mut()
            }
        } else {
            None
        }
    }

    /// Remove and return the element at the current position and move the cursor
    /// to the neighboring element that's closest to the back. This can be
    /// either the next or previous position.
    pub fn take(&mut self) -> Option<T> {
        let current = self.current?;
        
        unsafe {
            // Get the node we're removing
            let node_ptr = current.as_ptr();
            
            // Get the prev and next pointers
            let prev = (*node_ptr).prev;
            let next = (*node_ptr).next;
            
            // Update the linked list pointers
            if let Some(prev) = prev {
                (*prev.as_ptr()).next = next;
            } else {
                // This was the head node
                self.list.head = next;
            }
            
            if let Some(next) = next {
                (*next.as_ptr()).prev = prev;
            } else {
                // This was the tail node
                self.list.tail = prev;
            }
            
            // Position cursor at next node if available, otherwise prev node
            self.current = next.or(prev);
            
            // Take ownership of the node and return its data
            let node = Box::from_raw(node_ptr);
            self.list.len -= 1;
            Some(node.data)
        }
    }

    pub fn insert_after(&mut self, element: T) {
        // Safety: We're creating a new node and carefully updating all pointers
        unsafe {
            let new_node = Box::new(Node {
                data: element,
                prev: self.current,
                next: None,
            });
            let new_ptr = NonNull::new(Box::into_raw(new_node)).unwrap();
            
            if let Some(current) = self.current {
                // Get the next node (if any) of the current node
                let next = (*current.as_ptr()).next;
                
                // Link new node to next node if it exists
                (*new_ptr.as_ptr()).next = next;
                
                // Update next node's prev to point to new node
                if let Some(next) = next {
                    (*next.as_ptr()).prev = Some(new_ptr);
                } else {
                    // If current was tail, new node becomes tail
                    self.list.tail = Some(new_ptr);
                }
                
                // Update current node's next to point to new node
                (*current.as_ptr()).next = Some(new_ptr);
            } else {
                // List is empty, new node becomes both head and tail
                self.list.head = Some(new_ptr);
                self.list.tail = Some(new_ptr);
                self.current = Some(new_ptr);
            }
            
            self.list.len += 1;
        }
    }

    pub fn insert_before(&mut self, element: T) {
        // Safety: We're creating a new node and carefully updating all pointers
        unsafe {
            let new_node = Box::new(Node {
                data: element,
                prev: None,
                next: self.current,
            });
            let new_ptr = NonNull::new(Box::into_raw(new_node)).unwrap();
            
            if let Some(current) = self.current {
                // Get the prev node (if any) of the current node
                let prev = (*current.as_ptr()).prev;
                
                // Link new node to prev node if it exists
                (*new_ptr.as_ptr()).prev = prev;
                
                // Update prev node's next to point to new node
                if let Some(prev) = prev {
                    (*prev.as_ptr()).next = Some(new_ptr);
                } else {
                    // If current was head, new node becomes head
                    self.list.head = Some(new_ptr);
                }
                
                // Update current node's prev to point to new node
                (*current.as_ptr()).prev = Some(new_ptr);
            } else {
                // List is empty, new node becomes both head and tail
                self.list.head = Some(new_ptr);
                self.list.tail = Some(new_ptr);
                self.current = Some(new_ptr);
            }
            
            self.list.len += 1;
        }
    }
}

impl<'a, T> Iterator for Iter<'a, T> {
    type Item = &'a T;

    fn next(&mut self) -> Option<&'a T> {
        // Use the current pointer, then advance to the next node
        self.current.map(|node| unsafe {
            // Get a reference to the data at the current node
            let data = &(*node.as_ptr()).data;
            
            // Move to the next node
            self.current = (*node.as_ptr()).next;
            
            // Return the data reference
            data
        })
    }
}

// Implement Send and Sync manually
unsafe impl<T: Send> Send for LinkedList<T> {}
unsafe impl<T: Sync> Sync for LinkedList<T> {}
