package bowling

import (
	"errors"
	"fmt"
)

type Game struct {
	rolls []int
}

func NewGame() *Game {
	return &Game{}
}

func (g *Game) Roll(pins int) error {
	if pins < 0 || pins > 10 {
		return fmt.Errorf("invalid pins: %d", pins)
	}

	currentFrame, position, err := g.currentFrameAndPosition()
	if err != nil {
		return err
	}

	if currentFrame >= 10 {
		return errors.New("game already completed")
	}

	if currentFrame < 9 {
		if position == 0 {
			// First roll in frame
			if pins == 10 {
				// Strike - advance frame
				g.rolls = append(g.rolls, pins)
				return nil
			}
			// Open frame
			g.rolls = append(g.rolls, pins)
			return nil
		}

		// Second roll in frame
		first := g.rolls[len(g.rolls)-1]
		if first+pins > 10 {
			return fmt.Errorf("invalid roll sum: %d + %d", first, pins)
		}
		g.rolls = append(g.rolls, pins)
		return nil
	}

	// 10th frame rules
	if len(g.rolls) >= 21 {
		return errors.New("too many rolls in 10th frame")
	}

	// Special 10th frame validation
	rollsInTenth := len(g.rolls) - g.rollsInFrames(9)
	switch rollsInTenth {
	case 0:
		// First roll
		g.rolls = append(g.rolls, pins)
		return nil
	case 1:
		// Second roll - check if first was strike or sum < 10
		first := g.rolls[g.rollsInFrames(9)]
		if first == 10 || first+pins <= 10 {
			g.rolls = append(g.rolls, pins)
			return nil
		}
		return fmt.Errorf("invalid 10th frame roll: %d after %d", pins, first)
	case 2:
		// Third roll - only allowed if there was a strike or spare
		first := g.rolls[g.rollsInFrames(9)]
		second := g.rolls[g.rollsInFrames(9)+1]
		if first == 10 || (first+second == 10) {
			g.rolls = append(g.rolls, pins)
			return nil
		}
		return errors.New("third roll not allowed in 10th frame")
	}

	return errors.New("too many rolls in 10th frame")
}

func (g *Game) currentFrameAndPosition() (int, int, error) {
	frame := 0
	roll := 0

	for frame < 9 && roll < len(g.rolls) {
		if g.rolls[roll] == 10 {
			// Strike moves to next frame
			frame++
			roll++
		} else {
			if roll+1 >= len(g.rolls) {
				// Incomplete frame, return current position
				return frame, roll % 2, nil
			}

			if sum := g.rolls[roll] + g.rolls[roll+1]; sum > 10 {
				return 0, 0, fmt.Errorf("invalid frame %d sum: %d", frame, sum)
			}

			frame++
			roll += 2
		}
	}

	// Handle 10th frame
	if frame >= 9 {
		tenthFrameRolls := len(g.rolls) - roll
		if tenthFrameRolls > 3 {
			return 0, 0, errors.New("invalid 10th frame rolls")
		}
		return 9, tenthFrameRolls, nil
	}

	return frame, 0, nil
}

func (g *Game) rollsInFrames(n int) int {
	count := 0
	frame := 0
	roll := 0

	for roll < len(g.rolls) && frame <= n {
		if g.rolls[roll] == 10 {
			// Strike
			count++
			frame++
			roll++
		} else {
			if roll+1 >= len(g.rolls) {
				break
			}
			count += 2
			frame++
			roll += 2
		}
	}
	return count
}

func (g *Game) Score() (int, error) {
	if !g.isComplete() {
		return 0, errors.New("game not complete")
	}

	score := 0
	roll := 0

	for frame := 0; frame < 10; frame++ {
		if roll >= len(g.rolls) {
			return 0, fmt.Errorf("incomplete game at frame %d", frame)
		}

		// Strike
		if g.rolls[roll] == 10 {
			score += 10
			// Add next two rolls if available
			if roll+1 < len(g.rolls) {
				score += g.rolls[roll+1]
			}
			if roll+2 < len(g.rolls) {
				score += g.rolls[roll+2]
			}
			roll++
			continue
		}

		// Spare or open frame
		if roll+1 >= len(g.rolls) {
			return 0, fmt.Errorf("incomplete frame %d", frame)
		}
		frameSum := g.rolls[roll] + g.rolls[roll+1]
		score += frameSum

		// Spare bonus
		if frameSum == 10 && frame < 9 {
			if roll+2 < len(g.rolls) {
				score += g.rolls[roll+2]
			}
		}

		roll += 2
	}

	return score, nil
}

func (g *Game) isComplete() bool {
	frame, _, err := g.currentFrameAndPosition()
	if err != nil || frame < 10 {
		return false
	}

	// Check 10th frame completion
	tenthStart := g.rollsInFrames(9)
	tenthRolls := len(g.rolls) - tenthStart

	if tenthRolls < 2 {
		return false
	}

	first := g.rolls[tenthStart]
	second := g.rolls[tenthStart+1]

	if tenthRolls == 2 {
		// Complete if not strike or spare
		return first+second < 10
	}

	// Third roll exists, must be valid
	return tenthRolls == 3 && (first == 10 || first+second == 10)
}
