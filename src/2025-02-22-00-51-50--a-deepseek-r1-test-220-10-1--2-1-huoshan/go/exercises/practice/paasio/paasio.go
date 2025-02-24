package paasio

import "io"

import (
	"sync"
)

type readCounter struct {
	mu    sync.Mutex
	nBytes int64
	nOps   int
	reader io.Reader
}

type writeCounter struct {
	mu     sync.Mutex
	nBytes int64
	nOps   int
	writer io.Writer
}

type rwCounter struct {
	*readCounter
	*writeCounter
}

func NewWriteCounter(writer io.Writer) WriteCounter {
	return &writeCounter{writer: writer}
}

func NewReadCounter(reader io.Reader) ReadCounter {
	return &readCounter{reader: reader}
}

func NewReadWriteCounter(rw io.ReadWriter) ReadWriteCounter {
	return &rwCounter{
		NewReadCounter(rw).(*readCounter),
		NewWriteCounter(rw).(*writeCounter),
	}
}

func (rc *readCounter) Read(p []byte) (int, error) {
	n, err := rc.reader.Read(p)
	rc.mu.Lock()
	rc.nBytes += int64(n)
	rc.nOps++
	rc.mu.Unlock()
	return n, err
}

func (rc *readCounter) ReadCount() (int64, int) {
	rc.mu.Lock()
	defer rc.mu.Unlock()
	return rc.nBytes, rc.nOps
}

func (wc *writeCounter) Write(p []byte) (int, error) {
	n, err := wc.writer.Write(p)
	wc.mu.Lock()
	wc.nBytes += int64(n)
	wc.nOps++
	wc.mu.Unlock()
	return n, err
}

func (wc *writeCounter) WriteCount() (int64, int) {
	wc.mu.Lock()
	defer wc.mu.Unlock()
	return wc.nBytes, wc.nOps
}
