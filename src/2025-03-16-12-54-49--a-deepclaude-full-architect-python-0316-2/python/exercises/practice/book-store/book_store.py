def total(basket):
    """
    Calculate the price of a basket of books, applying the best possible discount.
    
    Args:
        basket: A list of book numbers (1-5) representing the books in the basket
        
    Returns:
        The total price after applying the optimal discount strategy
    """
    if not basket:
        return 0
        
    # Count the occurrences of each book
    book_counts = {}
    for book in basket:
        book_counts[book] = book_counts.get(book, 0) + 1
    
    # Get the count of each unique book
    counts = sorted(book_counts.values(), reverse=True)
    
    # Try all possible ways to group the books
    def calculate_price(groups):
        total = 0
        for group_size in groups:
            # Apply the appropriate discount based on group size
            if group_size == 1:
                discount = 0
            elif group_size == 2:
                discount = 5
            elif group_size == 3:
                discount = 10
            elif group_size == 4:
                discount = 20
            elif group_size == 5:
                discount = 25
            else:
                discount = 0
                
            total += group_size * 800 * (100 - discount) // 100
        return total
    
    # Find the optimal grouping
    def find_best_price(counts, current_groups=None):
        if current_groups is None:
            current_groups = []
            
        if all(count == 0 for count in counts):
            return calculate_price(current_groups)
        
        # Find the maximum number of different books we can group
        max_group_size = 0
        for count in counts:
            if count > 0:
                max_group_size += 1
        
        best_price = float('inf')
        
        # Try different group sizes
        for group_size in range(1, max_group_size + 1):
            # Create a new group of this size
            new_counts = list(counts)
            
            # Take one book from each of the top 'group_size' piles
            for i in range(group_size):
                new_counts[i] -= 1
            
            # Resort the counts
            new_counts.sort(reverse=True)
            
            # Recursively find the best price for the remaining books
            price = find_best_price(new_counts, current_groups + [group_size])
            best_price = min(best_price, price)
        
        return best_price
    
    # Special case optimization: Sometimes it's better to make 2 groups of 4 instead of a group of 5 and a group of 3
    def optimize_groups():
        best_price = find_best_price(counts)
        
        # Check if we have enough books to try the 4+4 vs 5+3 optimization
        if len(counts) >= 4 and counts[0] >= 2 and counts[1] >= 2 and counts[2] >= 2 and counts[3] >= 1 and len(book_counts) == 5:
            # Calculate price for 4+4 grouping manually for this specific case
            test_counts = list(counts)
            # Remove books for first group of 4
            for i in range(4):
                test_counts[i] -= 1
            test_counts.sort(reverse=True)
            # Remove books for second group of 4
            for i in range(4):
                test_counts[i] -= 1
            test_counts.sort(reverse=True)
            
            # Calculate price for these two groups of 4
            price_4_4 = 2 * 4 * 800 * 80 // 100  # Two groups of 4 with 20% discount each
            
            # Calculate price for remaining books
            remaining_price = find_best_price(test_counts)
            
            total_price = price_4_4 + remaining_price
            best_price = min(best_price, total_price)
        
        return best_price
    
    return optimize_groups()
