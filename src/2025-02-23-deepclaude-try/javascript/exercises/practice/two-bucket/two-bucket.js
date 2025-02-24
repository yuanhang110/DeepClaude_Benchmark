//
// This is only a SKELETON file for the 'Two Bucket' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export class TwoBucket {
  constructor(bucket1, bucket2, goal, startBucket) {
    if (goal === 0) {
      throw new Error('Goal cannot be zero');
    }

    if (goal > bucket1 && goal > bucket2) {
      throw new Error('Goal is larger than both buckets');
    }

    const gcd = (a, b) => b === 0 ? a : gcd(b, a % b);
    const greatestCommonDivisor = gcd(bucket1, bucket2);
    if (goal % greatestCommonDivisor !== 0) {
      throw new Error('Goal is not achievable with these bucket sizes');
    }

    this.bucket1Size = bucket1;
    this.bucket2Size = bucket2;
    this.goal = goal;
    this.startBucket = startBucket;
  }

  solve() {
    const startBucket = this.startBucket;
    const [initialB1, initialB2] = startBucket === 'one' 
      ? [this.bucket1Size, 0] 
      : [0, this.bucket2Size];

    const queue = [{ b1: initialB1, b2: initialB2, moves: 1 }];
    const visited = new Set([`${initialB1},${initialB2}`]);

    while (queue.length > 0) {
      const current = queue.shift();

      // Check if current state contains the goal
      if (current.b1 === this.goal) {
        return {
          moves: current.moves,
          goalBucket: 'one',
          otherBucket: current.b2
        };
      }
      if (current.b2 === this.goal) {
        return {
          moves: current.moves,
          goalBucket: 'two',
          otherBucket: current.b1
        };
      }

      // Generate all possible next states
      const nextStates = [
        // Fill bucket 1
        { b1: this.bucket1Size, b2: current.b2, moves: current.moves + 1 },
        // Fill bucket 2
        { b1: current.b1, b2: this.bucket2Size, moves: current.moves + 1 },
        // Empty bucket 1
        { b1: 0, b2: current.b2, moves: current.moves + 1 },
        // Empty bucket 2
        { b1: current.b1, b2: 0, moves: current.moves + 1 },
        // Pour from 1 to 2
        {
          b1: current.b1 - Math.min(current.b1, this.bucket2Size - current.b2),
          b2: current.b2 + Math.min(current.b1, this.bucket2Size - current.b2),
          moves: current.moves + 1
        },
        // Pour from 2 to 1
        {
          b1: current.b1 + Math.min(current.b2, this.bucket1Size - current.b1),
          b2: current.b2 - Math.min(current.b2, this.bucket1Size - current.b1),
          moves: current.moves + 1
        }
      ];

      for (const state of nextStates) {
        // Check invalid state rule
        const invalidState = startBucket === 'one'
          ? (state.b1 === 0 && state.b2 === this.bucket2Size)
          : (state.b2 === 0 && state.b1 === this.bucket1Size);
        if (invalidState) continue;

        // Check if we've already visited this state
        const stateKey = `${state.b1},${state.b2}`;
        if (!visited.has(stateKey)) {
          visited.add(stateKey);
          queue.push(state);

          // Check immediately if this new state is a solution
          if (state.b1 === this.goal) {
            return {
              moves: state.moves,
              goalBucket: 'one',
              otherBucket: state.b2
            };
          }
          if (state.b2 === this.goal) {
            return {
              moves: state.moves,
              goalBucket: 'two',
              otherBucket: state.b1
            };
          }
        }
      }
    }

    throw new Error('No solution exists');
  }
}
