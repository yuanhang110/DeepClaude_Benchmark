package sublist

// Relation type is defined in relations.go file.

func Sublist(l1, l2 []int) Relation {
	if areEqual(l1, l2) {
		return RelationEqual
	}
	if isSublist(l2, l1) {
		return RelationSublist
	}
	if isSublist(l1, l2) {
		return RelationSuperlist
	}
	return RelationUnequal
}

func areEqual(a, b []int) bool {
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

func isSublist(super, sub []int) bool {
	if len(sub) == 0 {
		return true
	}
	if len(super) < len(sub) {
		return false
	}
	for i := 0; i <= len(super)-len(sub); i++ {
		match := true
		for j := 0; j < len(sub); j++ {
			if super[i+j] != sub[j] {
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
