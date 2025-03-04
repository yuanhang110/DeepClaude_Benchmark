def measure(bucket_one, bucket_two, goal, start_bucket):
    if bucket_one <= 0 or bucket_two <= 0 or goal <= 0:
        raise ValueError("Bucket sizes and goal must be positive integers")
    
    if goal > max(bucket_one, bucket_two):
        raise ValueError("Goal cannot be larger than the largest bucket")
    
    if start_bucket not in ("one", "two"):
        raise ValueError("start_bucket must be 'one' or 'two'")
    
    # Initialize bucket states
    if start_bucket == "one":
        b1, b2 = bucket_one, 0
    else:
        b1, b2 = 0, bucket_two
    
    actions = 1  # First fill counts as one action
    max_actions = 1000  # Prevent infinite loops
    visited = set()  # Track visited states
    
    while actions <= max_actions:
        # Check if we've reached the goal
        if b1 == goal:
            return (actions, "one", b2)
        if b2 == goal:
            return (actions, "two", b1)
        
        # Check for repeated state to prevent infinite loops
        state = (b1, b2)
        if state in visited:
            raise ValueError("Cannot reach goal - stuck in a loop")
        visited.add(state)
        
        # If starting bucket is empty and other is full, we're stuck
        if (start_bucket == "one" and b1 == 0 and b2 == bucket_two) or \
           (start_bucket == "two" and b2 == 0 and b1 == bucket_one):
            raise ValueError("Cannot reach goal with given constraints")
        
        # If starting bucket is empty, fill it
        if start_bucket == "one" and b1 == 0:
            b1 = bucket_one
        elif start_bucket == "two" and b2 == 0:
            b2 = bucket_two
        # If other bucket is full, empty it
        elif b2 == bucket_two:
            b2 = 0
        elif b1 == bucket_one:
            b1 = 0
        # Otherwise, pour from one to another
        else:
            if start_bucket == "one":
                pour_amount = min(b1, bucket_two - b2)
                b1 -= pour_amount
                b2 += pour_amount
            else:
                pour_amount = min(b2, bucket_one - b1)
                b2 -= pour_amount
                b1 += pour_amount
        
        actions += 1
    
    raise ValueError("Cannot reach goal - maximum actions exceeded")
