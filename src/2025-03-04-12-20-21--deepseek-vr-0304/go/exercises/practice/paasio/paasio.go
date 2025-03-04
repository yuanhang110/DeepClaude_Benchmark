package paasio

import (
	"io"
	"sync"
)

type readCounter struct {
	reader  io.Reader
	mu      sync.Mutex
	bytes   int64
	ops     int
}

type writeCounter struct {
	writer  io.Writer
	mu      sync.Mutex
	bytes   int64
	ops     int
}

// For the return of the function NewReadWriteCounter, you must also define a type that satisfies the ReadWriteCounter interface.

func NewWriteCounter(writer io.Writer) WriteCounter {
	return &writeCounter{writer: writer}
}

func NewReadCounter(reader io.Reader) ReadCounter {
	return &readCounter{reader: reader}
}

func NewReadWriteCounter(readwriter io.ReadWriter) ReadWriteCounter {
	panic("Please implement the NewReadWriteCounter function")
}

func (rc *readCounter) Read(p []byte) (int, error) {
	panic("Please implement the Read function")
}

func (rc *readCounter) ReadCount() (int64, int) {
	panic("Please implement the ReadCount function")
}

func (wc *writeCounter) Write(p []byte) (int, error) {
	panic("Please implement the Write function")
}

func (wc *writeCounter) WriteCount() (int64, int) {
	panic("Please implement the WriteCount function")
}
