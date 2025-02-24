package paasio

import "io"

import "sync"

type readCounter struct {
    reader     io.Reader
    byteCount  int64
    readCount  int
    readMutex  sync.Mutex
}

type writeCounter struct {
    writer     io.Writer
    byteCount  int64
    writeCount int
    writeMutex sync.Mutex
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
        readCounter: NewReadCounter(readwriter).(*readCounter),
        writeCounter: NewWriteCounter(readwriter).(*writeCounter),
    }
}

func (rc *readCounter) Read(p []byte) (int, error) {
    n, err := rc.reader.Read(p)
    rc.readMutex.Lock()
    rc.byteCount += int64(n)
    rc.readCount++
    rc.readMutex.Unlock()
    return n, err
}

func (rc *readCounter) ReadCount() (int64, int) {
    rc.readMutex.Lock()
    defer rc.readMutex.Unlock()
    return rc.byteCount, rc.readCount
}

func (wc *writeCounter) Write(p []byte) (int, error) {
    n, err := wc.writer.Write(p)
    wc.writeMutex.Lock()
    wc.byteCount += int64(n)
    wc.writeCount++
    wc.writeMutex.Unlock()
    return n, err
}

func (wc *writeCounter) WriteCount() (int64, int) {
    wc.writeMutex.Lock()
    defer wc.writeMutex.Unlock()
    return wc.byteCount, wc.writeCount
}
