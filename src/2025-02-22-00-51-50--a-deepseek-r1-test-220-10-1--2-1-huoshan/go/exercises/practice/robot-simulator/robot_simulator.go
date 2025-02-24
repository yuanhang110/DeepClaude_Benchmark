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

func Right() {
	Step1Robot.Dir = (Step1Robot.Dir + 1) % 4
}

func Left() {
	Step1Robot.Dir = (Step1Robot.Dir - 1 + 4) % 4
}

func Advance() {
	switch Step1Robot.Dir {
	case N:
		Step1Robot.Y++
	case E:
		Step1Robot.X++
	case S:
		Step1Robot.Y--
	case W:
		Step1Robot.X--
	}
}

var dirNames = []string{"N", "E", "S", "W"}

func (d Dir) String() string {
	if d < 0 || int(d) >= len(dirNames) {
		return "Unknown"
	}
	return dirNames[d]
}

// Step 2
// Define Action type here.
type Action int

const (
	ActRight Action = iota
	ActLeft
	ActAdvance
)

func StartRobot(command chan Command, action chan Action) {
	for cmd := range command {
		switch cmd {
		case 'R':
			action <- ActRight
		case 'L':
			action <- ActLeft
		case 'A':
			action <- ActAdvance
		}
	}
	close(action)
}

func Room(extent Rect, robot Step2Robot, action chan Action, report chan Step2Robot) {
	for act := range action {
		switch act {
		case ActRight:
			robot.Dir = (robot.Dir + 1) % 4
		case ActLeft:
			robot.Dir = (robot.Dir - 1 + 4) % 4
		case ActAdvance:
			pos := robot.Pos
			switch robot.Dir {
			case N:
				pos.Northing++
			case E:
				pos.Easting++
			case S:
				pos.Northing--
			case W:
				pos.Easting--
			}
			// Check if new position is within room boundaries
			if pos.X >= extent.Min.Easting && pos.X <= extent.Max.Easting &&
				pos.Y >= extent.Min.Northing && pos.Y <= extent.Max.Northing {
				robot.Pos = pos
			}
		}
	}
	report <- robot
	close(report)
}

// Step 3
// Define Action3 type here.
type Action3 struct {
	Name   string
	Action Action
}

import "fmt"

func StartRobot3(name, script string, action chan Action3, log chan string) {
	if name == "" {
		log <- "Robot has no name"
		return
	}

	for _, cmd := range script {
		switch cmd {
		case 'R':
			action <- Action3{name, Right}
		case 'L':
			action <- Action3{name, Left}
		case 'A':
			action <- Action3{name, Advance}
		default:
			log <- fmt.Sprintf("Undefined command: %c", cmd)
		}
	}
	close(action)
}

func Room3(extent Rect, robots []Step3Robot, action chan Action3, rep chan []Step3Robot, log chan string) {
	robotMap := make(map[string]*Step3Robot)
	positions := make(map[Pos]string)
	
	// Initialize robot positions
	for i, r := range robots {
		if r.Name == "" {
			log <- "Unnamed robot"
			continue
		}
		if _, exists := robotMap[r.Name]; exists {
			log <- fmt.Sprintf("Duplicate name: %s", r.Name)
			continue
		}
		if r.Pos.X < extent.Min.Easting || r.Pos.X > extent.Max.Easting ||
			r.Pos.Y < extent.Min.Northing || r.Pos.Y > extent.Max.Northing {
			log <- fmt.Sprintf("%s placed outside room", r.Name)
			continue
		}
		if other, exists := positions[r.Pos]; exists {
			log <- fmt.Sprintf("%s and %s placed at same position", r.Name, other)
			continue
		}
		
		robotMap[r.Name] = &robots[i]
		positions[r.Pos] = r.Name
	}

	// Process actions
	for act := range action {
		robot, exists := robotMap[act.Name]
		if !exists {
			log <- fmt.Sprintf("Unknown robot: %s", act.Name)
			continue
		}

		oldPos := robot.Pos
		newPos := oldPos
		
		switch act.Action {
		case Right:
			robot.Dir = (robot.Dir + 1) % 4
		case Left:
			robot.Dir = (robot.Dir - 1 + 4) % 4
		case Advance:
			switch robot.Dir {
			case N:
				newPos.Northing++
			case E:
				newPos.Easting++
			case S:
				newPos.Northing--
			case W:
				newPos.Easting--
			}

			// Check boundaries
			if newPos.X < extent.Min.Easting || newPos.X > extent.Max.Easting ||
				newPos.Y < extent.Min.Northing || newPos.Y > extent.Max.Northing {
				log <- fmt.Sprintf("%s attempted to move into wall", act.Name)
				continue
			}

			// Check collisions
			if other, exists := positions[newPos]; exists {
				log <- fmt.Sprintf("%s attempted to move into %s", act.Name, other)
				continue
			}

			// Update position
			delete(positions, oldPos)
			positions[newPos] = act.Name
			robot.Pos = newPos
		}
	}

	rep <- robots
	close(rep)
	close(log)
}
