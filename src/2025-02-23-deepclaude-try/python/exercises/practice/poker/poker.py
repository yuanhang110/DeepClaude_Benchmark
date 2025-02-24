from collections import Counter

def best_hands(hands):
    def parse_hand(hand):
        value_map = {'2':2, '3':3, '4':4, '5':5, '6':6, '7':7,
                    '8':8, '9':9, 'T':10, 'J':11, 'Q':12, 'K':13, 'A':14}
        cards = hand.split()
        parsed = []
        for card in cards:
            if card.startswith('10'):
                value = 10
                suit = card[2]
            else:
                value = value_map[card[0]]
                suit = card[1]
            parsed.append((value, suit))
        return parsed

    def hand_rank(parsed):
        values = [v for v, s in parsed]
        suits = [s for v, s in parsed]
        value_counts = Counter(values)
        sorted_counts = sorted(value_counts.items(), key=lambda x: (-x[1], -x[0]))
        sorted_values = sorted(values, reverse=True)
        unique_values = sorted(values)

        # Check straight
        def is_straight(vals):
            if len(set(vals)) != 5:
                return (False, None)
            sorted_vals = sorted(vals)
            if sorted_vals[-1] - sorted_vals[0] == 4:
                return (True, sorted_vals[-1])
            if sorted_vals == [2,3,4,5,14]:
                return (True, 5)
            return (False, None)
        
        straight_result = is_straight(values)
        is_straight = straight_result[0]
        high_card = straight_result[1]
        
        # Check flush
        is_flush = len(set(suits)) == 1

        # Straight flush
        if is_straight and is_flush:
            return (8, high_card)
        
        # Four of a kind
        if sorted_counts[0][1] == 4:
            return (7, sorted_counts[0][0], sorted_counts[1][0])
        
        # Full house
        if sorted_counts[0][1] == 3 and sorted_counts[1][1] == 2:
            return (6, sorted_counts[0][0], sorted_counts[1][0])
        
        # Flush
        if is_flush:
            return (5, tuple(sorted_values))
        
        # Straight
        if is_straight:
            return (4, high_card)
        
        # Three of a kind
        if sorted_counts[0][1] == 3:
            others = [v for v in values if v != sorted_counts[0][0]]
            others_sorted = sorted(others, reverse=True)
            return (3, sorted_counts[0][0], others_sorted[0], others_sorted[1])
        
        # Two pairs
        if len(sorted_counts) >= 2 and sorted_counts[0][1] == 2 and sorted_counts[1][1] == 2:
            pair1, pair2 = sorted_counts[0][0], sorted_counts[1][0]
            others = [v for v in values if v not in (pair1, pair2)]
            return (2, max(pair1, pair2), min(pair1, pair2), others[0])
        
        # One pair
        if sorted_counts[0][1] == 2:
            pair_val = sorted_counts[0][0]
            others = sorted([v for v in values if v != pair_val], reverse=True)
            return (1, pair_val, others[0], others[1], others[2])
        
        # High card
        return (0, tuple(sorted_values))

    ranked_hands = []
    for hand in hands:
        parsed = parse_hand(hand)
        rank = hand_rank(parsed)
        ranked_hands.append((rank, hand))
    
    ranked_hands.sort(reverse=True, key=lambda x: x[0])
    max_rank = ranked_hands[0][0]
    best = [hand for rank, hand in ranked_hands if rank == max_rank]
    return best
