def total(basket):
    """
    Calculate the price of a basket of books applying optimal discounts.
    
    A single book costs $8.
    Discounts are applied based on the number of different books purchased:
    - 2 different books: 5% discount
    - 3 different books: 10% discount
    - 4 different books: 20% discount
    - 5 different books: 25% discount
    
    Args:
        basket: List of books (represented by integers 1-5)
    
    Returns:
        float: The total price after applying optimal discounts
    """
    if not basket:
        return 0
    
    # Count occurrences of each book
    from collections import Counter
    book_counts = Counter(basket)
    
    # Get sorted counts (highest first)
    counts = sorted(book_counts.values(), reverse=True)
    
    # Form initial groups greedily
    groups = []
    while any(counts):
        # Form a group with as many different books as possible
        group_size = sum(1 for c in counts if c > 0)
        groups.append(group_size)
        
        # Decrement the counts for books used in this group
        for i in range(len(counts)):
            if counts[i] > 0:
                counts[i] -= 1
        
        # Resort to maintain descending order
        counts.sort(reverse=True)
    
    # Count groups by size
    group_counts = {i: groups.count(i) for i in range(1, 6)}
    
    # Optimization: Replace combinations of 5+3 with 4+4 for better discount
    while group_counts.get(5, 0) > 0 and group_counts.get(3, 0) > 0:
        group_counts[5] -= 1
        group_counts[3] -= 1
        group_counts[4] = group_counts.get(4, 0) + 2
    
    # Calculate price with discounts
    discounts = {
        1: 0,      # No discount for 1 book
        2: 0.05,   # 5% discount for 2 different books
        3: 0.10,   # 10% discount for 3 different books
        4: 0.20,   # 20% discount for 4 different books
        5: 0.25,   # 25% discount for 5 different books
    }
    
    total_price = 0
    for group_size, count in group_counts.items():
        discount_rate = discounts[group_size]
        price_per_group = group_size * 800 * (1 - discount_rate)  # 800 cents = $8
        total_price += count * price_per_group
    
    return total_price
