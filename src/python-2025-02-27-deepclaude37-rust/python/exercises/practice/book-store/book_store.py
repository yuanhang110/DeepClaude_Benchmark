def total(basket):
    from functools import lru_cache
    
    # Count occurrences of each book (1-5)
    counts = [0] * 5
    for book in basket:
        # Adjust index to 0-based (books are 1-5)
        counts[book-1] += 1
    
    # Sort counts in descending order for optimization
    counts.sort(reverse=True)
    
    @lru_cache(maxsize=None)
    def min_price(counts):
        if sum(counts) == 0:
            return 0
            
        min_cost = float('inf')
        # Find how many different books are available
        max_group_size = sum(1 for c in counts if c > 0)
        
        # Try each possible group size and find the minimum cost
        for size in range(1, max_group_size + 1):
            # Check if we can make a group of this size
            if counts[size-1] == 0:
                break  # Not enough different books
                
            # Create a new counts list with one book removed from each of the first 'size' counts
            new_counts = list(counts)
            for i in range(size):
                new_counts[i] -= 1
            
            # Re-sort the counts for the next recursive call
            new_counts.sort(reverse=True)
            
            # Calculate discount based on group size
            discount = {1: 0, 2: 0.05, 3: 0.10, 4: 0.20, 5: 0.25}.get(size, 0)
            
            # Cost for this group + minimum cost for remaining books
            # Calculate in cents (800 cents = $8)
            group_cost = size * 800 * (1 - discount)
            total_cost = group_cost + min_price(tuple(new_counts))
            
            min_cost = min(min_cost, total_cost)
            
        return min_cost
    
    # Start the calculation with the initial counts as a tuple (for caching)
    return round(min_price(tuple(counts)))
