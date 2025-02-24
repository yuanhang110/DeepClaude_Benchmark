package poker

func compareStrengths(a, b []int) int {
	for i := 0; i < len(a) && i < len(b); i++ {
		if a[i] > b[i] {
			return 1
		} else if a[i] < b[i] {
			return -1
		}
	}
	return 0
}

func handleThreeOfAKind(rankCounts map[int]int) []int {
	var trio, kicker int
	for r, c := range rankCounts {
		if c == 3 {
			trio = r
		} else {
			kicker = r
		}
	}
	return []int{4, trio, kicker}
}

func handleFullHouse(rankCounts map[int]int) []int {
	var trio, pair int
	for r, c := range rankCounts {
		if c == 3 {
			trio = r
		} else {
			pair = r
		}
	}
	return []int{7, trio, pair}
}

func handleOnePair(rankCounts map[int]int) []int {
	var pair, kicker1, kicker2 int
	pairs := 0
	for r, c := range rankCounts {
		if c == 2 {
			pair = r
			pairs++
		} else {
			if kicker1 == 0 {
				kicker1 = r
			} else {
				kicker2 = r
			}
		}
	}
	return []int{2, pair, kicker1, kicker2}
}
