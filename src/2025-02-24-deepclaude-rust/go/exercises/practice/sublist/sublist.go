package sublist

// Relation type is defined in relations.go file.

func Sublist(l1, l2 []int) Relation {
	if equal(l1, l2) {
		return RelationEqual
	}
	if len(l1) == 0 {
		return RelationSublist
	}
	if len(l2) == 0 {
		return RelationSuperlist
	}
	if isSublist(l1, l2) {
		return RelationSublist
	}
	if isSublist(l2, l1) {
		return RelationSuperlist
	}
	return RelationUnequal
}

func equal(a, b []int) bool {
	if len(a) != len(b) {
		return false
	}
	for i, v := range a {
		if v != b[i] {
			return false
		}
	}
	return true
}

func isSublist(a, b []int) bool {
	if len(a) > len(b) {
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
