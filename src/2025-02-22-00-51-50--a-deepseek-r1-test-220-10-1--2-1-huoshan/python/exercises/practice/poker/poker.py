def best_hands(hands):
    from collections import Counter

    def score(hand):
        rank_values = {'2':2, '3':3, '4':4, '5':5, '6':6, '7':7, '8':8, '9':9,
                       'T':10, 'J':11, 'Q':12, 'K':13, 'A':14}
        cards = hand.split()
        suits = [card[1] for card in cards]
        ranks = []
        for card in cards:
            if card.startswith('10'):
                rank_char = 'T'
            else:
                rank_char = card[0].upper()
            ranks.append(rank_values[rank_char])
        ranks = sorted(ranks, reverse=True)

        flush = len(set(suits)) == 1
        straight = False
        unique_ranks = list(set(ranks))
        if len(unique_ranks) == 5:
            max_r, min_r = max(ranks), min(ranks)
            if max_r - min_r == 4:
                straight = True
            elif set(ranks) == {14, 2, 3, 4, 5}:
                straight = True
                ranks = [5, 4, 3, 2, 1]

        if straight and flush:
            return (8, ranks[0])

        count = Counter(ranks)
        sorted_counts = sorted(count.items(), key=lambda x: (-x[1], -x[0]))

        if sorted_counts[0][1] == 4:
            return (7, sorted_counts[0][0], sorted_counts[1][0])
        elif sorted_counts[0][1] == 3 and sorted_counts[1][1] == 2:
            return (6, sorted_counts[0][0], sorted_counts[1][0])
        elif flush:
            return (5, ranks)
        elif straight:
            return (4, ranks[0])
        elif sorted_counts[0][1] == 3:
            kickers = sorted([k for k in ranks if k != sorted_counts[0][0]], reverse=True)
            return (3, sorted_counts[0][0], kickers)
        elif sorted_counts[0][1] == 2 and sorted_counts[1][1] == 2:
            pairs = sorted([sorted_counts[0][0], sorted_counts[1][0]], reverse=True)
            kicker = sorted_counts[2][0]
            return (2, pairs[0], pairs[1], kicker)
        elif sorted_counts[0][1] == 2:
            kickers = sorted([k for k in ranks if k != sorted_counts[0][0]], reverse=True)
            return (1, sorted_counts[0][0], kickers)
        else:
            return (0, ranks)

    scored_hands = [(score(hand), hand) for hand in hands]
    max_score = max(scored_hands, key=lambda x: x[0])[0]
    return [hand for s, hand in scored_hands if s == max_score]
