def _calculate_group_price(group_size):
    """Calculate price in cents for a group of unique books with appropriate discount."""
    discounts = {1: 0, 2: 0.05, 3: 0.10, 4: 0.20, 5: 0.25}
    return int(group_size * 800 * (1 - discounts.get(group_size, 0)))

def total(basket):
    """Calculate the total price of a basket of books with maximum discount."""
    if not basket:
        return 0
    
    # Count how many of each book we have
    counts = {}
    for book in basket:
        counts[book] = counts.get(book, 0) + 1
    
    total_price = 0
    while sum(counts.values()) > 0:
        # Find the largest possible group of unique books
        group_size = 0
        for book in counts:
            if counts[book] > 0:
                group_size += 1
                counts[book] -= 1
        
        # Remove books with zero count
        counts = {k: v for k, v in counts.items() if v > 0}
        
        # Calculate price for this group and add to total
        total_price += _calculate_group_price(group_size)
    
    # Check if we can get a better price by splitting a group of 5 into 3+3
    # This is needed for cases like 2,2,2,1,1 where 5+3 is worse than 4+4
    counts = {}
    for book in basket:
        counts[book] = counts.get(book, 0) + 1
    
    alt_price = 0
    while sum(counts.values()) > 0:
        # Try to make groups of 4 first
        group_size = 0
        for book in counts:
            if counts[book] > 0:
                group_size += 1
                counts[book] -= 1
            if group_size == 4:
                break
        
        # If we couldn't make a group of 4, make the largest possible group
        if group_size < 4:
            for book in counts:
                if counts[book] > 0:
                    group_size += 1
                    counts[book] -= 1
        
        # Remove books with zero count
        counts = {k: v for k, v in counts.items() if v > 0}
        
        # Calculate price for this group and add to total
        alt_price += _calculate_group_price(group_size)
    
    # Return the minimum price (maximum discount)
    return min(total_price, alt_price)
