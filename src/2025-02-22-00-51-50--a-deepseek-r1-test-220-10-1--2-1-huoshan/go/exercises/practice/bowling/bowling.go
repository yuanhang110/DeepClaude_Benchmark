package bowling

import "fmt"

type Game struct {
	rolls        []int
	currentFrame int
	isDone       bool
}

func NewGame() *Game {
	return &Game{
		rolls:        make([]int, 0, 21),
		currentFrame: 1,
	}
	panic("Please implement the NewGame function")
}

func (g *Game) Roll(pins int) error {
	if g.isDone {
		return fmt.Errorf("game is already complete")
	}
	if pins < 0 || pins > 10 {
		return fmt.Errorf("invalid number of pins: %d", pins)
	}

	// Validate frame rules
	if g.currentFrame < 10 {
		// Normal frame validation
		if len(g.rolls)%2 == 0 { // First roll in frame
			if pins == 10 { // Strike
				g.rolls = append(g.rolls, pins)
				g.currentFrame++
				return nil
			}
		} else { // Second roll in frame
			if g.rolls[len(g.rolls)-1]+pins > 10 {
				return fmt.Errorf("frame total exceeds 10 pins")
			}
		}
	} else {
		// 10th frame special rules
		numRolls := len(g.rolls) - 20 // Rolls in 10th frame (0, 1, or 2)
		if numRolls == 0 {
			if pins == 10 { // Strike - get two extra rolls
				g.rolls = append(g.rolls, pins)
				return nil
			}
		} else if numRolls == 1 {
			if g.rolls[18] == 10 { // First strike in 10th
				if pins == 10 { // Second strike
					g.rolls = append(g.rolls, pins)
					return nil
				}
			} else { // Spare in 10th
				if pins > 10 {
					return fmt.Errorf("invalid number of pins: %d", pins)
				}
				g.rolls = append(g.rolls, pins)
				g.isDone = true
				return nil
			}
		} else if numRolls == 2 {
			if pins > 10 {
				return fmt.Errorf("invalid number of pins: %d", pins)
			}
			g.rolls = append(g.rolls, pins)
			g.isDone = true
			return nil
		}
	}

	g.rolls = append(g.rolls, pins)
	
	// Advance frame if needed
	if g.currentFrame < 10 && (pins == 10 || len(g.rolls)%2 == 0) {
		g.currentFrame++
	}
	
	// Check if game is complete
	if g.currentFrame == 10 && len(g.rolls) == 20 && (g.rolls[18]+g.rolls[19] < 10) {
		g.isDone = true
	}
	
	return nil
}

func (g *Game) Score() (int, error) {
	if !g.isDone {
		return 0, fmt.Errorf("game is not yet complete")
	}

	total := 0
	rollIndex := 0

	for frame := 1; frame <= 10; frame++ {
		if rollIndex >= len(g.rolls) {
			return 0, fmt.Errorf("invalid game state")
		}

		if frame < 10 {
			if g.rolls[rollIndex] == 10 { // Strike
				total += 10
				// Add next two rolls as bonus
				if rollIndex+1 < len(g.rolls) {
					total += g.rolls[rollIndex+1]
				}
				if rollIndex+2 < len(g.rolls) {
					total += g.rolls[rollIndex+2]
				}
				rollIndex++
			} else if rollIndex+1 < len(g.rolls) && g.rolls[rollIndex]+g.rolls[rollIndex+1] == 10 { // Spare
				total += 10
				if rollIndex+2 < len(g.rolls) {
					total += g.rolls[rollIndex+2]
				}
				rollIndex += 2
			} else { // Open frame
				total += g.rolls[rollIndex]
				if rollIndex+1 < len(g.rolls) {
					total += g.rolls[rollIndex+1]
				}
				rollIndex += 2
			}
		} else {
			// 10th frame - sum all rolls in the frame
			for i := rollIndex; i < len(g.rolls); i++ {
				total += g.rolls[i]
			}
			break
		}
	}

	return total, nil
}
