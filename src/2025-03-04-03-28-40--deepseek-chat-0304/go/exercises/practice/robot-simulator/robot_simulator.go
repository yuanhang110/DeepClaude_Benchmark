package robot

// See defs.go for other definitions

// Step 1
// Define N, E, S, W here.
const (
	N Dir = iota
	E
	S
	W
)

var currentDir Dir = N
var pos Pos = Pos{0, 0}

func Right() {
	currentDir = (currentDir + 1) % 4
}

func Left() {
	currentDir = (currentDir + 3) % 4
}

func Advance() {
	switch currentDir {
	case N:
		pos.Northing++
	case E:
		pos.Easting++
	case S:
		pos.Northing--
	case W:
		pos.Easting--
	}
}

func (d Dir) String() string {
	return [...]string{"N", "E", "S", "W"}[d]
}

// Step 2
// Define Action type here.
type Action byte

const (
	ActionRight Action = 'R'
	ActionLeft  Action = 'L'
	ActionAdvance Action = 'A'
)

func StartRobot(command chan Command, action chan Action) {
	panic("Please implement the StartRobot function")
}

func Room(extent Rect, robot Step2Robot, action chan Action, report chan Step2Robot) {
	panic("Please implement the Room function")
}

// Step 3
// Define Action3 type here.
type Action3 struct {
	Name   string
	Action Action
}

func StartRobot3(name, script string, action chan Action3, log chan string) {
	panic("Please implement the StartRobot3 function")
}

func Room3(extent Rect, robots []Step3Robot, action chan Action3, rep chan []Step3Robot, log chan string) {
	panic("Please implement the Room3 function")
}
