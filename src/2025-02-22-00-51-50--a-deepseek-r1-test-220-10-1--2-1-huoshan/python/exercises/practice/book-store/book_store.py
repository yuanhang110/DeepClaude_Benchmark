from functools import lru_cache
from collections import Counter

def total(basket):
    counts = sorted(Counter(basket).values(), reverse=True)
    
    @lru_cache(maxsize=None)
    def calculate_cost(sorted_counts):
        if not sorted_counts:
            return 0
        
        min_cost = 10**18
        max_group_size = len(sorted_counts)
        
        for group_size in range(max_group_size, 0, -1):
            new_counts = list(sorted_counts)
            # Subtract 1 from the first 'group_size' counts
            for i in range(group_size):
                new_counts[i] -= 1
            
            # Filter out zero counts and sort again
            filtered = sorted([c for c in new_counts if c > 0], reverse=True)
            group_price = int(group_size * 800 * (1 - get_discount(group_size)))
            current_cost = group_price + calculate_cost(tuple(filtered))
            
            if current_cost < min_cost:
                min_cost = current_cost
        
        return min_cost
    
    return int(calculate_cost(tuple(counts)))

def get_discount(group_size):
    if group_size == 5:
        return 0.25
    elif group_size == 4:
        return 0.20
    elif group_size == 3:
        return 0.10
    elif group_size == 2:
        return 0.05
    else:
        return 0.0
