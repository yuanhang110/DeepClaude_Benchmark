//
// This is only a SKELETON file for the 'Go Counting' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export class GoCounting {
  constructor(board) {
    this.board = board.map(row => row.split(''));
    this.height = this.board.length;
    this.width = this.height > 0 ? this.board[0].length : 0;
  }

  getTerritory(x, y) {
    if (x < 0 || x >= this.width || y < 0 || y >= this.height) {
      return { owner: 'NONE', territory: [] };
    }

    const cell = this.board[y][x];
    if (cell !== ' ') {
      return { owner: 'NONE', territory: [] };
    }

    const visited = new Set();
    const queue = [[x, y]];
    visited.add(`${x},${y}`);
    let borders = new Set();
    let hasEdge = false;

    while (queue.length > 0) {
      const [currentX, currentY] = queue.shift();
      const directions = [[0, 1], [0, -1], [1, 0], [-1, 0]];
      for (const [dx, dy] of directions) {
        const nx = currentX + dx;
        const ny = currentY + dy;

        if (nx < 0 || nx >= this.width || ny < 0 || ny >= this.height) {
          hasEdge = true;
        } else {
          const neighborCell = this.board[ny][nx];
          if (neighborCell === 'B') {
            borders.add('B');
          } else if (neighborCell === 'W') {
            borders.add('W');
          } else if (neighborCell === ' ' && !visited.has(`${nx},${ny}`)) {
            visited.add(`${nx},${ny}`);
            queue.push([nx, ny]);
          }
        }
      }
    }

    const territory = Array.from(visited).map(s => s.split(',').map(Number))
      .sort((a, b) => a[0] - b[0] || a[1] - b[1]);

    if (hasEdge) {
      return { owner: 'NONE', territory };
    }

    const colors = Array.from(borders);
    if (colors.length === 1) {
      return {
        owner: colors[0] === 'B' ? 'BLACK' : 'WHITE',
        territory
      };
    } else {
      return { owner: 'NONE', territory };
    }
  }

  getTerritories() {
    const visited = new Set();
    const territories = {
      territoryBlack: [],
      territoryWhite: [],
      territoryNone: []
    };

    for (let y = 0; y < this.height; y++) {
      for (let x = 0; x < this.width; x++) {
        const key = `${x},${y}`;
        if (this.board[y][x] === ' ' && !visited.has(key)) {
          const { owner, territory } = this.getTerritory(x, y);
          territory.forEach(([tx, ty]) => {
            visited.add(`${tx},${ty}`);
          });
          if (owner === 'BLACK') {
            territories.territoryBlack.push(...territory);
          } else if (owner === 'WHITE') {
            territories.territoryWhite.push(...territory);
          } else {
            territories.territoryNone.push(...territory);
          }
        }
      }
    }

    territories.territoryBlack.sort((a, b) => a[0] - b[0] || a[1] - b[1]);
    territories.territoryWhite.sort((a, b) => a[0] - b[0] || a[1] - b[1]);
    territories.territoryNone.sort((a, b) => a[0] - b[0] || a[1] - b[1]);

    return territories;
  }
}
