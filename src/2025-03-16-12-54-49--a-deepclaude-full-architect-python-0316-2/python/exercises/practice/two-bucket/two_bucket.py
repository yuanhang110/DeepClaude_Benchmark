def measure(bucket_one, bucket_two, goal, start_bucket):
    """
    Determine how many actions are required to measure an exact number of liters
    by strategically transferring fluid between two buckets.
    
    Args:
        bucket_one: Size of the first bucket
        bucket_two: Size of the second bucket
        goal: The desired amount of water to measure
        start_bucket: Which bucket to fill first ("one" or "two")
        
    Returns:
        A tuple containing (number of actions, which bucket contains the goal amount, 
        amount in the other bucket)
    
    Raises:
        ValueError: If the goal is impossible to reach
    """
    # Check if the goal is achievable
    if goal > max(bucket_one, bucket_two):
        raise ValueError("Goal amount cannot be greater than the capacity of either bucket")
    
    # Check if goal is a multiple of the GCD of bucket sizes
    def gcd(a, b):
        while b:
            a, b = b, a % b
        return a
    
    if goal % gcd(bucket_one, bucket_two) != 0:
        raise ValueError("Goal amount is not possible with these bucket sizes")
    
    from collections import deque
    
    # Track visited states to avoid cycles
    visited = set()
    
    # Queue for BFS: (amount in bucket one, amount in bucket two, number of actions)
    queue = deque()
    
    # Initialize based on starting bucket
    if start_bucket == "one":
        queue.append((bucket_one, 0, 1))  # First action is filling bucket one
        visited.add((bucket_one, 0))
    else:  # start_bucket == "two"
        queue.append((0, bucket_two, 1))  # First action is filling bucket two
        visited.add((0, bucket_two))
    
    while queue:
        amount_one, amount_two, actions = queue.popleft()
        
        # Check if we've reached the goal
        if amount_one == goal:
            return (actions, "one", amount_two)
        if amount_two == goal:
            return (actions, "two", amount_one)
        
        # Try all possible actions
        next_states = []
        
        # 1. Fill bucket one
        next_states.append((bucket_one, amount_two))
        
        # 2. Fill bucket two
        next_states.append((amount_one, bucket_two))
        
        # 3. Empty bucket one
        next_states.append((0, amount_two))
        
        # 4. Empty bucket two
        next_states.append((amount_one, 0))
        
        # 5. Pour from bucket one to bucket two
        pour_amount = min(amount_one, bucket_two - amount_two)
        next_states.append((amount_one - pour_amount, amount_two + pour_amount))
        
        # 6. Pour from bucket two to bucket one
        pour_amount = min(amount_two, bucket_one - amount_one)
        next_states.append((amount_one + pour_amount, amount_two - pour_amount))
        
        # Process each possible next state
        for next_one, next_two in next_states:
            # Skip if we've seen this state before
            if (next_one, next_two) in visited:
                continue
                
            # Skip forbidden state: starting bucket empty and other bucket full
            if (start_bucket == "one" and next_one == 0 and next_two == bucket_two) or \
               (start_bucket == "two" and next_two == 0 and next_one == bucket_one):
                continue
                
            # Add valid state to queue and mark as visited
            visited.add((next_one, next_two))
            queue.append((next_one, next_two, actions + 1))
    
    # If we've exhausted all possibilities without finding a solution
    raise ValueError("Goal amount cannot be measured with these bucket sizes")
