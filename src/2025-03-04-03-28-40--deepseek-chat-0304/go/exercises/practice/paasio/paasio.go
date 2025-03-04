package paasio

import (
	"io"
	"sync"
)

type readCounter struct {
	reader  io.Reader
	bytes   int64
	ops     int
	mu      sync.Mutex
}

type writeCounter struct {
	writer  io.Writer
	bytes   int64
	ops     int
	mu      sync.Mutex
}

type readWriteCounter struct {
	ReadCounter
	WriteCounter
}

func NewWriteCounter(writer io.Writer) WriteCounter {
	return &writeCounter{writer: writer}
}

func NewReadCounter(reader io.Reader) ReadCounter {
	return &readCounter{reader: reader}
}

func NewReadWriteCounter(readwriter io.ReadWriter) ReadWriteCounter {
	return &readWriteCounter{
		ReadCounter:  NewReadCounter(readwriter),
		WriteCounter: NewWriteCounter(readwriter),
	}
}

func (rc *readCounter) Read(p []byte) (int, error) {
	rc.mu.Lock()
	defer rc.mu.Unlock()
	
	n, err := rc.reader.Read(p)
	rc.bytes += int64(n)
	rc.ops++
	return n, err
}

func (rc *readCounter) ReadCount() (int64, int) {
	rc.mu.Lock()
	defer rc.mu.Unlock()
	return rc.bytes, rc.ops
}

func (wc *writeCounter) Write(p []byte) (int, error) {
	wc.mu.Lock()
	defer wc.mu.Unlock()
	
	n, err := wc.writer.Write(p)
	wc.bytes += int64(n)
	wc.ops++
	return n, err
}

func (wc *writeCounter) WriteCount() (int64, int) {
	wc.mu.Lock()
	defer wc.mu.Unlock()
	return wc.bytes, wc.ops
}
