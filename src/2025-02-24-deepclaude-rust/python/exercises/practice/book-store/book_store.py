from functools import lru_cache
from collections import Counter

def total(basket):
    counts = sorted(Counter(basket).values(), reverse=True)
    counts += [0] * (5 - len(counts))
    return calculate_min_cost(tuple(counts))

@lru_cache(maxsize=None)
def calculate_min_cost(counts):
    if sum(counts) == 0:
        return 0
    min_cost = float('inf')
    for k in range(5, 0, -1):
        if k > len(counts) or counts[k-1] == 0:
            continue
        new_counts = list(counts)
        for i in range(k):
            new_counts[i] -= 1
        new_counts.sort(reverse=True)
        cost = calculate_min_cost(tuple(new_counts)) + (k * 800 * (100 - [0, 5, 10, 20, 25][k-1])) // 100
        if cost < min_cost:
            min_cost = cost
    return min_cost
