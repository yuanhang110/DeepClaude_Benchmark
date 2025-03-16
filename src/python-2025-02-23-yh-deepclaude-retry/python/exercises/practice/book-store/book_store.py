def total(basket):
    if not basket:
        return 0
        
    # Count occurrences of each book
    book_counts = [0] * 5
    for book in basket:
        book_counts[book - 1] += 1
    
    def calculate_groups(remaining_books, memo=None):
        if memo is None:
            memo = {}
            
        # Convert remaining books to tuple for memoization
        books_key = tuple(remaining_books)
        if books_key in memo:
            return memo[books_key]
            
        if not any(remaining_books):
            return 0
            
        min_price = float('inf')
        
        # Try all possible group sizes
        for size in range(5, 0, -1):
            # Check if we can make a group of this size
            temp_books = remaining_books.copy()
            group_possible = True
            group_count = 0
            
            # Count how many different books we have
            for i in range(5):
                if temp_books[i] > 0:
                    group_count += 1
                    temp_books[i] -= 1
                    
            if group_count >= size:
                # Calculate price for this grouping
                discount = {1: 0, 2: 0.05, 3: 0.10, 4: 0.20, 5: 0.25}[size]
                price = int(size * 800 * (1 - discount))
                
                # Add price of remaining books
                remaining_price = calculate_groups(temp_books, memo)
                total_price = price + remaining_price
                
                min_price = min(min_price, total_price)
        
        # If we couldn't make any groups, charge full price for one book
        if min_price == float('inf'):
            min_price = 800
            remaining_books[remaining_books.index(max(remaining_books))] -= 1
            min_price += calculate_groups(remaining_books, memo)
            
        memo[books_key] = min_price
        return min_price
        
    return calculate_groups(book_counts)
