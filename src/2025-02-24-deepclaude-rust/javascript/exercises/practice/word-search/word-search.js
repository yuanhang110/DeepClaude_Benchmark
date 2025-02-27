//
// This is only a SKELETON file for the 'Word Search' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

class WordSearch {
  constructor(grid) {
    this.grid = grid;
    this.rows = grid.length;
    this.cols = grid[0].length;
  }

  find(words) {
    const directions = [
      [-1, -1], [-1, 0], [-1, 1],  // up-left, up, up-right
      [0, -1],           [0, 1],    // left, right
      [1, -1],  [1, 0],  [1, 1]     // down-left, down, down-right
    ];
    
    const result = {};
    
    words.forEach(word => {
      let found = false;
      
      // Try each starting position
      for (let row = 0; row < this.rows && !found; row++) {
        for (let col = 0; col < this.cols && !found; col++) {
          // Try each direction
          for (const [dx, dy] of directions) {
            if (this.checkWord(word, row, col, dx, dy)) {
              const endRow = row + dx * (word.length - 1);
              const endCol = col + dy * (word.length - 1);
              result[word] = {
                start: [row + 1, col + 1],
                end: [endRow + 1, endCol + 1]
              };
              found = true;
              break;
            }
          }
        }
      }
      
      if (!found) {
        result[word] = undefined;
      }
    });
    
    return result;
  }

  checkWord(word, startRow, startCol, dx, dy) {
    if (
      startRow + dx * (word.length - 1) < 0 ||
      startRow + dx * (word.length - 1) >= this.rows ||
      startCol + dy * (word.length - 1) < 0 ||
      startCol + dy * (word.length - 1) >= this.cols
    ) {
      return false;
    }

    for (let i = 0; i < word.length; i++) {
      const row = startRow + dx * i;
      const col = startCol + dy * i;
      if (this.grid[row][col] !== word[i]) {
        return false;
      }
    }
    return true;
  }
}

export default WordSearch;
