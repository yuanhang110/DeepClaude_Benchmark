package bowling

import "errors"

type Game struct {
    rolls []int
}

func NewGame() *Game {
	return &Game{
		rolls: []int{},
	}
}

func (g *Game) Roll(pins int) error {
	if pins < 0 || pins > 10 {
		return errors.New("invalid number of pins")
	}

	if g.isGameOver() {
		return errors.New("game is already over")
	}

	currentFrame, rollsInFrame10 := g.computeFrameState()

	if currentFrame < 10 {
		i := 0
		for f := 1; f < currentFrame; f++ {
			if g.rolls[i] == 10 {
				i++
			} else {
				i += 2
			}
		}
		if i < len(g.rolls) {
			sum := g.rolls[i] + pins
			if sum > 10 {
				return errors.New("invalid roll: sum of frame exceeds 10")
			}
		}
	} else {
		if rollsInFrame10 == 1 {
			first := g.rolls[len(g.rolls)-1]
			if first != 10 {
				sum := first + pins
				if sum > 10 {
					return errors.New("invalid roll: sum of frame exceeds 10")
				}
			}
		} else if rollsInFrame10 == 2 {
			first := g.rolls[len(g.rolls)-2]
			second := g.rolls[len(g.rolls)-1]
			if first != 10 && (first + second) != 10 {
				return errors.New("invalid roll: no fill balls allowed")
			}
			// Check if the sum of fill balls exceeds 10
			if second + pins > 10 {
				return errors.New("invalid roll: sum of fill balls exceeds 10")
			}
		}
	}

	g.rolls = append(g.rolls, pins)
	return nil
}

func (g *Game) Score() (int, error) {
	if !g.isGameOver() {
		return 0, errors.New("game is not over")
	}

	total := 0
	i := 0
	for frame := 0; frame < 10; frame++ {
		if i >= len(g.rolls) {
			break
		}

		if g.rolls[i] == 10 {
			// Strike
			total += 10
			if i+1 < len(g.rolls) {
				total += g.rolls[i+1]
			}
			if i+2 < len(g.rolls) {
				total += g.rolls[i+2]
			}
			i++
		} else {
			// Open or spare
			first := g.rolls[i]
			var second int
			if i+1 < len(g.rolls) {
				second = g.rolls[i+1]
			}
			total += first + second

			if first+second == 10 {
				// Spare
				if i+2 < len(g.rolls) {
					total += g.rolls[i+2]
				}
			}
			i += 2
		}
	}
	return total, nil
}

func (g *Game) computeFrameState() (currentFrame, rollsInFrame10 int) {
	i := 0
	currentFrame = 1
	for currentFrame < 10 && i < len(g.rolls) {
		if g.rolls[i] == 10 {
			currentFrame++
			i++
		} else {
			if i+1 < len(g.rolls) {
				currentFrame++
				i += 2
			} else {
				break
			}
		}
	}
	rollsInFrame10 = len(g.rolls) - i
	return
}

func (g *Game) isGameOver() bool {
	currentFrame, rollsInFrame10 := g.computeFrameState()
	if currentFrame < 10 {
		return false
	}

	if rollsInFrame10 < 2 {
		return false
	}

	first := g.rolls[len(g.rolls)-rollsInFrame10]
	if first == 10 {
		// Strike in 10th frame
		return rollsInFrame10 >= 3
	}

	sum := first + g.rolls[len(g.rolls)-rollsInFrame10+1]
	if sum == 10 {
		// Spare in 10th frame
		return rollsInFrame10 >= 3
	}

	return rollsInFrame10 >= 2
}
