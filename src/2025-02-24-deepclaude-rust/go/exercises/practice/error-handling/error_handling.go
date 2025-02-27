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
	defer res.Close()

	defer func() {
		if r := recover(); r != nil {
			if frobErr, ok := r.(FrobError); ok {
				res.Defrob(frobErr.defrobTag)
				err = frobErr
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
