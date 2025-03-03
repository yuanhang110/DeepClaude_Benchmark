package paasio

import (
	"io"
	"sync"
)

type readCounter struct {
	reader io.Reader
	mutex  sync.Mutex
	bytes  int64
	ops    int
}

type writeCounter struct {
	writer io.Writer
	mutex  sync.Mutex
	bytes  int64
	ops    int
}

type readWriteCounter struct {
	*readCounter
	*writeCounter
}

func NewWriteCounter(writer io.Writer) WriteCounter {
	return &writeCounter{
		writer: writer,
	}
}

func NewReadCounter(reader io.Reader) ReadCounter {
	return &readCounter{
		reader: reader,
	}
}

func NewReadWriteCounter(readwriter io.ReadWriter) ReadWriteCounter {
	return &readWriteCounter{
		readCounter:  NewReadCounter(readwriter).(*readCounter),
		writeCounter: NewWriteCounter(readwriter).(*writeCounter),
	}
}

func (rc *readCounter) Read(p []byte) (int, error) {
	n, err := rc.reader.Read(p)
	if n > 0 {
		rc.mutex.Lock()
		rc.bytes += int64(n)
		rc.ops++
		rc.mutex.Unlock()
	}
	return n, err
}

func (rc *readCounter) ReadCount() (int64, int) {
	rc.mutex.Lock()
	defer rc.mutex.Unlock()
	return rc.bytes, rc.ops
}

func (wc *writeCounter) Write(p []byte) (int, error) {
	n, err := wc.writer.Write(p)
	if n > 0 {
		wc.mutex.Lock()
		wc.bytes += int64(n)
		wc.ops++
		wc.mutex.Unlock()
	}
	return n, err
}

func (wc *writeCounter) WriteCount() (int64, int) {
	wc.mutex.Lock()
	defer wc.mutex.Unlock()
	return wc.bytes, wc.ops
}
