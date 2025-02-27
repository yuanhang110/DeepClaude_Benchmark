//
// This is only a SKELETON file for the 'Rectangles' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export function count(diagram) {
  const corners = [];
  for (let i = 0; i < diagram.length; i++) {
    for (let j = 0; j < diagram[i].length; j++) {
      if (diagram[i][j] === '+') {
        corners.push({ row: i, col: j });
      }
    }
  }

  let rectangleCount = 0;

  for (let i = 0; i < corners.length; i++) {
    const tl = corners[i];
    for (let j = i + 1; j < corners.length; j++) {
      const tr = corners[j];
      if (tr.row !== tl.row) continue;
      if (!isHorizontalValid(tl, tr, diagram)) continue;

      for (let k = 0; k < corners.length; k++) {
        const bl = corners[k];
        if (bl.col !== tl.col || bl.row <= tl.row) continue;
        if (!isVerticalValid(tl, bl, diagram)) continue;

        for (let l = 0; l < corners.length; l++) {
          const br = corners[l];
          if (br.col !== tr.col || br.row !== bl.row) continue;
          if (!isHorizontalValid(bl, br, diagram)) continue;
          if (!isVerticalValid(tr, br, diagram)) continue;

          rectangleCount++;
        }
      }
    }
  }

  return rectangleCount;
}

function isHorizontalValid(a, b, diagram) {
  if (a.row !== b.row) return false;
  for (let col = Math.min(a.col, b.col) + 1; col < Math.max(a.col, b.col); col++) {
    const c = diagram[a.row][col];
    if (c !== '-' && c !== '+') return false;
  }
  return true;
}

function isVerticalValid(a, b, diagram) {
  if (a.col !== b.col) return false;
  for (let row = Math.min(a.row, b.row) + 1; row < Math.max(a.row, b.row); row++) {
    const c = diagram[row][a.col];
    if (c !== '|' && c !== '+') return false;
  }
  return true;
}
