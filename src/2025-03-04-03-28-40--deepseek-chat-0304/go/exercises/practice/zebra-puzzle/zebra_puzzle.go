package zebra

type Solution struct {
	DrinksWater string
	OwnsZebra   string
}

type House struct {
	Nationality string
	Color       string
	Pet         string
	Drink       string
	Cigarette   string
}

func SolvePuzzle() Solution {
	// Define all possible values
	nationalities := []string{"Englishman", "Spaniard", "Ukrainian", "Norwegian", "Japanese"}
	colors := []string{"Red", "Green", "Ivory", "Yellow", "Blue"}
	pets := []string{"Dog", "Snails", "Fox", "Horse", "Zebra"}
	drinks := []string{"Coffee", "Tea", "Milk", "Orange juice", "Water"}
	cigarettes := []string{"Old Gold", "Kools", "Chesterfields", "Lucky Strike", "Parliaments"}

	// Try all permutations until we find a valid solution
	for _, houses := range permuteHouses(nationalities, colors, pets, drinks, cigarettes) {
		if isValidSolution(houses) {
			// Find who drinks water and owns zebra
			var waterDrinker, zebraOwner string
			for _, h := range houses {
				if h.Drink == "Water" {
					waterDrinker = h.Nationality
				}
				if h.Pet == "Zebra" {
					zebraOwner = h.Nationality
				}
			}
			return Solution{DrinksWater: waterDrinker, OwnsZebra: zebraOwner}
		}
	}
	
	return Solution{}
}

func permuteHouses(nationalities, colors, pets, drinks, cigarettes []string) [][]House {
	var result [][]House
	// Generate all possible combinations
	for _, n := range permutations(nationalities) {
		for _, c := range permutations(colors) {
			for _, p := range permutations(pets) {
				for _, d := range permutations(drinks) {
					for _, cig := range permutations(cigarettes) {
						var houses []House
						for i := 0; i < 5; i++ {
							houses = append(houses, House{
								Nationality: n[i],
								Color:       c[i],
								Pet:         p[i],
								Drink:       d[i],
								Cigarette:   cig[i],
							})
						}
						result = append(result, houses)
					}
				}
			}
		}
	}
	return result
}

func permutations(arr []string) [][]string {
	var result [][]string
	permute(arr, 0, &result)
	return result
}

func permute(arr []string, start int, result *[][]string) {
	if start == len(arr)-1 {
		tmp := make([]string, len(arr))
		copy(tmp, arr)
		*result = append(*result, tmp)
		return
	}
	for i := start; i < len(arr); i++ {
		arr[start], arr[i] = arr[i], arr[start]
		permute(arr, start+1, result)
		arr[start], arr[i] = arr[i], arr[start]
	}
}

func isValidSolution(houses []House) bool {
	// Create maps for quick lookup
	nationalityMap := make(map[string]int)
	colorMap := make(map[string]int)
	petMap := make(map[string]int)
	drinkMap := make(map[string]int)
	cigaretteMap := make(map[string]int)
	
	for i, h := range houses {
		nationalityMap[h.Nationality] = i
		colorMap[h.Color] = i
		petMap[h.Pet] = i
		drinkMap[h.Drink] = i
		cigaretteMap[h.Cigarette] = i
	}
	
	// Check all constraints
	// 2. The Englishman lives in the red house.
	if nationalityMap["Englishman"] != colorMap["Red"] {
		return false
	}
	// 3. The Spaniard owns the dog.
	if nationalityMap["Spaniard"] != petMap["Dog"] {
		return false
	}
	// 4. Coffee is drunk in the green house.
	if drinkMap["Coffee"] != colorMap["Green"] {
		return false
	}
	// 5. The Ukrainian drinks tea.
	if nationalityMap["Ukrainian"] != drinkMap["Tea"] {
		return false
	}
	// 6. The green house is immediately to the right of the ivory house.
	if colorMap["Green"] != colorMap["Ivory"]+1 {
		return false
	}
	// 7. The Old Gold smoker owns snails.
	if cigaretteMap["Old Gold"] != petMap["Snails"] {
		return false
	}
	// 8. Kools are smoked in the yellow house.
	if cigaretteMap["Kools"] != colorMap["Yellow"] {
		return false
	}
	// 9. Milk is drunk in the middle house.
	if drinkMap["Milk"] != 2 {
		return false
	}
	// 10. The Norwegian lives in the first house.
	if nationalityMap["Norwegian"] != 0 {
		return false
	}
	// 11. The man who smokes Chesterfields lives in the house next to the man with the fox.
	if abs(cigaretteMap["Chesterfields"] - petMap["Fox"]) != 1 {
		return false
	}
	// 12. Kools are smoked in the house next to the house where the horse is kept.
	if abs(cigaretteMap["Kools"] - petMap["Horse"]) != 1 {
		return false
	}
	// 13. The Lucky Strike smoker drinks orange juice.
	if cigaretteMap["Lucky Strike"] != drinkMap["Orange juice"] {
		return false
	}
	// 14. The Japanese smokes Parliaments.
	if nationalityMap["Japanese"] != cigaretteMap["Parliaments"] {
		return false
	}
	// 15. The Norwegian lives next to the blue house.
	if abs(nationalityMap["Norwegian"] - colorMap["Blue"]) != 1 {
		return false
	}
	
	return true
}

func abs(x int) int {
	if x < 0 {
		return -x
	}
	return x
}
