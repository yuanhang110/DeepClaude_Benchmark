package bowling

type Game struct {
	rolls []int
}

func NewGame() *Game {
	return &Game{rolls: []int{}}
}

func (g *Game) Roll(pins int) error {
	panic("Please implement the Roll function")
}

func (g *Game) Score() (int, error) {
	panic("Please implement the Score function")
}
