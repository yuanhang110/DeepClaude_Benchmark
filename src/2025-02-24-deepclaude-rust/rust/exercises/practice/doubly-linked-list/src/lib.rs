// this module adds some functionality based on the required implementations
// here like: `LinkedList::pop_back` or `Clone for LinkedList<T>`
// You are free to use anything in it, but it's mainly for the test framework.
mod pre_implemented;

// A node in the linked list
struct Node<T> {
    data: T,
    prev: *mut Node<T>,
    next: *mut Node<T>,
}

pub struct LinkedList<T> {
    head: *mut Node<T>,
    tail: *mut Node<T>,
    len: usize,
    _marker: std::marker::PhantomData<T>,
}

pub struct Cursor<'a, T> {
    list: &'a mut LinkedList<T>,
    current: *mut Node<T>,
}

pub struct Iter<'a, T> {
    current: *mut Node<T>,
    _marker: std::marker::PhantomData<&'a T>,
}

impl<T> LinkedList<T> {
    pub fn new() -> Self {
        Self {
            head: std::ptr::null_mut(),
            tail: std::ptr::null_mut(),
            len: 0,
            _marker: std::marker::PhantomData,
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
        let current_head = self.head;
        Cursor {
            list: self,
            current: current_head,
        }
    }

    /// Return a cursor positioned on the back element
    pub fn cursor_back(&mut self) -> Cursor<'_, T> {
        let current_tail = self.tail;
        Cursor {
            list: self,
            current: current_tail,
        }
    }

    /// Return an iterator that moves from front to back
    pub fn iter(&self) -> Iter<'_, T> {
        Iter {
            current: self.head,
            _marker: std::marker::PhantomData,
        }
    }
}

// the cursor is expected to act as if it is at the position of an element
// and it also has to work with and be able to insert into an empty list.
impl<T> Cursor<'_, T> {
    /// Take a mutable reference to the current element
    pub fn peek_mut(&mut self) -> Option<&mut T> {
        if self.current.is_null() {
            None
        } else {
            // SAFETY: current is not null and we have exclusive access via the cursor's
            // mutable reference to the list
            unsafe { Some(&mut (*self.current).data) }
        }
    }

    /// Move one position forward (towards the back) and
    /// return a reference to the new position
    #[allow(clippy::should_implement_trait)]
    pub fn next(&mut self) -> Option<&mut T> {
        if self.current.is_null() {
            return None;
        }
        // SAFETY: current is not null, so we can safely read next
        let next = unsafe { (*self.current).next };
        if next.is_null() {
            None
        } else {
            self.current = next;
            // SAFETY: next is not null and we have exclusive access
            unsafe { Some(&mut (*self.current).data) }
        }
    }

    /// Move one position backward (towards the front) and
    /// return a reference to the new position
    pub fn prev(&mut self) -> Option<&mut T> {
        if self.current.is_null() {
            return None;
        }
        // SAFETY: current is not null, so we can safely read prev
        let prev = unsafe { (*self.current).prev };
        if prev.is_null() {
            None
        } else {
            self.current = prev;
            // SAFETY: prev is not null and we have exclusive access
            unsafe { Some(&mut (*self.current).data) }
        }
    }

    /// Remove and return the element at the current position and move the cursor
    /// to the neighboring element that's closest to the back. This can be
    /// either the next or previous position.
    pub fn take(&mut self) -> Option<T> {
        if self.current.is_null() {
            return None;
        }

        // SAFETY: current is not null, we have exclusive access, and we're taking ownership
        let current = unsafe { Box::from_raw(self.current) };
        let prev = current.prev;
        let next = current.next;

        if prev.is_null() {
            self.list.head = next;
        } else {
            // SAFETY: prev is not null, so we can update its next pointer
            unsafe { (*prev).next = next; }
        }

        if next.is_null() {
            self.list.tail = prev;
        } else {
            // SAFETY: next is not null, so we can update its prev pointer
            unsafe { (*next).prev = prev; }
        }

        self.list.len -= 1;

        // Move cursor to next if possible, else to prev
        self.current = if !next.is_null() {
            next
        } else {
            prev
        };

        Some(current.data)
    }

    pub fn insert_after(&mut self, element: T) {
        let new_node = Box::into_raw(Box::new(Node {
            data: element,
            prev: self.current,
            next: if self.current.is_null() {
                std::ptr::null_mut()
            } else {
                // SAFETY: current is not null here
                unsafe { (*self.current).next }
            },
        }));

        if self.current.is_null() {
            // Inserting into empty list
            self.list.head = new_node;
            self.list.tail = new_node;
        } else {
            // SAFETY: current is not null
            let next = unsafe { (*self.current).next };
            if next.is_null() {
                self.list.tail = new_node;
            } else {
                // SAFETY: next is not null
                unsafe { (*next).prev = new_node; }
            }
            // SAFETY: current is not null
            unsafe { (*self.current).next = new_node; }
        }

        self.list.len += 1;
    }

    pub fn insert_before(&mut self, element: T) {
        let new_node = Box::into_raw(Box::new(Node {
            data: element,
            next: self.current,
            prev: if self.current.is_null() {
                std::ptr::null_mut()
            } else {
                // SAFETY: current is not null here
                unsafe { (*self.current).prev }
            },
        }));

        if self.current.is_null() {
            // Inserting into empty list
            self.list.head = new_node;
            self.list.tail = new_node;
        } else {
            // SAFETY: current is not null
            let prev = unsafe { (*self.current).prev };
            if prev.is_null() {
                self.list.head = new_node;
            } else {
                // SAFETY: prev is not null
                unsafe { (*prev).next = new_node; }
            }
            // SAFETY: current is not null
            unsafe { (*self.current).prev = new_node; }
        }

        self.list.len += 1;
    }
}

impl<'a, T> Iterator for Iter<'a, T> {
    type Item = &'a T;

    fn next(&mut self) -> Option<&'a T> {
        if self.current.is_null() {
            None
        } else {
            // SAFETY: current is not null and points to a valid node
            // The Node cannot be deallocated while we have a reference to it
            let result = unsafe { &(*self.current).data };
            self.current = unsafe { (*self.current).next };
            Some(result)
        }
    }
}

impl<T> Drop for LinkedList<T> {
    fn drop(&mut self) {
        let mut current = self.head;
        while !current.is_null() {
            // SAFETY: current is a valid pointer, and we have unique ownership
            let node = unsafe { Box::from_raw(current) };
            current = node.next;
            // node is dropped here, deallocating it
        }
    }
}
