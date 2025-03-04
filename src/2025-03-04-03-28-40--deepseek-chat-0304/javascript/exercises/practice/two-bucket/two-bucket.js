export class TwoBucket {
  constructor(bucket1, bucket2, goal, startBucket) {
    // Validate inputs
    if (bucket1 <= 0 || bucket2 <= 0 || goal <= 0) {
      throw new Error('Buckets and goal must be positive integers');
    }
    
    // Check if goal is larger than both buckets
    if (goal > Math.max(bucket1, bucket2)) {
      throw new Error('Goal cannot be larger than both buckets');
    }
    
    // Check if goal is reachable (must be divisible by GCD of bucket sizes)
    const gcd = (a, b) => b === 0 ? a : gcd(b, a % b);
    if (goal % gcd(bucket1, bucket2) !== 0) {
      throw new Error('Goal is not reachable with given bucket sizes');
    }
    
    this.bucket1 = bucket1;
    this.bucket2 = bucket2;
    this.goal = goal;
    this.startBucket = startBucket;
  }

  solve() {
    const visited = new Set();
    const queue = [];
    
    // Initial state based on start bucket
    const initialState = this.startBucket === 'one' ? 
      { b1: this.bucket1, b2: 0, moves: 1 } : 
      { b1: 0, b2: this.bucket2, moves: 1 };
    
    queue.push(initialState);
    
    while (queue.length > 0) {
      const current = queue.shift();
      
      // Check if we've reached the goal
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
      
      // Generate next possible states
      const nextStates = this.getNextStates(current);
      
      for (const state of nextStates) {
        const stateKey = `${state.b1},${state.b2}`;
        if (!visited.has(stateKey)) {
          visited.add(stateKey);
          queue.push(state);
        }
      }
    }
    
    throw new Error('No solution found');
  }

  getNextStates(current) {
    const { b1, b2, moves } = current;
    const states = [];
    
    // Fill bucket1
    if (b1 < this.bucket1) {
      states.push({ b1: this.bucket1, b2, moves: moves + 1 });
    }
    
    // Fill bucket2
    if (b2 < this.bucket2) {
      states.push({ b1, b2: this.bucket2, moves: moves + 1 });
    }
    
    // Empty bucket1
    if (b1 > 0) {
      states.push({ b1: 0, b2, moves: moves + 1 });
    }
    
    // Empty bucket2
    if (b2 > 0) {
      states.push({ b1, b2: 0, moves: moves + 1 });
    }
    
    // Pour from bucket1 to bucket2
    if (b1 > 0 && b2 < this.bucket2) {
      const pourAmount = Math.min(b1, this.bucket2 - b2);
      states.push({ 
        b1: b1 - pourAmount, 
        b2: b2 + pourAmount, 
        moves: moves + 1 
      });
    }
    
    // Pour from bucket2 to bucket1
    if (b2 > 0 && b1 < this.bucket1) {
      const pourAmount = Math.min(b2, this.bucket1 - b1);
      states.push({ 
        b1: b1 + pourAmount, 
        b2: b2 - pourAmount, 
        moves: moves + 1 
      });
    }
    
    // Filter out invalid states based on the rules
    return states.filter(state => {
      // Rule: After any action, starting bucket can't be empty while other is full
      if (this.startBucket === 'one') {
        return !(state.b1 === 0 && state.b2 === this.bucket2);
      } else {
        return !(state.b2 === 0 && state.b1 === this.bucket1);
      }
    });
  }
}
