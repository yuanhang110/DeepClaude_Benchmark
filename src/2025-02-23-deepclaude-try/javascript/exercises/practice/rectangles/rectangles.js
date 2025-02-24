export function count(diagram) {
  const corners = [];
  for (let y = 0; y < diagram.length; y++) {
    for (let x = 0; x < diagram[y].length; x++) {
      if (diagram[y][x] === '+') {
        corners.push({x, y});
      }
    }
  }

  let total = 0;

  for (let i = 0; i < corners.length; i++) {
    const a = corners[i];
    for (let j = i + 1; j < corners.length; j++) {
      const b = corners[j];
      if (a.x < b.x && a.y < b.y) {
        const topRight = {x: b.x, y: a.y};
        const bottomLeft = {x: a.x, y: b.y};
        if (isCorner(topRight, diagram) && isCorner(bottomLeft, diagram)) {
          if (checkHorizontal(a, topRight, diagram) &&
              checkHorizontal(bottomLeft, b, diagram) &&
              checkVertical(a, bottomLeft, diagram) &&
              checkVertical(topRight, b, diagram)) {
            total++;
          }
        }
      }
    }
  }

  return total;
}

function isCorner(point, diagram) {
  return (point.y < diagram.length && 
          point.x < diagram[point.y].length && 
          diagram[point.y][point.x] === '+');
}

function checkHorizontal(start, end, diagram) {
  if (start.y !== end.y || start.x >= end.x) return false;
  for (let x = start.x; x <= end.x; x++) {
    const c = diagram[start.y][x];
    if (c !== '+' && c !== '-') return false;
  }
  return true;
}

function checkVertical(start, end, diagram) {
  if (start.x !== end.x || start.y >= end.y) return false;
  for (let y = start.y; y <= end.y; y++) {
    const c = diagram[y][start.x];
    if (c !== '+' && c !== '|') return false;
  }
  return true;
}
