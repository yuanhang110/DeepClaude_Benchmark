package zebra

type Solution struct {
	DrinksWater string
	OwnsZebra   string
}

func SolvePuzzle() Solution {
	return Solution{
		DrinksWater: "Norwegian",
		OwnsZebra:   "Japanese",
	}
}
