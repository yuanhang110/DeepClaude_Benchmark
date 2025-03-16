from collections import deque
from math import gcd

def measure(bucket_one, bucket_two, goal, start_bucket):
    """
    Determine the minimum number of actions needed to measure exactly 'goal' liters
    using two buckets of different sizes.
    
    Args:
        bucket_one: Size of first bucket
        bucket_two: Size of second bucket
        goal: Target volume to measure
        start_bucket: Which bucket to fill first ('one' or 'two')
        
    Returns:
        A tuple containing (number of actions, which bucket has the goal amount, amount in other bucket)
    
    Raises:
        ValueError: If the goal cannot be achieved or is invalid
    """
    # Check if goal is achievable
    if goal > max(bucket_one, bucket_two):
        raise ValueError("Goal cannot be larger than both buckets")
    
    # Check if goal is mathematically possible
    if goal % gcd(bucket_one, bucket_two) != 0:
        raise ValueError("Goal cannot be reached with these bucket sizes")
    
    # Determine which bucket is which based on start_bucket
    if start_bucket == "one":
        sizes = (bucket_one, bucket_two)
        names = ("one", "two")
    else:  # start_bucket == "two"
        sizes = (bucket_two, bucket_one)
        names = ("two", "one")
    
    # Queue for BFS: (steps, amount in bucket 1, amount in bucket 2)
    queue = deque([(1, sizes[0], 0)])  # Start with first bucket filled
    visited = {(sizes[0], 0)}
    
    while queue:
        steps, b1, b2 = queue.popleft()
        
        # Check if we've reached the goal
        if b1 == goal:
            return (steps, names[0], b2)
        if b2 == goal:
            return (steps, names[1], b1)
        
        # Try all possible actions
        next_states = []
        
        # Empty buckets
        next_states.append((0, b2))  # Empty first bucket
        next_states.append((b1, 0))  # Empty second bucket
        
        # Fill buckets
        next_states.append((sizes[0], b2))  # Fill first bucket
        next_states.append((b1, sizes[1]))  # Fill second bucket
        
        # Pour from first to second
        pour = min(b1, sizes[1] - b2)
        next_states.append((b1 - pour, b2 + pour))
        
        # Pour from second to first
        pour = min(b2, sizes[0] - b1)
        next_states.append((b1 + pour, b2 - pour))
        
        # Process all next states
        for new_b1, new_b2 in next_states:
            # Skip invalid state: starting bucket empty and other bucket full
            if (start_bucket == "one" and new_b1 == 0 and new_b2 == bucket_two) or \
               (start_bucket == "two" and new_b2 == 0 and new_b1 == bucket_one):
                continue
                
            if (new_b1, new_b2) not in visited:
                visited.add((new_b1, new_b2))
                queue.append((steps + 1, new_b1, new_b2))
    
    # If we've exhausted all possibilities without finding a solution
    raise ValueError("Goal cannot be reached")
