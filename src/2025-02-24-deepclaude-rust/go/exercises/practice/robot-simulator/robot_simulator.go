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

var (
	curDir Dir = N
	posX   int
	posY   int
)

func Right() {
	curDir = (curDir + 1) % 4
}

func Left() {
	curDir = (curDir - 1 + 4) % 4
}

func Advance() {
	switch curDir {
	case N:
		posY++
	case E:
		posX++
	case S:
		posY--
	case W:
		posX--
	}
}

func (d Dir) String() string {
	switch d {
	case N:
		return "N"
	case E:
		return "E"
	case S:
		return "S"
	case W:
		return "W"
	default:
		return "unknown direction"
	}
}

// Step 2
type Action struct {
	cmd  Command
	pos  Step2Robot
}

func StartRobot(command chan Command, action chan Action) {
	panic("Please implement the StartRobot function")
}

func Room(extent Rect, robot Step2Robot, action chan Action, report chan Step2Robot) {
	panic("Please implement the Room function")
}

// Step 3
type Action3 struct {
	name    string
	cmd     Command
	pos     Step3Robot
}

func StartRobot3(name, script string, action chan Action3, log chan string) {
	panic("Please implement the StartRobot3 function")
}

func Room3(extent Rect, robots []Step3Robot, action chan Action3, rep chan []Step3Robot, log chan string) {
	panic("Please implement the Room3 function")
}
