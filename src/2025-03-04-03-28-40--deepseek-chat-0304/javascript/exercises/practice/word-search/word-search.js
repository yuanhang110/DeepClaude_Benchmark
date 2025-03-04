class WordSearch {
  constructor(grid) {
    this.grid = grid;
  }

  find(words) {
    const results = {};
    
    for (const word of words) {
      const result = this.searchWord(word);
      results[word] = result || undefined;
    }
    
    return results;
  }

  searchWord(word) {
    // Search in all 8 possible directions
    const directions = [
      [1, 0],   // right
      [1, 1],   // right-down
      [0, 1],   // down
      [-1, 1],  // left-down
      [-1, 0],  // left
      [-1, -1], // left-up
      [0, -1],  // up
      [1, -1]   // right-up
    ];

    for (let row = 0; row < this.grid.length; row++) {
      for (let col = 0; col < this.grid[row].length; col++) {
        for (const [dx, dy] of directions) {
          const end = this.checkDirection(word, row, col, dx, dy);
          if (end) {
            return {
              start: [row + 1, col + 1], // 1-based indexing
              end: [end.row + 1, end.col + 1]
            };
          }
        }
      }
    }
    
    return undefined;
  }

  checkDirection(word, row, col, dx, dy) {
    for (let i = 0; i < word.length; i++) {
      const newRow = row + i * dy;
      const newCol = col + i * dx;
      
      if (newRow < 0 || newRow >= this.grid.length || 
          newCol < 0 || newCol >= this.grid[newRow].length ||
          this.grid[newRow][newCol] !== word[i]) {
        return null;
      }
    }
    
    return {
      row: row + (word.length - 1) * dy,
      col: col + (word.length - 1) * dx
    };
  }
}

export default WordSearch;
