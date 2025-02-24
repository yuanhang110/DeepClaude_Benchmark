package dndcharacter

import (
	"math"
	"math/rand"
	"sort"
	"time"
)

type Character struct {
	Strength     int
	Dexterity    int
	Constitution int
	Intelligence int
	Wisdom       int
	Charisma     int
	Hitpoints    int
}

// Modifier calculates the ability modifier for a given ability score
func Modifier(score int) int {
	return int(math.Floor(float64(score - 10) / 2))
}

// Ability uses randomness to generate the score for an ability
func Ability() int {
	rand.Seed(time.Now().UnixNano())
	dice := make([]int, 4)
	for i := 0; i < 4; i++ {
		dice[i] = rand.Intn(6) + 1 // Generate dice roll between 1-6
	}
	sort.Ints(dice) // Sort ascending
	return dice[1] + dice[2] + dice[3] // Sum highest 3
}

// GenerateCharacter creates a new Character with random scores for abilities
func GenerateCharacter() Character {
	rand.Seed(time.Now().UnixNano())
	con := Ability() // Need to store constitution separately for HP calculation
	return Character{
		Strength:     Ability(),
		Dexterity:    Ability(),
		Constitution: con,
		Intelligence: Ability(),
		Wisdom:       Ability(),
		Charisma:     Ability(),
		Hitpoints:    10 + Modifier(con),
	}
}
