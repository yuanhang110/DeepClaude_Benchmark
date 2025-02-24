from collections import deque

def measure(bucket_one, bucket_two, goal, start_bucket):
    # Validate input parameters
    if bucket_one <= 0 or bucket_two <= 0:
        raise ValueError("Buckets must have positive capacity")
    if start_bucket not in ('one', 'two'):
        raise ValueError("start_bucket must be 'one' or 'two'")
    if goal < 0:
        raise ValueError("Goal must be non-negative")
    if goal > max(bucket_one, bucket_two):
        raise ValueError("Goal is impossible")

    # Determine capacities based on starting bucket
    start_cap = bucket_one if start_bucket == 'one' else bucket_two
    other_cap = bucket_two if start_bucket == 'one' else bucket_one

    # Check if goal exceeds both buckets' capacities
    if goal > max(start_cap, other_cap):
        raise ValueError("Goal exceeds capacity of both buckets")

    # Initialize BFS
    visited = set()
    queue = deque()
    
    # Create initial state based on starting bucket
    if start_bucket == 'one':
        initial_state = (bucket_one, 0)
    else:
        initial_state = (0, bucket_two)
    
    queue.append((initial_state[0], initial_state[1], 1))
    visited.add((initial_state[0], initial_state[1]))

    while queue:
        b1, b2, steps = queue.popleft()

        # Check if we've reached the goal
        if b1 == goal:
            return (steps, 'one', b2)
        if b2 == goal:
            return (steps, 'two', b1)

        # Generate all possible next states
        next_states = []

        # Action 1: Fill start bucket
        if start_bucket == 'one' and b1 < bucket_one:
            next_states.append((bucket_one, b2))
        elif start_bucket == 'two' and b2 < bucket_two:
            next_states.append((b1, bucket_two))

        # Action 2: Fill other bucket
        if start_bucket == 'one' and b2 < bucket_two:
            next_states.append((b1, bucket_two))
        elif start_bucket == 'two' and b1 < bucket_one:
            next_states.append((bucket_one, b2))

        # Action 3: Empty start bucket
        if start_bucket == 'one' and b1 > 0:
            next_states.append((0, b2))
        elif start_bucket == 'two' and b2 > 0:
            next_states.append((b1, 0))

        # Action 4: Empty other bucket
        if start_bucket == 'one' and b2 > 0:
            next_states.append((b1, 0))
        elif start_bucket == 'two' and b1 > 0:
            next_states.append((0, b2))

        # Action 5: Pour from start to other
        if start_bucket == 'one':
            pour = min(b1, bucket_two - b2)
            next_states.append((b1 - pour, b2 + pour))
        else:
            pour = min(b2, bucket_one - b1)
            next_states.append((b1 + pour, b2 - pour))

        # Action 6: Pour from other to start
        if start_bucket == 'one':
            pour = min(b2, bucket_one - b1)
            next_states.append((b1 + pour, b2 - pour))
        else:
            pour = min(b1, bucket_two - b2)
            next_states.append((b1 - pour, b2 + pour))

        # Process generated states
        for new_b1, new_b2 in next_states:
            # Check forbidden state rule
            if start_bucket == 'one':
                if new_b1 == 0 and new_b2 == bucket_two:
                    continue
            else:
                if new_b2 == 0 and new_b1 == bucket_one:
                    continue

            if (new_b1, new_b2) not in visited:
                visited.add((new_b1, new_b2))
                queue.append((new_b1, new_b2, steps + 1))

    # If we exhausted all possibilities
    raise ValueError("No solution exists")
