//
// This is only a SKELETON file for the 'Rectangles' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export function count(diagram) {
  if (!diagram || diagram.length === 0) return 0;
  
  // Collect all '+' positions
  const corners = [];
  for (let r = 0; r < diagram.length; r++) {
    for (let c = 0; c < diagram[r].length; c++) {
      if (diagram[r][c] === '+') {
        corners.push([r, c]);
      }
    }
  }
  
  // Create a set for faster corner lookup
  const cornerSet = new Set(corners.map(([r, c]) => `${r},${c}`));
  
  let rectCount = 0;
  
  // Check all possible top-left and bottom-right corner pairs
  for (const [r1, c1] of corners) {
    for (const [r2, c2] of corners) {
      // Only consider if second point is below and to the right of first point
      if (r2 > r1 && c2 > c1) {
        // Check if the other two corners exist
        if (cornerSet.has(`${r1},${c2}`) && cornerSet.has(`${r2},${c1}`)) {
          // Check top edge
          let topValid = true;
          for (let c = c1 + 1; c < c2; c++) {
            if (diagram[r1][c] !== '-' && diagram[r1][c] !== '+') {
              topValid = false;
              break;
            }
          }
          if (!topValid) continue;
          
          // Check bottom edge
          let bottomValid = true;
          for (let c = c1 + 1; c < c2; c++) {
            if (diagram[r2][c] !== '-' && diagram[r2][c] !== '+') {
              bottomValid = false;
              break;
            }
          }
          if (!bottomValid) continue;
          
          // Check left edge
          let leftValid = true;
          for (let r = r1 + 1; r < r2; r++) {
            if (diagram[r][c1] !== '|' && diagram[r][c1] !== '+') {
              leftValid = false;
              break;
            }
          }
          if (!leftValid) continue;
          
          // Check right edge
          let rightValid = true;
          for (let r = r1 + 1; r < r2; r++) {
            if (diagram[r][c2] !== '|' && diagram[r][c2] !== '+') {
              rightValid = false;
              break;
            }
          }
          if (!rightValid) continue;
          
          // If all edges are valid, we found a rectangle
          rectCount++;
        }
      }
    }
  }
  
  return rectCount;
}
