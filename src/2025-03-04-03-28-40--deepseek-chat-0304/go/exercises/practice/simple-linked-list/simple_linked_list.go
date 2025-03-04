package linkedlist

import (
	"errors"
)

type Element struct {
	value int
	next  *Element
}

type List struct {
	head *Element
	tail *Element
	size int
}

func New(elements []int) *List {
	l := &List{}
	for _, v := range elements {
		l.Push(v)
	}
	return l
}

func (l *List) Size() int {
	return l.size
}

func (l *List) Push(element int) {
	newElement := &Element{value: element}
	if l.head == nil {
		l.head = newElement
	} else {
		l.tail.next = newElement
	}
	l.tail = newElement
	l.size++
}

func (l *List) Pop() (int, error) {
	if l.head == nil {
		return 0, errors.New("list is empty")
	}
	value := l.head.value
	l.head = l.head.next
	l.size--
	return value, nil
}

func (l *List) Array() []int {
	arr := make([]int, 0, l.size)
	current := l.head
	for current != nil {
		arr = append(arr, current.value)
		current = current.next
	}
	return arr
}

func (l *List) Reverse() *List {
	reversed := &List{}
	current := l.head
	for current != nil {
		// Push to head to reverse the order
		newElement := &Element{value: current.value, next: reversed.head}
		reversed.head = newElement
		if reversed.tail == nil {
			reversed.tail = newElement
		}
		reversed.size++
		current = current.next
	}
	return reversed
}
