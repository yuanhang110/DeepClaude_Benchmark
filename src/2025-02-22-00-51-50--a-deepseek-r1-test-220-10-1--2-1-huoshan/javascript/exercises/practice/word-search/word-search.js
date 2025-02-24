//
// This is only a SKELETON file for the 'Word Search' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

class WordSearch {
  constructor(grid) {
    this.grid = grid.map(row => row.split(''));
    this.rows = this.grid.length;
    this.cols = this.rows > 0 ? this.grid[0].length : 0;
  }

  find(words) {
    const directions = [
      [0, 1],   // Right
      [1, 0],   // Down
      [0, -1],  // Left
      [-1, 0],  // Up
      [1, 1],   // Down-Right
      [1, -1],  // Down-Left
      [-1, 1],  // Up-Right
      [-1, -1]  // Up-Left
    ];
    
    const result = {};
    
    for (const word of words) {
      outer:
      for (let r = 0; r < this.rows; r++) {
        for (let c = 0; c < this.cols; c++) {
          if (this.grid[r][c] === word[0]) {
            for (const [dr, dc] of directions) {
              let found = true;
              let endRow = r, endCol = c;
              
              for (let i = 1; i < word.length; i++) {
                const nr = r + dr * i;
                const nc = c + dc * i;
                
                if (nr < 0 || nr >= this.rows || nc < 0 || nc >= this.cols || 
                    this.grid[nr][nc] !== word[i]) {
                  found = false;
                  break;
                }
                endRow = nr;
                endCol = nc;
              }
              
              if (found) {
                result[word] = {
                  start: [r + 1, c + 1],
                  end: [endRow + 1, endCol + 1]
                };
                break outer;
              }
            }
          }
        }
      }
    }
    
    return result;
  }
}

export default WordSearch;
