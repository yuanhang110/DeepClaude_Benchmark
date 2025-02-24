package sublist

// Relation type is defined in relations.go file.

func Sublist(l1, l2 []int) Relation {
	if len(l1) == len(l2) {
		if isSublist(l1, l2) {
			return RelationEqual
		}
		return RelationUnequal
	}

	if isSublist(l1, l2) {
		return RelationSublist
	}

	if isSublist(l2, l1) {
		return RelationSuperlist
	}

	return RelationUnequal
}

func isSublist(a, b []int) bool {
	if len(a) == 0 {
		return true
	}
	if len(b) < len(a) {
		return false
	}
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
