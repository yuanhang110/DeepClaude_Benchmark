from collections import deque
from math import gcd

def measure(bucket_one, bucket_two, goal, start_bucket):
    # Check if the goal is impossible
    if goal > bucket_one and goal > bucket_two:
        raise ValueError("Goal is impossible: larger than both buckets.")
    if goal == 0:
        raise ValueError("Goal must be positive.")
    if goal % gcd(bucket_one, bucket_two) != 0:
        raise ValueError("Goal is not achievable with given bucket sizes.")

    # Determine initial state based on starting bucket
    if start_bucket == 'one':
        initial_state = (bucket_one, 0)
        other_bucket = 'two'
    else:
        initial_state = (0, bucket_two)
        other_bucket = 'one'
    steps = 1

    # Check if initial state already meets the goal
    if initial_state[0] == goal:
        return (steps, 'one', initial_state[1])
    if initial_state[1] == goal:
        return (steps, 'two', initial_state[0])

    # BFS initialization
    visited = set()
    queue = deque([(initial_state[0], initial_state[1], steps)])
    visited.add((initial_state[0], initial_state[1]))

    while queue:
        a, b, current_steps = queue.popleft()
        
        # Generate all possible next states
        next_states = [
            (bucket_one, b),  # Fill bucket one
            (a, bucket_two),  # Fill bucket two
            (0, b),           # Empty bucket one
            (a, 0),           # Empty bucket two
            (a - min(a, bucket_two - b), b + min(a, bucket_two - b)),  # Pour a to b
            (a + min(b, bucket_one - a), b - min(b, bucket_one - a)),  # Pour b to a
        ]
        
        for new_a, new_b in next_states:
            # Check if the new state is invalid
            if start_bucket == 'one':
                if new_a == 0 and new_b == bucket_two:
                    continue
            else:
                if new_b == 0 and new_a == bucket_one:
                    continue
            
            # Check if the new state is the goal
            if new_a == goal:
                return (current_steps + 1, 'one', new_b)
            if new_b == goal:
                return (current_steps + 1, 'two', new_a)
            
            # Add to queue if not visited
            if (new_a, new_b) not in visited:
                visited.add((new_a, new_b))
                queue.append((new_a, new_b, current_steps + 1))
    
    # If no solution found
    raise ValueError("No solution possible")
