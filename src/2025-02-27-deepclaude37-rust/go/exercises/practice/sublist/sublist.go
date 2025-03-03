package sublist

// Relation type is defined in relations.go file.

func Sublist(l1, l2 []int) Relation {
	if equal(l1, l2) {
		return RelationEqual
	}
	if isSublist(l1, l2) {
		return RelationSublist
	}
	if isSublist(l2, l1) {
		return RelationSuperlist
	}
	return RelationUnequal
}

// equal checks if two slices have the same elements in the same order
func equal(a, b []int) bool {
	if len(a) != len(b) {
		return false
	}
	for i := range a {
		if a[i] != b[i] {
			return false
		}
	}
	return true
}

// isSublist checks if slice a is a sublist of slice b
func isSublist(a, b []int) bool {
	// An empty list is a sublist of any list
	if len(a) == 0 {
		return true
	}
	// If a is longer than b, it can't be a sublist
	if len(b) < len(a) {
		return false
	}
	
	// Check for each possible starting position in b
	for i := 0; i <= len(b)-len(a); i++ {
		match := true
		for j := 0; j < len(a); j++ {
			if b[i+j] != a[j] {
				match = false
				break
			}
		}
		if match {
			return true
		}
	}
	return false
}
