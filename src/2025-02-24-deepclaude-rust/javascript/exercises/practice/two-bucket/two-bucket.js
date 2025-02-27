//
// This is only a SKELETON file for the 'Two Bucket' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export class TwoBucket {
  static gcd(a, b) {
    while (b !== 0) {
      let t = b;
      b = a % b;
      a = t;
    }
    return a;
  }

  constructor(bucketOne, bucketTwo, goal, startBucket) {
    // Check if goal is larger than both buckets
    if (goal > Math.max(bucketOne, bucketTwo)) {
      throw new Error('Goal is larger than both buckets');
    }

    // Check if goal is achievable using GCD
    const gcd = TwoBucket.gcd(bucketOne, bucketTwo);
    if (goal % gcd !== 0) {
      throw new Error('Goal cannot be reached');
    }

    this.bucketOneCap = bucketOne;
    this.bucketTwoCap = bucketTwo;
    this.goal = goal;
    this.startBucket = startBucket;
  }

  solve() {
    const queue = [];
    const visited = new Set();

    let initialState;
    if (this.startBucket === 'one') {
      initialState = { a: this.bucketOneCap, b: 0, moves: 1 };
    } else {
      initialState = { a: 0, b: this.bucketTwoCap, moves: 1 };
    }

    visited.add(`${initialState.a},${initialState.b}`);
    queue.push(initialState);

    while (queue.length > 0) {
      const current = queue.shift();

      if (current.a === this.goal) {
        return {
          moves: current.moves,
          goalBucket: 'one',
          otherBucket: current.b
        };
      }
      if (current.b === this.goal) {
        return {
          moves: current.moves,
          goalBucket: 'two',
          otherBucket: current.a
        };
      }

      // Generate next states
      const nextStates = [];

      // Fill bucket one
      if (current.a < this.bucketOneCap) {
        const newA = this.bucketOneCap;
        const newB = current.b;
        if (newA !== current.a || newB !== current.b) {
          nextStates.push({ a: newA, b: newB, moves: current.moves + 1 });
        }
      }

      // Fill bucket two
      if (current.b < this.bucketTwoCap) {
        const newB = this.bucketTwoCap;
        const newA = current.a;
        if (newB !== current.b || newA !== current.a) {
          nextStates.push({ a: newA, b: newB, moves: current.moves + 1 });
        }
      }

      // Empty bucket one
      if (current.a > 0) {
        const newA = 0;
        const newB = current.b;
        if (newA !== current.a || newB !== current.b) {
          nextStates.push({ a: newA, b: newB, moves: current.moves + 1 });
        }
      }

      // Empty bucket two
      if (current.b > 0) {
        const newB = 0;
        const newA = current.a;
        if (newB !== current.b || newA !== current.a) {
          nextStates.push({ a: newA, b: newB, moves: current.moves + 1 });
        }
      }

      // Pour from a to b
      if (current.a > 0 && current.b < this.bucketTwoCap) {
        const transfer = Math.min(current.a, this.bucketTwoCap - current.b);
        const newA = current.a - transfer;
        const newB = current.b + transfer;
        nextStates.push({ a: newA, b: newB, moves: current.moves + 1 });
      }

      // Pour from b to a
      if (current.b > 0 && current.a < this.bucketOneCap) {
        const transfer = Math.min(current.b, this.bucketOneCap - current.a);
        const newB = current.b - transfer;
        const newA = current.a + transfer;
        nextStates.push({ a: newA, b: newB, moves: current.moves + 1 });
      }

      for (const state of nextStates) {
        // Check if the state is invalid based on the starting bucket rule
        let invalid = false;
        if (this.startBucket === 'one') {
          invalid = (state.a === 0 && state.b === this.bucketTwoCap);
        } else {
          invalid = (state.b === 0 && state.a === this.bucketOneCap);
        }

        if (!invalid) {
          const key = `${state.a},${state.b}`;
          if (!visited.has(key)) {
            visited.add(key);
            queue.push(state);
          }
        }
      }
    }

    throw new Error('No solution possible');
  }
}
