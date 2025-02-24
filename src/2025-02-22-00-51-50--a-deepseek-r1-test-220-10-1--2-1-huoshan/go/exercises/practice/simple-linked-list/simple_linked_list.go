package linkedlist

import "errors"

type Element struct {
	value int
	next  *Element
}

type List struct {
	head *Element
	size int
}

func New(elements []int) *List {
	l := &List{}
	// Add elements in normal order
	for i := 0; i < len(elements); i++ {
		l.Push(elements[i])
	}
	return l
}

func (l *List) Size() int {
	return l.size
}

func (l *List) Push(element int) {
	newElem := &Element{value: element}
	if l.head == nil {
		l.head = newElem
	} else {
		current := l.head
		for current.next != nil {
			current = current.next
		}
		current.next = newElem
	}
	l.size++
}

func (l *List) Pop() (int, error) {
	if l.head == nil {
		return 0, errors.New("list is empty")
	}

	// Handle single element case
	if l.head.next == nil {
		value := l.head.value
		l.head = nil
		l.size--
		return value, nil
	}

	// Find second-to-last element
	previous := l.head
	current := l.head.next
	for current.next != nil {
		previous = current
		current = current.next
	}

	value := current.value
	previous.next = nil
	l.size--
	return value, nil
}

func (l *List) Array() []int {
	result := []int{}
	current := l.head
	for current != nil {
		result = append(result, current.value)
		current = current.next
	}
	return result
}

func (l *List) Reverse() *List {
	current := l.head
	var reversedHead *Element
	for current != nil {
		next := current.next
		current.next = reversedHead
		reversedHead = current
		current = next
	}
	return &List{head: reversedHead, size: l.size}
}
