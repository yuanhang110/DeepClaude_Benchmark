//
// This is only a SKELETON file for the 'Rectangles' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export function count(lines) {
  const corners = [];
  for (let y = 0; y < lines.length; y++) {
    const line = lines[y];
    for (let x = 0; x < line.length; x++) {
      if (line[x] === '+') {
        corners.push({x, y});
      }
    }
  }

  const cornerSet = new Set(corners.map(c => `${c.x},${c.y}`));
  let total = 0;

  const isHorizontalValid = (start, end) => {
    if (start.y !== end.y) return false;
    for (let x = Math.min(start.x, end.x); x <= Math.max(start.x, end.x); x++) {
      const c = lines[start.y][x];
      if (c !== '+' && c !== '-') return false;
    }
    return true;
  };

  const isVerticalValid = (start, end) => {
    if (start.x !== end.x) return false;
    for (let y = Math.min(start.y, end.y); y <= Math.max(start.y, end.y); y++) {
      const c = lines[y][start.x];
      if (c !== '+' && c !== '|') return false;
    }
    return true;
  };

  for (let i = 0; i < corners.length; i++) {
    const a = corners[i];
    for (let j = i + 1; j < corners.length; j++) {
      const b = corners[j];
      // Check if a and b could be diagonal corners
      if (a.x < b.x && a.y < b.y) {
        const cx = b.x;
        const cy = a.y;
        const dx = a.x;
        const dy = b.y;
        if (cornerSet.has(`${cx},${cy}`) && cornerSet.has(`${dx},${dy}`)) {
          // Check all four edges
          if (isHorizontalValid(a, {x: cx, y: cy}) &&
              isHorizontalValid({x: dx, y: dy}, b) &&
              isVerticalValid(a, {x: dx, y: dy}) &&
              isVerticalValid({x: cx, y: cy}, b)) {
            total++;
          }
        }
      }
    }
  }

  return total;
}
