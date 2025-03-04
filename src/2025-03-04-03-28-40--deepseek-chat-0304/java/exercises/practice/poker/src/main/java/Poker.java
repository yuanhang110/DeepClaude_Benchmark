import java.util.*;
import java.util.stream.Collectors;

class Poker {
    private final List<String> hand;
    private final int[] ranks;
    private final boolean flush;
    private final boolean straight;
    private final int[] rankCounts;

    Poker(List<String> hand) {
        if (hand == null || hand.size() != 5) {
            throw new IllegalArgumentException("A poker hand must contain exactly 5 cards");
        }
        this.hand = new ArrayList<>(hand);
        try {
            this.ranks = parseRanks();
            this.flush = isFlush();
            this.straight = isStraight();
            this.rankCounts = countRanks();
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid poker hand: " + hand, e);
        }
    }

    List<String> getBestHands() {
        return Collections.singletonList(String.join(" ", hand));
    }

    private int[] parseRanks() {
        int[] ranks = new int[hand.size()];
        for (int i = 0; i < hand.size(); i++) {
            String card = hand.get(i);
            if (card == null || card.length() < 2) {
                throw new IllegalArgumentException("Invalid card: " + card);
            }
            String rank = card.substring(0, card.length() - 1);
            try {
                switch (rank) {
                    case "J": ranks[i] = 11; break;
                    case "Q": ranks[i] = 12; break;
                    case "K": ranks[i] = 13; break;
                    case "A": ranks[i] = 14; break;
                    default: 
                        int value = Integer.parseInt(rank);
                        if (value < 2 || value > 10) {
                            throw new IllegalArgumentException("Invalid card rank: " + rank);
                        }
                        ranks[i] = value;
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid card rank: " + rank, e);
            }
        }
        Arrays.sort(ranks);
        return ranks;
    }

    private boolean isFlush() {
        char suit = hand.get(0).charAt(hand.get(0).length() - 1);
        return hand.stream().allMatch(card -> card.charAt(card.length() - 1) == suit);
    }

    private boolean isStraight() {
        // Check for low straight (A-2-3-4-5)
        if (Arrays.equals(ranks, new int[]{2, 3, 4, 5, 14})) {
            return true;
        }
        
        // Check normal straight
        for (int i = 1; i < ranks.length; i++) {
            if (ranks[i] != ranks[i - 1] + 1) {
                return false;
            }
        }
        return true;
    }

    private int[] countRanks() {
        int[] counts = new int[15]; // Index 0 unused, 1-14 for ranks
        for (int rank : ranks) {
            counts[rank]++;
        }
        return counts;
    }

    private int handStrength() {
        if (straight && flush && ranks[4] == 14) return 9; // Royal flush
        if (straight && flush) return 8; // Straight flush
        if (Arrays.stream(rankCounts).anyMatch(count -> count == 4)) return 7; // Four of a kind
        if (Arrays.stream(rankCounts).anyMatch(count -> count == 3) && 
            Arrays.stream(rankCounts).anyMatch(count -> count == 2)) return 6; // Full house
        if (flush) return 5; // Flush
        if (straight) return 4; // Straight
        if (Arrays.stream(rankCounts).anyMatch(count -> count == 3)) return 3; // Three of a kind
        if (Arrays.stream(rankCounts).filter(count -> count == 2).count() == 2) return 2; // Two pair
        if (Arrays.stream(rankCounts).anyMatch(count -> count == 2)) return 1; // One pair
        return 0; // High card
    }

    // Compare this hand with another hand
    private int compareTo(Poker other) {
        int strengthComparison = Integer.compare(this.handStrength(), other.handStrength());
        if (strengthComparison != 0) return strengthComparison;

        // For hands of same strength, compare individual cards
        for (int i = ranks.length - 1; i >= 0; i--) {
            int cardComparison = Integer.compare(this.ranks[i], other.ranks[i]);
            if (cardComparison != 0) return cardComparison;
        }
        return 0;
    }

    public static List<String> getBestHands(List<List<String>> hands) {
        if (hands == null || hands.isEmpty()) {
            throw new IllegalArgumentException("Hands list cannot be null or empty");
        }
        
        try {
            List<Poker> pokerHands = hands.stream()
                .map(Poker::new)
                .collect(Collectors.toList());

            Poker maxHand = pokerHands.get(0);
            List<Poker> bestHands = new ArrayList<>();
            bestHands.add(maxHand);

            for (int i = 1; i < pokerHands.size(); i++) {
                Poker current = pokerHands.get(i);
                int comparison = current.compareTo(maxHand);
                if (comparison > 0) {
                    maxHand = current;
                    bestHands.clear();
                    bestHands.add(maxHand);
                } else if (comparison == 0) {
                    bestHands.add(current);
                }
            }

            return bestHands.stream()
                .map(p -> String.join(" ", p.hand))
                .collect(Collectors.toList());
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid poker hands input", e);
        }
    }
}
