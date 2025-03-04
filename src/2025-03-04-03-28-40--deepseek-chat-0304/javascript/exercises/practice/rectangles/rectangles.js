export function count(diagram) {
    if (diagram.length === 0) return 0;
    
    // Find all corner positions
    const corners = [];
    for (let y = 0; y < diagram.length; y++) {
        for (let x = 0; x < diagram[y].length; x++) {
            if (diagram[y][x] === '+') {
                corners.push({x, y});
            }
        }
    }
    
    let rectangleCount = 0;
    
    // Check all pairs of corners to find rectangles
    for (let i = 0; i < corners.length; i++) {
        for (let j = i + 1; j < corners.length; j++) {
            const a = corners[i];
            const b = corners[j];
            
            // Check if these could be diagonal corners of a rectangle
            if (a.x < b.x && a.y < b.y) {
                // Check if the other two corners exist
                const topRight = {x: b.x, y: a.y};
                const bottomLeft = {x: a.x, y: b.y};
                
                if (isCorner(diagram, topRight) && isCorner(diagram, bottomLeft)) {
                    // Check if all sides are valid
                    if (isValidHorizontal(diagram, a, topRight) &&
                        isValidHorizontal(diagram, bottomLeft, b) &&
                        isValidVertical(diagram, a, bottomLeft) &&
                        isValidVertical(diagram, topRight, b)) {
                        rectangleCount++;
                    }
                }
            }
        }
    }
    
    return rectangleCount;
}

function isCorner(diagram, pos) {
    return diagram[pos.y] && diagram[pos.y][pos.x] === '+';
}

function isValidHorizontal(diagram, start, end) {
    for (let x = start.x + 1; x < end.x; x++) {
        const char = diagram[start.y][x];
        if (char !== '-' && char !== '+') return false;
    }
    return true;
}

function isValidVertical(diagram, start, end) {
    for (let y = start.y + 1; y < end.y; y++) {
        const char = diagram[y][start.x];
        if (char !== '|' && char !== '+') return false;
    }
    return true;
}
