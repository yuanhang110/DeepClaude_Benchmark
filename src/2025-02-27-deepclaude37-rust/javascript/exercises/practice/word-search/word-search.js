//
// This is only a SKELETON file for the 'Word Search' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

class WordSearch {
  constructor(grid) {
    this.grid = grid.map(row => row.split(''));
    this.rows = grid.length;
    this.cols = this.grid[0]?.length || 0;
  }

  find(words) {
    const results = {};
    const directions = [
      [-1, 0],  // up
      [1, 0],   // down
      [0, 1],   // right
      [0, -1],  // left
      [-1, 1],  // up-right
      [-1, -1], // up-left
      [1, 1],   // down-right
      [1, -1]   // down-left
    ];

    for (const word of words) {
      let found = false;
      
      for (let row = 0; row < this.rows && !found; row++) {
        for (let col = 0; col < this.cols && !found; col++) {
          if (this.grid[row][col] !== word[0]) continue;
          
          for (const [dr, dc] of directions) {
            let matched = true;
            let r = row;
            let c = col;
            
            for (let i = 0; i < word.length; i++) {
              if (r < 0 || r >= this.rows || c < 0 || c >= this.cols || 
                  this.grid[r][c] !== word[i]) {
                matched = false;
                break;
              }
              
              if (i < word.length - 1) {
                r += dr;
                c += dc;
              }
            }
            
            if (matched) {
              const endRow = row + dr * (word.length - 1);
              const endCol = col + dc * (word.length - 1);
              results[word] = {
                start: [row + 1, col + 1],  // Convert to 1-based indexing
                end: [endRow + 1, endCol + 1]  // Convert to 1-based indexing
              };
              found = true;
              break;
            }
          }
        }
      }
      
      if (!found) {
        results[word] = undefined;
      }
    }

    return results;
  }
}

export default WordSearch;
