package erratum

import "fmt"

func Use(opener ResourceOpener, input string) (err error) {
	var res Resource
	for {
		res, err = opener()
		if err != nil {
			if _, ok := err.(TransientError); ok {
				continue
			}
			return err
		}
		break
	}

	defer func() {
		if closeErr := res.Close(); closeErr != nil {
			if err == nil {
				err = closeErr
			}
		}
	}()

	defer func() {
		if r := recover(); r != nil {
			if fe, ok := r.(FrobError); ok {
				res.Defrob(fe.defrobTag)
				err = fe
			} else if e, ok := r.(error); ok {
				err = e
			} else {
				err = fmt.Errorf("%v", r)
			}
		}
	}()

	res.Frob(input)
	return nil
}
