def measure(bucket_one, bucket_two, goal, start_bucket):
    from collections import deque

    # Check if goal is achievable
    if goal > max(bucket_one, bucket_two):
        raise ValueError("Goal cannot be greater than the capacity of either bucket")

    # Set up initial state based on which bucket to fill first
    if start_bucket == 'one':
        initial_state = (bucket_one, 0)
        bucket_sizes = (bucket_one, bucket_two)
    else:  # start_bucket == 'two'
        initial_state = (0, bucket_two)
        bucket_sizes = (bucket_one, bucket_two)

    # Check if initial state already meets the goal
    if initial_state[0] == goal:
        return (1, 'one', initial_state[1])
    if initial_state[1] == goal:
        return (1, 'two', initial_state[0])

    # Set up BFS
    queue = deque([(initial_state, 1)])  # (state, steps)
    visited = {initial_state}

    while queue:
        state, steps = queue.popleft()
        bucket_1, bucket_2 = state

        # Try all possible actions
        next_states = [
            (bucket_one, bucket_2),  # Fill bucket 1
            (bucket_1, bucket_two),  # Fill bucket 2
            (0, bucket_2),          # Empty bucket 1
            (bucket_1, 0),          # Empty bucket 2
        ]

        # Pour from bucket 1 to bucket 2
        amount = min(bucket_1, bucket_two - bucket_2)
        next_states.append((bucket_1 - amount, bucket_2 + amount))

        # Pour from bucket 2 to bucket 1
        amount = min(bucket_2, bucket_one - bucket_1)
        next_states.append((bucket_1 + amount, bucket_2 - amount))

        # Process each new state
        for new_state in next_states:
            new_bucket_1, new_bucket_2 = new_state

            # Check the rule: After an action, starting bucket can't be empty while the other is full
            if start_bucket == 'one' and new_bucket_1 == 0 and new_bucket_2 == bucket_two:
                continue
            if start_bucket == 'two' and new_bucket_2 == 0 and new_bucket_1 == bucket_one:
                continue

            # Skip if we've already visited this state
            if new_state in visited:
                continue

            # Check if we've found the goal
            if new_bucket_1 == goal:
                return (steps + 1, 'one', new_bucket_2)
            if new_bucket_2 == goal:
                return (steps + 1, 'two', new_bucket_1)

            # Add to visited and queue
            visited.add(new_state)
            queue.append((new_state, steps + 1))

    # If we exhausted all possible states without finding the goal
    raise ValueError("No solution possible with these bucket sizes and goal")
