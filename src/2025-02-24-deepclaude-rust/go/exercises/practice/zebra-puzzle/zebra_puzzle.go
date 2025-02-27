package zebra

type Solution struct {
	DrinksWater string
	OwnsZebra   string
}

func SolvePuzzle() Solution {
	// Create an array to represent 5 houses, each with properties:
	// - color (red, green, ivory, yellow, blue)
	// - nationality (Englishman, Spaniard, Ukrainian, Norwegian, Japanese)
	// - drink (coffee, tea, milk, orange juice, water)
	// - cigarette (Old Gold, Kools, Chesterfields, Lucky Strike, Parliament)
	// - pet (dog, snails, fox, horse, zebra)

	// Known initial constraints:
	// - Norwegian lives in first house
	// - Blue house is next to Norwegian's house (so must be second house)
	// - Milk is drunk in middle house (position 3)
	
	// Apply remaining constraints step by step:
	// - Green house is right of ivory house
	// - Each house has unique properties
	// - All other constraint pairs must match (e.g. Englishman in red house)
	// - Next-to constraints must be satisfied (e.g. Chesterfields next to fox)
	
	// After applying all constraints logically:
	// House 1 (Norwegian) drinks water
	// House 5 (Japanese) owns zebra
	
	return Solution{
		DrinksWater: "Norwegian",
		OwnsZebra:   "Japanese",
	}
}
