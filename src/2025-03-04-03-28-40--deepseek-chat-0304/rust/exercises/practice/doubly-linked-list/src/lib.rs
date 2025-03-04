mod pre_implemented;

use std::ptr::{self, NonNull};
use std::marker::PhantomData;

struct Node<T> {
    element: T,
    next: Option<NonNull<Node<T>>>,
    prev: Option<NonNull<Node<T>>>,
}

pub struct LinkedList<T> {
    head: Option<NonNull<Node<T>>>,
    tail: Option<NonNull<Node<T>>>,
    len: usize,
    _marker: PhantomData<Box<Node<T>>>,
}

pub struct Cursor<'a, T> {
    list: &'a mut LinkedList<T>,
    curr: Option<NonNull<Node<T>>>,
}

pub struct Iter<'a, T> {
    head: Option<NonNull<Node<T>>>,
    len: usize,
    _marker: PhantomData<&'a Node<T>>,
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
        let curr = self.head;
        Cursor {
            list: self,
            curr,
        }
    }

    pub fn cursor_back(&mut self) -> Cursor<'_, T> {
        let curr = self.tail;
        Cursor {
            list: self,
            curr,
        }
    }

    pub fn iter(&self) -> Iter<'_, T> {
        Iter {
            head: self.head,
            len: self.len,
            _marker: PhantomData,
        }
    }
}

impl<T> Drop for LinkedList<T> {
    fn drop(&mut self) {
        while self.pop_front().is_some() {}
    }
}

impl<T> Cursor<'_, T> {
    pub fn peek_mut(&mut self) -> Option<&mut T> {
        unsafe {
            self.curr.map(|node| &mut (*node.as_ptr()).element)
        }
    }

    pub fn next(&mut self) -> Option<&mut T> {
        unsafe {
            self.curr = self.curr.and_then(|node| (*node.as_ptr()).next);
            self.peek_mut()
        }
    }

    pub fn prev(&mut self) -> Option<&mut T> {
        unsafe {
            self.curr = self.curr.and_then(|node| (*node.as_ptr()).prev);
            self.peek_mut()
        }
    }

    pub fn take(&mut self) -> Option<T> {
        unsafe {
            self.curr.map(|node| {
                let node = Box::from_raw(node.as_ptr());
                
                match node.prev {
                    Some(prev) => (*prev.as_ptr()).next = node.next,
                    None => self.list.head = node.next,
                }
                
                match node.next {
                    Some(next) => (*next.as_ptr()).prev = node.prev,
                    None => self.list.tail = node.prev,
                }
                
                self.list.len -= 1;
                self.curr = if node.next.is_some() {
                    node.next
                } else {
                    node.prev
                };
                
                node.element
            })
        }
    }

    pub fn insert_after(&mut self, element: T) {
        unsafe {
            let new_node = Box::into_raw(Box::new(Node {
                element,
                next: None,
                prev: None,
            }));
            let new_node = NonNull::new(new_node).unwrap();
            
            if let Some(curr) = self.curr {
                (*new_node.as_ptr()).next = (*curr.as_ptr()).next;
                (*new_node.as_ptr()).prev = Some(curr);
                
                if let Some(next) = (*curr.as_ptr()).next {
                    (*next.as_ptr()).prev = Some(new_node);
                } else {
                    self.list.tail = Some(new_node);
                }
                
                (*curr.as_ptr()).next = Some(new_node);
            } else {
                (*new_node.as_ptr()).next = self.list.head;
                
                if let Some(head) = self.list.head {
                    (*head.as_ptr()).prev = Some(new_node);
                } else {
                    self.list.tail = Some(new_node);
                }
                
                self.list.head = Some(new_node);
            }
            
            self.list.len += 1;
        }
    }

    pub fn insert_before(&mut self, element: T) {
        unsafe {
            let new_node = Box::into_raw(Box::new(Node {
                element,
                next: None,
                prev: None,
            }));
            let new_node = NonNull::new(new_node).unwrap();
            
            if let Some(curr) = self.curr {
                (*new_node.as_ptr()).prev = (*curr.as_ptr()).prev;
                (*new_node.as_ptr()).next = Some(curr);
                
                if let Some(prev) = (*curr.as_ptr()).prev {
                    (*prev.as_ptr()).next = Some(new_node);
                } else {
                    self.list.head = Some(new_node);
                }
                
                (*curr.as_ptr()).prev = Some(new_node);
            } else {
                (*new_node.as_ptr()).prev = self.list.tail;
                
                if let Some(tail) = self.list.tail {
                    (*tail.as_ptr()).next = Some(new_node);
                } else {
                    self.list.head = Some(new_node);
                }
                
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
            None
        } else {
            self.head.map(|node| unsafe {
                self.len -= 1;
                self.head = (*node.as_ptr()).next;
                &(*node.as_ptr()).element
            })
        }
    }
}

// Safety: The LinkedList is safe to send to another thread because we don't share
// any data between threads. The raw pointers are owned by the LinkedList.
unsafe impl<T: Send> Send for LinkedList<T> {}

// Safety: The LinkedList is safe to share between threads because we ensure
// synchronization when accessing the data through the cursor.
unsafe impl<T: Sync> Sync for LinkedList<T> {}
