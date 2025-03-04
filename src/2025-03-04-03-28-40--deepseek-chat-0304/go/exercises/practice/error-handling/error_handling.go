package erratum

func Use(opener ResourceOpener, input string) (err error) {
	var resource Resource
	
	// Keep trying to open resource until we get a non-transient error or success
	for {
		resource, err = opener()
		if err == nil {
			break
		}
		if _, ok := err.(TransientError); !ok {
			return err
		}
	}
	
	// Ensure resource is closed no matter what happens
	defer resource.Close()
	
	// Handle potential panics during Frob
	defer func() {
		if r := recover(); r != nil {
			if frobErr, ok := r.(FrobError); ok {
				resource.Defrob(frobErr.defrobTag)
				err = frobErr
			} else if panicErr, ok := r.(error); ok {
				err = panicErr
			} else {
				panic(r) // re-panic if it's not an error type
			}
		}
	}()
	
	// Call Frob and return any error
	resource.Frob(input)
	return err
}
