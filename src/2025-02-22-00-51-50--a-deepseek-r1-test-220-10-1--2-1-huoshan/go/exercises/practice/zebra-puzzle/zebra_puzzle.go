package zebra


type Solution struct {
	DrinksWater string
	OwnsZebra   string
}

type House struct {
	color      string
	national   string
	pet        string
	drink      string
	cigarette  string
}

var (
	nationals = []string{"Englishman", "Spaniard", "Ukrainian", "Norwegian", "Japanese"}
	colors    = []string{"red", "green", "ivory", "yellow", "blue"}
	pets      = []string{"dog", "snails", "fox", "horse", "zebra"}
	drinks    = []string{"coffee", "tea", "milk", "orange juice", "water"}
	cigarettes = []string{"Old Gold", "Kools", "Chesterfields", "Lucky Strike", "Parliaments"}
)

func contains(s []string, str string) bool {
	for _, v := range s {
		if v == str {
			return true
		}
	}
	return false
}

func allUnique(items []string) bool {
	seen := make(map[string]bool)
	for _, item := range items {
		if seen[item] {
			return false
		}
		seen[item] = true
	}
	return true
}

func validateConstraints(houses []House) bool {
	var positions struct {
		nat, col, pet, drink, cig []string
	}
	
	for _, h := range houses {
		positions.nat = append(positions.nat, h.national)
		positions.col = append(positions.col, h.color)
		positions.pet = append(positions.pet, h.pet)
		positions.drink = append(positions.drink, h.drink)
		positions.cig = append(positions.cig, h.cigarette)
	}
	
	if !allUnique(positions.nat) || !allUnique(positions.col) ||
		!allUnique(positions.pet) || !allUnique(positions.drink) || !allUnique(positions.cig) {
		return false
	}

	for i, h := range houses {
		// Known constraints
		if h.national == "Englishman" && h.color != "red" {
			return false
		}
		if h.national == "Spaniard" && h.pet != "dog" {
			return false
		}
		if h.drink == "coffee" && h.color != "green" {
			return false
		}
		if h.national == "Ukrainian" && h.drink != "tea" {
			return false
		}
		if h.cigarette == "Old Gold" && h.pet != "snails" {
			return false
		}
		if h.cigarette == "Kools" && h.color != "yellow" {
			return false
		}
		if h.cigarette == "Lucky Strike" && h.drink != "orange juice" {
			return false
		}
		if h.national == "Japanese" && h.cigarette != "Parliaments" {
			return false
		}
		if i == 2 && h.drink != "milk" {
			return false
		}
		if i == 0 && h.national != "Norwegian" {
			return false
		}
	}

	// Cross-house constraints
	greenIdx := -1
	ivoryIdx := -1
	for i, h := range houses {
		if h.color == "green" {
			greenIdx = i
		}
		if h.color == "ivory" {
			ivoryIdx = i
		}
	}
	if greenIdx != ivoryIdx+1 {
		return false
	}

	// Norwegian next to blue
	if houses[0].color == "blue" || (houses[1].color != "blue" && len(houses) > 1) {
		return false
	}

	// Chesterfields next to fox
	chesterfieldIdx := -1
	foxIdx := -1
	for i, h := range houses {
		if h.cigarette == "Chesterfields" {
			chesterfieldIdx = i
		}
		if h.pet == "fox" {
			foxIdx = i
		}
	}
	if abs(chesterfieldIdx-foxIdx) != 1 {
		return false
	}

	// Kools next to horse
	koolsIdx := -1
	horseIdx := -1
	for i, h := range houses {
		if h.cigarette == "Kools" {
			koolsIdx = i
		}
		if h.pet == "horse" {
			horseIdx = i
		}
	}
	if abs(koolsIdx-horseIdx) != 1 {
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

func backtrack(houses []House, usedN, usedC, usedP, usedD, usedS map[string]bool) []House {
	if len(houses) == 5 {
		if validateConstraints(houses) {
			return houses
		}
		return nil
	}

	i := len(houses)
	for _, nat := range nationals {
		if i == 0 && nat != "Norwegian" {
			continue
		}
		if usedN[nat] {
			continue
		}
		for _, col := range colors {
			if i == 1 && col != "blue" {
				continue
			}
			if usedC[col] {
				continue
			}
			for _, pet := range pets {
				if usedP[pet] {
					continue
				}
				for _, drink := range drinks {
					if i == 2 && drink != "milk" {
						continue
					}
					if usedD[drink] {
						continue
					}
					for _, cig := range cigarettes {
						if usedS[cig] {
							continue
						}

						newHouse := House{
							national:  nat,
							color:    col,
							pet:      pet,
							drink:    drink,
							cigarette: cig,
						}

						newUsedN := cloneAndAdd(usedN, nat)
						newUsedC := cloneAndAdd(usedC, col)
						newUsedP := cloneAndAdd(usedP, pet)
						newUsedD := cloneAndAdd(usedD, drink)
						newUsedS := cloneAndAdd(usedS, cig)

						result := backtrack(append(houses, newHouse), newUsedN, newUsedC, newUsedP, newUsedD, newUsedS)
						if result != nil {
							return result
						}
					}
				}
			}
		}
	}
	return nil
}

func cloneAndAdd(m map[string]bool, key string) map[string]bool {
	newMap := make(map[string]bool)
	for k, v := range m {
		newMap[k] = v
	}
	newMap[key] = true
	return newMap
}

func SolvePuzzle() Solution {
	houses := backtrack(nil, make(map[string]bool), make(map[string]bool), 
		make(map[string]bool), make(map[string]bool), make(map[string]bool))
		
	var waterDrinker, zebraOwner string
	for _, h := range houses {
		if h.drink == "water" {
			waterDrinker = h.national
		}
		if h.pet == "zebra" {
			zebraOwner = h.national
		}
	}
	
	return Solution{
		DrinksWater: waterDrinker,
		OwnsZebra:   zebraOwner,
	}
}
