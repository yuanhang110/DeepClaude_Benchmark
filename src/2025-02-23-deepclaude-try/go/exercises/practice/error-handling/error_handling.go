package erratum

func Use(opener ResourceOpener, input string) (err error) {
	var resource Resource
	
	// Keep trying to open until we get a non-transient error or success
	for {
		resource, err = opener()
		if err == nil {
			break
		}
		if _, ok := err.(TransientError); !ok {
			return err
		}
	}
	
	// Ensure resource is closed exactly once
	defer resource.Close()
	
	// Handle panics from Frob
	defer func() {
		if r := recover(); r != nil {
			if frobErr, ok := r.(FrobError); ok {
				resource.Defrob(frobErr.defrobTag)
			}
			// Set the return error to the panic value
			err = r.(error)
		}
	}()
	
	resource.Frob(input)
	return err
}
