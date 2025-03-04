package sublist

// Relation type is defined in relations.go file.

func isSublist(small, large []int) bool {
	if len(small) == 0 {
		return true
	}
	
	for i := 0; i <= len(large)-len(small); i++ {
		match := true
		for j := 0; j < len(small); j++ {
			if large[i+j] != small[j] {
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

func Sublist(l1, l2 []int) Relation {
	if len(l1) == len(l2) {
		// Check if equal
		for i := 0; i < len(l1); i++ {
			if l1[i] != l2[i] {
				return RelationUnequal
			}
		}
		return RelationEqual
	} else if len(l1) < len(l2) {
		if isSublist(l1, l2) {
			return RelationSublist
		}
	} else {
		if isSublist(l2, l1) {
			return RelationSuperlist
		}
	}
	return RelationUnequal
}
