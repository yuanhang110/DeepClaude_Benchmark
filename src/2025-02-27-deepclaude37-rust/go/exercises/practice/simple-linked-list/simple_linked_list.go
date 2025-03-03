package linkedlist

import "errors"

// Element represents a node in our linked list
type Element struct {
	Value int
	Next  *Element
}

// List represents a singly linked list with a head pointer and size counter
type List struct {
	head *Element
	size int
}

func New(elements []int) *List {
	l := &List{}
	for _, e := range elements {
		l.Push(e)
	}
	return l
}

func (l *List) Size() int {
	return l.size
}

func (l *List) Push(element int) {
	newElement := &Element{Value: element}
	if l.head == nil {
		l.head = newElement
	} else {
		current := l.head
		for current.Next != nil {
			current = current.Next
		}
		current.Next = newElement
	}
	l.size++
}

func (l *List) Pop() (int, error) {
	if l.head == nil {
		return 0, errors.New("empty list")
	}

	// If there's only one element
	if l.head.Next == nil {
		value := l.head.Value
		l.head = nil
		l.size--
		return value, nil
	}

	// Find the second-to-last element
	current := l.head
	for current.Next.Next != nil {
		current = current.Next
	}

	// Get the value of the last element
	value := current.Next.Value
	// Remove the last element
	current.Next = nil
	l.size--
	return value, nil
}

func (l *List) Array() []int {
	arr := make([]int, 0, l.size)
	current := l.head
	for current != nil {
		arr = append(arr, current.Value)
		current = current.Next
	}
	return arr
}

func (l *List) Reverse() *List {
	reversed := &List{}
	current := l.head
	for current != nil {
		// Prepend to the reversed list (stack-like push)
		reversed.head = &Element{Value: current.Value, Next: reversed.head}
		reversed.size++
		current = current.Next
	}
	return reversed
}
