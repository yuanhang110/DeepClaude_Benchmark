mod pre_implemented;

use std::ptr::{self, NonNull};
use std::marker::PhantomData;

struct Node<T> {
    data: T,
    prev: Option<NonNull<Node<T>>>,
    next: Option<NonNull<Node<T>>>,
}

pub struct LinkedList<T> {
    head: Option<NonNull<Node<T>>>,
    tail: Option<NonNull<Node<T>>>,
    len: usize,
    _marker: PhantomData<T>,
}

pub struct Cursor<'a, T> {
    current: Option<NonNull<Node<T>>>,
    list: &'a mut LinkedList<T>,
}

pub struct Iter<'a, T> {
    front: Option<NonNull<Node<T>>>,
    back: Option<NonNull<Node<T>>>,
    len: usize,
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

    pub fn is_empty(&self) -> bool {
        self.len == 0
    }

    pub fn len(&self) -> usize {
        self.len
    }

    pub fn cursor_front(&mut self) -> Cursor<'_, T> {
        Cursor {
            current: self.head,
            list: self,
        }
    }

    pub fn cursor_back(&mut self) -> Cursor<'_, T> {
        Cursor {
            current: self.tail,
            list: self,
        }
    }

    pub fn iter(&self) -> Iter<'_, T> {
        Iter {
            front: self.head,
            back: self.tail,
            len: self.len,
            _marker: PhantomData,
        }
    }
}

impl<T> Drop for LinkedList<T> {
    fn drop(&mut self) {
        let mut current = self.head;
        while let Some(node) = current {
            unsafe {
                let node = Box::from_raw(node.as_ptr());
                current = node.next;
            }
        }
    }
}

// the cursor is expected to act as if it is at the position of an element
// and it also has to work with and be able to insert into an empty list.
impl<T> Cursor<'_, T> {
    /// Take a mutable reference to the current element
    pub fn peek_mut(&mut self) -> Option<&mut T> {
        unsafe {
            self.current.map(|node| &mut (*node.as_ptr()).data)
        }
    }

    /// Move one position forward (towards the back) and return a reference to the new position
    #[allow(clippy::should_implement_trait)]
    pub fn next(&mut self) -> Option<&mut T> {
        unsafe {
            self.current = self.current.and_then(|node| (*node.as_ptr()).next);
            self.peek_mut()
        }
    }

    /// Move one position backward (towards the front) and return a reference to the new position
    pub fn prev(&mut self) -> Option<&mut T> {
        unsafe {
            self.current = self.current.and_then(|node| (*node.as_ptr()).prev);
            self.peek_mut()
        }
    }

    /// Remove and return the element at the current position
    pub fn take(&mut self) -> Option<T> {
        unsafe {
            let current = self.current?;
            let node = Box::from_raw(current.as_ptr());

            // Update previous node's next pointer
            if let Some(prev) = node.prev {
                (*prev.as_ptr()).next = node.next;
            } else {
                self.list.head = node.next;
            }

            // Update next node's previous pointer
            if let Some(next) = node.next {
                (*next.as_ptr()).prev = node.prev;
            } else {
                self.list.tail = node.prev;
            }

            // Move cursor to next if available, otherwise previous
            self.current = node.next.or(node.prev);
            self.list.len -= 1;

            Some(node.data)
        }
    }

    pub fn insert_after(&mut self, element: T) {
        unsafe {
            let new_node = Box::into_raw(Box::new(Node {
                data: element,
                prev: self.current,
                next: None,
            }));
            let new_node = NonNull::new(new_node).unwrap();

            if let Some(current) = self.current {
                let next = (*current.as_ptr()).next;
                (*new_node.as_ptr()).next = next;
                (*current.as_ptr()).next = Some(new_node);

                if let Some(next) = next {
                    (*next.as_ptr()).prev = Some(new_node);
                } else {
                    self.list.tail = Some(new_node);
                }
            } else { // Empty list
                self.list.head = Some(new_node);
                self.list.tail = Some(new_node);
            }

            self.list.len += 1;
        }
    }

    pub fn insert_before(&mut self, element: T) {
        unsafe {
            let new_node = Box::into_raw(Box::new(Node {
                data: element,
                prev: None,
                next: self.current,
            }));
            let new_node = NonNull::new(new_node).unwrap();

            if let Some(current) = self.current {
                let prev = (*current.as_ptr()).prev;
                (*new_node.as_ptr()).prev = prev;
                (*current.as_ptr()).prev = Some(new_node);

                if let Some(prev) = prev {
                    (*prev.as_ptr()).next = Some(new_node);
                } else {
                    self.list.head = Some(new_node);
                }
            } else { // Empty list
                self.list.head = Some(new_node);
                self.list.tail = Some(new_node);
            }

            self.list.len += 1;
        }
    }
}

impl<'a, T> Iterator for Iter<'a, T> {
    type Item = &'a T;

    fn next(&mut self) -> Option<&'a T> {
        if self.len == 0 {
            return None;
        }

        unsafe {
            self.front.map(|node| {
                self.len -= 1;
                self.front = (*node.as_ptr()).next;
                &(*node.as_ptr()).data
            })
        }
    }
}

impl<T> LinkedList<T> {
    // Safety: The list is covariant over T because we only store T and never use it in a way
    // that would require invariance. The pointers are properly wrapped in NonNull.
    // The list is Send/Sync when T is Send/Sync because we properly manage exclusive access
    // through the cursor and iterator APIs.
}

unsafe impl<T: Send> Send for LinkedList<T> {}
unsafe impl<T: Sync> Sync for LinkedList<T> {}
