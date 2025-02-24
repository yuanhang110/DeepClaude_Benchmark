//
// This is only a SKELETON file for the 'Go Counting' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export class GoCounting {
  constructor(board) {
    this.board = board.map(row => [...row]);
    this.rows = board.length;
    this.cols = this.rows > 0 ? board[0].length : 0;
  }

  getTerritory(x, y) {
    if (x < 0 || x >= this.cols || y < 0 || y >= this.rows) {
      return { owner: 'NONE', territory: [] };
    }

    if (this.board[y][x] !== ' ') {
      return { owner: 'NONE', territory: [] };
    }

    const visited = Array.from({ length: this.rows }, () => new Array(this.cols).fill(false));
    const queue = [[x, y]];
    visited[y][x] = true;
    const territory = [];
    const adjacentStones = new Set();
    let isBordered = true;

    while (queue.length > 0) {
      const [currentX, currentY] = queue.shift();
      territory.push([currentX, currentY]);

      const directions = [[0, 1], [1, 0], [0, -1], [-1, 0]];
      for (const [dx, dy] of directions) {
        const newX = currentX + dx;
        const newY = currentY + dy;

        if (newX < 0 || newX >= this.cols || newY < 0 || newY >= this.rows) {
          isBordered = false;
          continue;
        }

        const neighbor = this.board[newY][newX];
        if (neighbor === ' ') {
          if (!visited[newY][newX]) {
            visited[newY][newX] = true;
            queue.push([newX, newY]);
          }
        } else {
          adjacentStones.add(neighbor);
        }
      }
    }

    let owner;
    if (!isBordered) {
      owner = 'NONE';
    } else {
      if (adjacentStones.size === 1) {
        owner = adjacentStones.has('B') ? 'BLACK' : 'WHITE';
      } else {
        owner = 'NONE';
      }
    }

    return {
      owner: owner,
      territory: territory.sort((a, b) => a[0] - b[0] || a[1] - b[1])
    };
  }

  getTerritories() {
    const visited = Array.from({ length: this.rows }, () => new Array(this.cols).fill(false));
    const result = {
      territoryBlack: [],
      territoryWhite: [],
      territoryNone: []
    };

    for (let y = 0; y < this.rows; y++) {
      for (let x = 0; x < this.cols; x++) {
        if (this.board[y][x] !== ' ' || visited[y][x]) {
          continue;
        }

        const queue = [[x, y]];
        visited[y][x] = true;
        const territory = [];
        const adjacentStones = new Set();
        let isBordered = true;

        while (queue.length > 0) {
          const [currentX, currentY] = queue.shift();
          territory.push([currentX, currentY]);

          const directions = [[0, 1], [1, 0], [0, -1], [-1, 0]];
          for (const [dx, dy] of directions) {
            const newX = currentX + dx;
            const newY = currentY + dy;

            if (newX < 0 || newX >= this.cols || newY < 0 || newY >= this.rows) {
              isBordered = false;
              continue;
            }

            const neighbor = this.board[newY][newX];
            if (neighbor === ' ') {
              if (!visited[newY][newX]) {
                visited[newY][newX] = true;
                queue.push([newX, newY]);
              }
            } else {
              adjacentStones.add(neighbor);
            }
          }
        }

        let owner;
        if (!isBordered) {
          owner = 'NONE';
        } else {
          if (adjacentStones.size === 1) {
            owner = adjacentStones.has('B') ? 'BLACK' : 'WHITE';
          } else {
            owner = 'NONE';
          }
        }

        const sortedTerritory = territory.sort((a, b) => a[0] - b[0] || a[1] - b[1]);
        if (owner === 'BLACK') {
          result.territoryBlack.push(...sortedTerritory);
        } else if (owner === 'WHITE') {
          result.territoryWhite.push(...sortedTerritory);
        } else {
          result.territoryNone.push(...sortedTerritory);
        }
      }
    }

    // Sort each territory array
    result.territoryBlack.sort((a, b) => a[0] - b[0] || a[1] - b[1]);
    result.territoryWhite.sort((a, b) => a[0] - b[0] || a[1] - b[1]);
    result.territoryNone.sort((a, b) => a[0] - b[0] || a[1] - b[1]);

    return result;
  }
}
