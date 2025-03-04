package robot

// See defs.go for other definitions

// Step 1
// Define N, E, S, W here.
type Dir int

const (
	N Dir = iota
	E
	S
	W
)

func Right() {
	panic("Please implement the Right function")
}

func Left() {
	panic("Please implement the Left function")
}

func Advance() {
	panic("Please implement the Advance function")
}

func (d Dir) String() string {
	switch d {
	case N:
		return "north"
	case E:
		return "east"
	case S:
		return "south"
	case W:
		return "west"
	}
	return ""
}

// Step 2
// Define Action type here.
type Action int

const (
	ActionRight Action = iota
	ActionLeft
	ActionAdvance
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
	Name    string
	Command Command
}

func StartRobot3(name, script string, action chan Action3, log chan string) {
	panic("Please implement the StartRobot3 function")
}

func Room3(extent Rect, robots []Step3Robot, action chan Action3, rep chan []Step3Robot, log chan string) {
	panic("Please implement the Room3 function")
}
