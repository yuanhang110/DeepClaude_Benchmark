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
	return (score - 10) / 2
}

// Ability uses randomness to generate the score for an ability
func Ability() int {
	rolls := make([]int, 4)
	for i := 0; i < 4; i++ {
		rolls[i] = rand.Intn(6) + 1
	}
	sort.Ints(rolls)
	return rolls[1] + rolls[2] + rolls[3]
}

// GenerateCharacter creates a new Character with random scores for abilities
func GenerateCharacter() Character {
	rand.Seed(time.Now().UnixNano())
	c := Character{
		Strength:     Ability(),
		Dexterity:    Ability(),
		Constitution: Ability(),
		Intelligence: Ability(),
		Wisdom:       Ability(),
		Charisma:     Ability(),
	}
	c.Hitpoints = 10 + Modifier(c.Constitution)
	return c
}
