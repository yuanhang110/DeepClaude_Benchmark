package dndcharacter

import (
	"math"
	"math/rand"
	"time"
)

func init() {
	rand.Seed(time.Now().UnixNano())
}

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
	sum := 0
	min := 6
	for i := 0; i < 4; i++ {
		roll := rand.Intn(6) + 1
		sum += roll
		if roll < min {
			min = roll
		}
	}
	return sum - min
}

// GenerateCharacter creates a new Character with random scores for abilities
func GenerateCharacter() Character {
	c := Character{}
	c.Strength = Ability()
	c.Dexterity = Ability()
	c.Constitution = Ability()
	c.Intelligence = Ability()
	c.Wisdom = Ability()
	c.Charisma = Ability()
	c.Hitpoints = 10 + Modifier(c.Constitution)
	return c
}
