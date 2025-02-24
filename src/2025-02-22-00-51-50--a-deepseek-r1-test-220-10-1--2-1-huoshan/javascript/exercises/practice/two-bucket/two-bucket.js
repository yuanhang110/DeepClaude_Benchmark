//
// This is only a SKELETON file for the 'Two Bucket' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export class TwoBucket {
  constructor(bucket1Size, bucket2Size, goal, startBucket) {
    if (goal > Math.max(bucket1Size, bucket2Size)) {
      throw new Error('Goal cannot be larger than both buckets');
    }
    
    const gcdVal = TwoBucket.gcd(bucket1Size, bucket2Size);
    if (goal !== bucket1Size && goal !== bucket2Size && goal % gcdVal !== 0) {
      throw new Error('No solution possible');
    }

    this.bucket1Size = bucket1Size;
    this.bucket2Size = bucket2Size;
    this.goal = goal;
    this.startBucket = startBucket;
  }

  static gcd(a, b) {
    while (b !== 0) {
      [a, b] = [b, a % b];
    }
    return a;
  }

  solve() {
    const queue = [];
    const visited = new Set();

    // Initialize based on starting bucket
    let initialState;
    if (this.startBucket === 'one') {
      initialState = { b1: this.bucket1Size, b2: 0, moves: 1 };
    } else {
      initialState = { b1: 0, b2: this.bucket2Size, moves: 1 };
    }

    // Check initial state immediately
    if (initialState.b1 === this.goal) {
      return {
        moves: initialState.moves,
        goalBucket: 'one',
        otherBucket: initialState.b2
      };
    }
    if (initialState.b2 === this.goal) {
      return {
        moves: initialState.moves,
        goalBucket: 'two',
        otherBucket: initialState.b1
      };
    }

    queue.push(initialState);
    visited.add(`${initialState.b1},${initialState.b2}`);

    while (queue.length > 0) {
      const current = queue.shift();

      for (const next of this.generateNextStates(current)) {
        const stateKey = `${next.b1},${next.b2}`;
        
        if (visited.has(stateKey)) continue;
        if (this.isForbiddenState(next)) continue;

        // Check if we've reached the goal
        if (next.b1 === this.goal) {
          return {
            moves: next.moves,
            goalBucket: 'one',
            otherBucket: next.b2
          };
        }
        if (next.b2 === this.goal) {
          return {
            moves: next.moves,
            goalBucket: 'two',
            otherBucket: next.b1
          };
        }

        visited.add(stateKey);
        queue.push(next);
      }
    }

    throw new Error('No solution found');
  }

  generateNextStates(current) {
    const { b1, b2, moves } = current;
    const nextStates = [];

    // Fill operations
    if (b1 < this.bucket1Size) {
      nextStates.push({ b1: this.bucket1Size, b2, moves: moves + 1 });
    }
    if (b2 < this.bucket2Size) {
      nextStates.push({ b1, b2: this.bucket2Size, moves: moves + 1 });
    }

    // Empty operations
    if (b1 > 0) {
      nextStates.push({ b1: 0, b2, moves: moves + 1 });
    }
    if (b2 > 0) {
      nextStates.push({ b1, b2: 0, moves: moves + 1 });
    }

    // Pour operations
    const pour1to2 = Math.min(b1, this.bucket2Size - b2);
    if (pour1to2 > 0) {
      nextStates.push({
        b1: b1 - pour1to2,
        b2: b2 + pour1to2,
        moves: moves + 1
      });
    }

    const pour2to1 = Math.min(b2, this.bucket1Size - b1);
    if (pour2to1 > 0) {
      nextStates.push({
        b1: b1 + pour2to1,
        b2: b2 - pour2to1,
        moves: moves + 1
      });
    }

    return nextStates;
  }

  isForbiddenState(state) {
    if (this.startBucket === 'one') {
      return state.b1 === 0 && state.b2 === this.bucket2Size;
    }
    return state.b2 === 0 && state.b1 === this.bucket1Size;
  }
}
