//
// This is only a SKELETON file for the 'Queen Attack' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export class QueenAttack {
  constructor({
    black: [blackRow, blackColumn] = [7, 3],
    white: [whiteRow, whiteColumn] = [0, 3],
  } = {}) {
    if (
      blackRow < 0 || blackRow > 7 ||
      blackColumn < 0 || blackColumn > 7 ||
      whiteRow < 0 || whiteRow > 7 ||
      whiteColumn < 0 || whiteColumn > 7
    ) {
      throw new Error('Queen must be placed on the board');
    }

    if (blackRow === whiteRow && blackColumn === whiteColumn) {
      throw new Error('Queens cannot share the same space');
    }

    this.black = [blackRow, blackColumn];
    this.white = [whiteRow, whiteColumn];
  }

  toString() {
    const board = Array.from({length: 8}, () => Array(8).fill('_'));
    board[this.black[0]][this.black[1]] = 'B';
    board[this.white[0]][this.white[1]] = 'W';
    return board.reverse()
      .map(row => row.join(' '))
      .join('\n') + '\n';
  }

  get canAttack() {
    const [br, bc] = this.black;
    const [wr, wc] = this.white;
    return br === wr || bc === wc || Math.abs(br - wr) === Math.abs(bc - wc);
  }
}
