package bowling

import "errors"

type Game struct {
	rolls []int
}

func NewGame() *Game {
	return &Game{
		rolls: make([]int, 0, 21), // Max 21 rolls in a game
	}
}

func (g *Game) Roll(pins int) error {
	if pins < 0 || pins > 10 {
		return ErrInvalidPins
	}
	
	// Check for invalid roll in frame (except 10th)
	if len(g.rolls)%2 == 1 && len(g.rolls) < 18 {
		if g.rolls[len(g.rolls)-1] + pins > 10 {
			return ErrInvalidPins
		}
	}
	
	g.rolls = append(g.rolls, pins)
	return nil
}

func (g *Game) Score() (int, error) {
	if len(g.rolls) < 12 || len(g.rolls) > 21 {
		return 0, ErrIncompleteGame
	}
	
	score := 0
	rollIndex := 0
	
	for frame := 0; frame < 10; frame++ {
		if isStrike(g.rolls, rollIndex) {
			score += 10 + strikeBonus(g.rolls, rollIndex)
			rollIndex++
		} else if isSpare(g.rolls, rollIndex) {
			score += 10 + spareBonus(g.rolls, rollIndex)
			rollIndex += 2
		} else {
			score += sumOfBallsInFrame(g.rolls, rollIndex)
			rollIndex += 2
		}
	}
	
	return score, nil
}

// Helper functions
func isStrike(rolls []int, rollIndex int) bool {
	return rolls[rollIndex] == 10
}

func isSpare(rolls []int, rollIndex int) bool {
	return rolls[rollIndex]+rolls[rollIndex+1] == 10
}

func strikeBonus(rolls []int, rollIndex int) int {
	return rolls[rollIndex+1] + rolls[rollIndex+2]
}

func spareBonus(rolls []int, rollIndex int) int {
	return rolls[rollIndex+2]
}

func sumOfBallsInFrame(rolls []int, rollIndex int) int {
	return rolls[rollIndex] + rolls[rollIndex+1]
}

// Errors
var (
	ErrInvalidPins    = errors.New("invalid number of pins")
	ErrIncompleteGame = errors.New("game is not complete")
)
