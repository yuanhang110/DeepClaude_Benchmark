package linkedlist

import "errors"

// Define the List and Element types here.
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
	for i := len(elements) - 1; i >= 0; i-- {
		list.Push(elements[i])
	}
	return list
}

func (l *List) Size() int {
	return l.size
}

func (l *List) Push(element int) {
	l.head = &Element{Value: element, Next: l.head}
	l.size++
}

func (l *List) Pop() (int, error) {
	if l.head == nil {
		return 0, errors.New("empty list")
	}
	val := l.head.Value
	l.head = l.head.Next
	l.size--
	return val, nil
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
	elements := l.Array()
	for i, j := 0, len(elements)-1; i < j; i, j = i+1, j-1 {
		elements[i], elements[j] = elements[j], elements[i]
	}
	return New(elements)
}
