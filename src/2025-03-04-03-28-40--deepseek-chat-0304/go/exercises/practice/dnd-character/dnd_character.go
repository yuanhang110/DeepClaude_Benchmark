package dndcharacter

import (
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
	return int(float64(score - 10) / 2.0)
}

// rollDice simulates rolling a 6-sided die
func rollDice() int {
	return rand.Intn(6) + 1
}

// Ability uses randomness to generate the score for an ability
func Ability() int {
	// Roll 4 dice
	rolls := []int{rollDice(), rollDice(), rollDice(), rollDice()}
	// Sort to get highest 3
	sort.Ints(rolls)
	// Sum highest 3
	return rolls[1] + rolls[2] + rolls[3]
}

// GenerateCharacter creates a new Character with random scores for abilities
func GenerateCharacter() Character {
	rand.Seed(time.Now().UnixNano())
	char := Character{
		Strength:     Ability(),
		Dexterity:    Ability(),
		Constitution: Ability(),
		Intelligence: Ability(),
		Wisdom:       Ability(),
		Charisma:     Ability(),
	}
	char.Hitpoints = 10 + Modifier(char.Constitution)
	return char
}
