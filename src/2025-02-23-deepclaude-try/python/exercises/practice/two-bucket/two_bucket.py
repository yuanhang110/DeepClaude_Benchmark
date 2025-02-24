def measure(bucket_one, bucket_two, goal, start_bucket):
    # Input validation
    if bucket_one <= 0 or bucket_two <= 0:
        raise ValueError("Buckets must have positive capacities")
    if goal <= 0:
        raise ValueError("Goal must be a positive number")
    if start_bucket not in ('one', 'two'):
        raise ValueError("Start bucket must be 'one' or 'two'")
    if goal > max(bucket_one, bucket_two):
        raise ValueError("Goal cannot be larger than the largest bucket")

    # Check if goal is impossible based on GCD
    def gcd(a, b):
        while b:
            a, b = b, a % b
        return a

    if goal % gcd(bucket_one, bucket_two) != 0:
        raise ValueError("No solution possible")

    # Initialize starting state based on which bucket to fill first
    initial_state = (bucket_one, 0) if start_bucket == 'one' else (0, bucket_two)
    
    # Track visited states and moves
    visited = {initial_state}
    queue = [(initial_state, 1)]  # (state, moves)
    
    while queue:
        current_state, moves = queue.pop(0)
        current_one, current_two = current_state
        
        # Check if current state has the goal
        if current_one == goal:
            return (moves, 'one', current_two)
        if current_two == goal:
            return (moves, 'two', current_one)
            
        # Generate next possible states
        next_states = []
        
        # Fill operations
        if current_one < bucket_one:
            next_states.append((bucket_one, current_two))
        if current_two < bucket_two:
            next_states.append((current_one, bucket_two))
            
        # Empty operations
        if current_one > 0:
            next_states.append((0, current_two))
        if current_two > 0:
            next_states.append((current_one, 0))
            
        # Pour operations
        if current_one > 0 and current_two < bucket_two:
            pour = min(current_one, bucket_two - current_two)
            next_states.append((current_one - pour, current_two + pour))
        if current_two > 0 and current_one < bucket_one:
            pour = min(current_two, bucket_one - current_one)
            next_states.append((current_one + pour, current_two - pour))
            
        # Process next states
        for next_state in next_states:
            # Skip invalid states where starting bucket is empty and other is full
            if (start_bucket == 'one' and next_state[0] == 0 and next_state[1] == bucket_two) or \
               (start_bucket == 'two' and next_state[1] == 0 and next_state[0] == bucket_one):
                continue
                
            if next_state not in visited:
                visited.add(next_state)
                queue.append((next_state, moves + 1))
    
    raise ValueError("No solution possible")
