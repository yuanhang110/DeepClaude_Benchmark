package linkedlist

import "errors"

type Element struct {
    Value int
    Next  *Element
}

type List struct {
    head *Element
    size int
}

func New(elements []int) *List {
	list := &List{}
	for _, v := range elements {
		list.Push(v)
	}
	return list
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
	if l.size == 0 {
		return 0, errors.New("list is empty")
	}

	var value int
	if l.size == 1 {
		value = l.head.Value
		l.head = nil
	} else {
		current := l.head
		for current.Next.Next != nil {
			current = current.Next
		}
		value = current.Next.Value
		current.Next = nil
	}
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
		reversed.head = &Element{Value: current.Value, Next: reversed.head}
		reversed.size++
		current = current.Next
	}
	return reversed
}
