package zebra

type Solution struct {
	DrinksWater string
	OwnsZebra   string
}

func SolvePuzzle() Solution {
	colorPerms := [][5]string{
		{"red", "blue", "ivory", "green", "yellow"},
		{"yellow", "blue", "ivory", "green", "red"},
		{"red", "blue", "yellow", "ivory", "green"},
		{"yellow", "blue", "red", "ivory", "green"},
	}

	nationalityPerms := generateNationalityPermutations()
	drinkPerms := generateDrinkPermutations()
	smokePerms := permutations([]string{"Old Gold", "Kools", "Chesterfields", "Lucky Strike", "Parliaments"})
	petPerms := permutations([]string{"dog", "snails", "fox", "horse", "zebra"})

	for _, colors := range colorPerms {
		for _, nationals := range nationalityPerms {
			engIdx := indexOf("Englishman", nationals[:])
			if engIdx == -1 || colors[engIdx] != "red" {
				continue
			}

			ukrIdx := indexOf("Ukrainian", nationals[:])
			if ukrIdx == -1 {
				continue
			}

			for _, drinks := range drinkPerms {
				if drinks[indexOf("green", colors[:])] != "coffee" {
					continue
				}
				if drinks[ukrIdx] != "tea" {
					continue
				}

				for _, smokes := range smokePerms {
					yellowIdx := indexOf("yellow", colors[:])
					if yellowIdx == -1 || smokes[yellowIdx] != "Kools" {
						continue
					}

					japaneseIdx := indexOf("Japanese", nationals[:])
					if japaneseIdx == -1 || smokes[japaneseIdx] != "Parliaments" {
						continue
					}

					validSmoke := true
					for i, s := range smokes {
						if s == "Lucky Strike" && drinks[i] != "orange juice" {
							validSmoke = false
							break
						}
					}
					if !validSmoke {
						continue
					}

					for _, pets := range petPerms {
						spaniardIdx := indexOf("Spaniard", nationals[:])
						if spaniardIdx == -1 || pets[spaniardIdx] != "dog" {
							continue
						}

						validPet := true
						for i, s := range smokes {
							if s == "Old Gold" && pets[i] != "snails" {
								validPet = false
								break
							}
						}
						if !validPet {
							continue
						}

						smokesArr := [5]string{}
						copy(smokesArr[:], smokes)
						petsArr := [5]string{}
						copy(petsArr[:], pets)
						
						if !checkAdjacent(smokesArr, "Chesterfields", petsArr, "fox") {
							continue
						}

						if !checkAdjacent(smokesArr, "Kools", petsArr, "horse") {
							continue
						}

						var waterDrinker, zebraOwner string
						for i, d := range drinks {
							if d == "water" {
								waterDrinker = nationals[i]
							}
						}
						for i, p := range pets {
							if p == "zebra" {
								zebraOwner = nationals[i]
							}
						}

						return Solution{waterDrinker, zebraOwner}
					}
				}
			}
		}
	}
	panic("No solution found")
}

func generateNationalityPermutations() [][5]string {
	base := []string{"Englishman", "Spaniard", "Ukrainian", "Japanese"}
	perms := permutations(base)
	result := make([][5]string, len(perms))
	for i, p := range perms {
		result[i] = [5]string{"Norwegian", p[0], p[1], p[2], p[3]}
	}
	return result
}

func generateDrinkPermutations() [][5]string {
	base := []string{"coffee", "tea", "orange juice", "water"}
	perms := permutations(base)
	result := make([][5]string, len(perms))
	for i, p := range perms {
		result[i] = [5]string{p[0], p[1], "milk", p[2], p[3]}
	}
	return result
}

func permutations(arr []string) [][]string {
	var result [][]string
	generatePerms(arr, 0, &result)
	return result
}

func generatePerms(arr []string, n int, result *[][]string) {
	if n == len(arr) {
		tmp := make([]string, len(arr))
		copy(tmp, arr)
		*result = append(*result, tmp)
		return
	}
	for i := n; i < len(arr); i++ {
		arr[n], arr[i] = arr[i], arr[n]
		generatePerms(arr, n+1, result)
		arr[n], arr[i] = arr[i], arr[n]
	}
}

func indexOf(target string, arr []string) int {
	for i, v := range arr {
		if v == target {
			return i
		}
	}
	return -1
}

func checkAdjacent(smokes [5]string, smokeTarget string, pets [5]string, petTarget string) bool {
	for i := 0; i < 5; i++ {
		if smokes[i] == smokeTarget {
			if (i > 0 && pets[i-1] == petTarget) || (i < 4 && pets[i+1] == petTarget) {
				return true
			}
		}
	}
	return false
}
