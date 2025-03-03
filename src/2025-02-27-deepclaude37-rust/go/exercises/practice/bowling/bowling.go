package bowling

import "errors"

type Game struct {
	rolls        []int
	frame        int
	inFrameRolls int
}

func NewGame() *Game {
	return &Game{
		rolls:        make([]int, 0),
		frame:        0,
		inFrameRolls: 0,
	}
}

func (g *Game) Roll(pins int) error {
	if g.frame >= 10 {
		return errors.New("game is over")
	}
	if pins < 0 || pins > 10 {
		return errors.New("invalid number of pins")
	}

	if g.frame < 9 {
		// Frames 1-9
		if g.inFrameRolls == 0 {
			// First roll in frame
			if pins == 10 {
				// Strike - frame is done
				g.rolls = append(g.rolls, pins)
				g.frame++
				g.inFrameRolls = 0
			} else {
				g.rolls = append(g.rolls, pins)
				g.inFrameRolls = 1
			}
		} else {
			// Second roll in frame
			if g.rolls[len(g.rolls)-1]+pins > 10 {
				return errors.New("invalid roll, sum exceeds 10")
			}
			g.rolls = append(g.rolls, pins)
			g.frame++
			g.inFrameRolls = 0
		}
	} else {
		// 10th frame
		switch g.inFrameRolls {
		case 0:
			// First roll in 10th frame
			g.rolls = append(g.rolls, pins)
			g.inFrameRolls = 1
		case 1:
			// Second roll in 10th frame
			first := g.rolls[len(g.rolls)-1]
			if first == 10 {
				// Strike in first roll, any second roll is valid
				g.rolls = append(g.rolls, pins)
				g.inFrameRolls = 2
			} else {
				// Not a strike in first roll
				if first+pins > 10 {
					return errors.New("invalid roll, sum exceeds 10")
				}
				g.rolls = append(g.rolls, pins)
				if first+pins == 10 {
					// Spare, allow third roll
					g.inFrameRolls = 2
				} else {
					// Open frame, game is done
					g.frame++
					g.inFrameRolls = 0
				}
			}
		case 2:
			// Third roll in 10th frame
			// Check if third roll is allowed (first was strike or spare)
			first := g.rolls[len(g.rolls)-2]
			second := g.rolls[len(g.rolls)-1]
			if first != 10 && first+second != 10 {
				return errors.New("third roll not allowed")
			}
			
			// Additional validation for the third roll
			// If first was a strike and second wasn't, then second+third can't exceed 10
			if first == 10 && second != 10 && second+pins > 10 {
				return errors.New("invalid number of pins")
			}
			
			g.rolls = append(g.rolls, pins)
			g.frame++
			g.inFrameRolls = 0
		}
	}

	return nil
}

func (g *Game) Score() (int, error) {
	if g.frame < 10 {
		return 0, errors.New("game not over")
	}

	total := 0
	pos := 0

	for frame := 0; frame < 10; frame++ {
		if pos >= len(g.rolls) {
			break
		}

		if frame == 9 {
			// 10th frame: sum all remaining rolls
			sum := 0
			for i := pos; i < len(g.rolls); i++ {
				sum += g.rolls[i]
			}
			total += sum
			break
		}

		if g.rolls[pos] == 10 {
			// Strike
			total += 10
			// Add next two rolls as bonus
			if pos+1 < len(g.rolls) {
				total += g.rolls[pos+1]
			}
			if pos+2 < len(g.rolls) {
				total += g.rolls[pos+2]
			}
			pos++
		} else if pos+1 < len(g.rolls) && g.rolls[pos]+g.rolls[pos+1] == 10 {
			// Spare
			total += 10
			// Add next roll as bonus
			if pos+2 < len(g.rolls) {
				total += g.rolls[pos+2]
			}
			pos += 2
		} else {
			// Open frame
			total += g.rolls[pos]
			if pos+1 < len(g.rolls) {
				total += g.rolls[pos+1]
			}
			pos += 2
		}
	}

	return total, nil
}
