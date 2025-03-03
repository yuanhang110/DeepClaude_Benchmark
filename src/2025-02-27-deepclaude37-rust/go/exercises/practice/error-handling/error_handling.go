package erratum

import "fmt"

func Use(opener ResourceOpener, input string) (err error) {
	var resource Resource
	for {
		resource, err = opener()
		if err == nil {
			break
		}
		if _, ok := err.(TransientError); ok {
			continue
		}
		return err
	}
	defer resource.Close()

	defer func() {
		if r := recover(); r != nil {
			if frobErr, ok := r.(FrobError); ok {
				resource.Defrob(frobErr.defrobTag)
				err = frobErr
			} else if otherErr, ok := r.(error); ok {
				err = otherErr
			} else {
				err = fmt.Errorf("%v", r)
			}
		}
	}()

	resource.Frob(input)
	return nil
}
