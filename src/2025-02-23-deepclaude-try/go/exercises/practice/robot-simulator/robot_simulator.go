package robot

import "fmt"

// See defs.go for other definitions

var (
    currentDir Dir
    posX, posY int
)

// Step 1
// Define N, E, S, W here.
const (
    N Dir = iota
    E
    S
    W
)

func Right() {
    currentDir = (currentDir + 1) % 4
}

func Left() {
    currentDir = (currentDir - 1 + 4) % 4
}

func Advance() {
    switch currentDir {
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
    return [...]string{"N", "E", "S", "W"}[d]
}

// Step 2
// Define Action type here.
type Action int

const (
    RightAction Action = iota
    LeftAction
    AdvanceAction
)

func StartRobot(command chan Command, action chan Action) {
    for cmd := range command {
        switch cmd {
        case 'R':
            action <- RightAction
        case 'L':
            action <- LeftAction
        case 'A':
            action <- AdvanceAction
        }
    }
    close(action)
}

func Room(extent Rect, robot Step2Robot, action chan Action, report chan Step2Robot) {
    pos := robot.Pos
    dir := robot.Dir

    for act := range action {
        switch act {
        case RightAction:
            dir = (dir + 1) % 4
        case LeftAction:
            dir = (dir - 1 + 4) % 4
        case AdvanceAction:
            newPos := pos
            switch dir {
            case N:
                newPos.y++
            case E:
                newPos.x++
            case S:
                newPos.y--
            case W:
                newPos.x--
            }
            if newPos.x >= extent.Min.x && newPos.x <= extent.Max.x &&
                newPos.y >= extent.Min.y && newPos.y <= extent.Max.y {
                pos = newPos
            }
        }
    }

    report <- Step2Robot{Pos: pos, Dir: dir}
    close(report)
}

// Step 3
// Define Action3 type here.
type Action3 struct {
    Name   string
    Action Action
}

func StartRobot3(name, script string, action chan Action3, log chan string) {
    if name == "" {
        log <- "Robot has no name"
        return
    }
    for _, c := range script {
        var act Action
        switch c {
        case 'R':
            act = RightAction
        case 'L':
            act = LeftAction
        case 'A':
            act = AdvanceAction
        default:
            log <- "Undefined command: " + string(c)
            return
        }
        action <- Action3{Name: name, Action: act}
    }
    close(action)
}

func Room3(extent Rect, robots []Step3Robot, action chan Action3, rep chan []Step3Robot, log chan string) {
    robotMap := make(map[string]*Step3Robot)
    posMap := make(map[Vec2D]string)

    // Initial placement
    for _, r := range robots {
        if r.Name == "" {
            log <- "Robot has no name"
            continue
        }
        if _, exists := robotMap[r.Name]; exists {
            log <- "Duplicate robot name: " + r.Name
            continue
        }
        if r.Pos.X < extent.Min.X || r.Pos.X > extent.Max.X ||
            r.Pos.Y < extent.Min.Y || r.Pos.Y > extent.Max.Y {
            log <- "Robot placed outside room: " + r.Name
            continue
        }
        if other, exists := posMap[r.Pos]; exists {
            log <- fmt.Sprintf("Robot %s placed at same position as %s", r.Name, other)
            continue
        }

        robotMap[r.Name] = &Step3Robot{Name: r.Name, Pos: r.Pos, Dir: r.Dir}
        posMap[r.Pos] = r.Name
    }

    // Process actions
    for a := range action {
        r, exists := robotMap[a.Name]
        if !exists {
            log <- "Action from unknown robot: " + a.Name
            continue
        }

        switch a.Action {
        case RightAction:
            r.Dir = (r.Dir + 1) % 4
        case LeftAction:
            r.Dir = (r.Dir - 1 + 4) % 4
        case AdvanceAction:
            newPos := r.Pos
            switch r.Dir {
            case N:
                newPos.Y++
            case E:
                newPos.X++
            case S:
                newPos.Y--
            case W:
                newPos.X--
            }

            // Check room boundaries
            if newPos.X < extent.Min.X || newPos.X > extent.Max.X ||
                newPos.Y < extent.Min.Y || newPos.Y > extent.Max.Y {
                log <- fmt.Sprintf("Robot %s attempted to move into wall", a.Name)
                continue
            }

            // Check for other robots
            if other, exists := posMap[newPos]; exists {
                log <- fmt.Sprintf("Robot %s attempted to move into %s", a.Name, other)
                continue
            }

            // Update position
            delete(posMap, r.Pos)
            r.Pos = newPos
            posMap[newPos] = a.Name
        }
    }

    // Prepare report
    var result []Step3Robot
    for _, r := range robotMap {
        result = append(result, *r)
    }
    rep <- result
    close(rep)
    close(log)
}
