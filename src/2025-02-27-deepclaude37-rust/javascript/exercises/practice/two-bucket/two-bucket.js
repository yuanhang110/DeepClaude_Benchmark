//
// This is only a SKELETON file for the 'Two Bucket' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export class TwoBucket {
  constructor(bucketOne, bucketTwo, target, startBucket) {
    // Check if target is larger than both buckets
    if (target > bucketOne && target > bucketTwo) {
      throw new Error('Target cannot be larger than both buckets');
    }
    
    // Check if target is achievable using GCD
    const gcd = (a, b) => {
      while (b !== 0) {
        let temp = b;
        b = a % b;
        a = temp;
      }
      return a;
    };
    
    const gcdValue = gcd(bucketOne, bucketTwo);
    if (target % gcdValue !== 0) {
      throw new Error('Target is not reachable');
    }

    this.bucketOneCap = bucketOne;
    this.bucketTwoCap = bucketTwo;
    this.target = target;
    this.startBucket = startBucket;
  }

  solve() {
    const queue = [];
    const visited = new Set();

    // Set initial state after filling the starting bucket (first action)
    let initialA, initialB;
    if (this.startBucket === 'one') {
      initialA = this.bucketOneCap;
      initialB = 0;
    } else {
      initialA = 0;
      initialB = this.bucketTwoCap;
    }

    queue.push({ a: initialA, b: initialB, moves: 1 });
    visited.add(`${initialA},${initialB}`);

    while (queue.length > 0) {
      const current = queue.shift();

      // Check if target has been reached
      if (current.a === this.target) {
        return {
          moves: current.moves,
          goalBucket: 'one',
          otherBucket: current.b
        };
      }
      if (current.b === this.target) {
        return {
          moves: current.moves,
          goalBucket: 'two',
          otherBucket: current.a
        };
      }

      // Generate all possible next states
      const nextStates = [
        { a: this.bucketOneCap, b: current.b, moves: current.moves + 1 }, // Fill bucket one
        { a: current.a, b: this.bucketTwoCap, moves: current.moves + 1 }, // Fill bucket two
        { a: 0, b: current.b, moves: current.moves + 1 }, // Empty bucket one
        { a: current.a, b: 0, moves: current.moves + 1 }, // Empty bucket two
        { // Pour from bucket one to bucket two
          a: current.a - Math.min(current.a, this.bucketTwoCap - current.b),
          b: current.b + Math.min(current.a, this.bucketTwoCap - current.b),
          moves: current.moves + 1
        },
        { // Pour from bucket two to bucket one
          a: current.a + Math.min(current.b, this.bucketOneCap - current.a),
          b: current.b - Math.min(current.b, this.bucketOneCap - current.a),
          moves: current.moves + 1
        }
      ];

      for (const state of nextStates) {
        // Check if the state violates the rule: starting bucket empty and other bucket full
        const isInvalid = this.startBucket === 'one' 
          ? (state.a === 0 && state.b === this.bucketTwoCap)
          : (state.b === 0 && state.a === this.bucketOneCap);

        if (!isInvalid) {
          const key = `${state.a},${state.b}`;
          if (!visited.has(key)) {
            visited.add(key);
            queue.push(state);
          }
        }
      }
    }

    throw new Error('No solution exists');
  }
}
