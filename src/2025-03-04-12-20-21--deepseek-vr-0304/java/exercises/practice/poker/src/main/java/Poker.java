import java.util.*;
import java.util.stream.Collectors;

class Poker {
    private static final Map<Character, Integer> RANK_VALUES = Map.ofEntries(
        Map.entry('2', 2),
        Map.entry('3', 3),
        Map.entry('4', 4),
        Map.entry('5', 5),
        Map.entry('6', 6),
        Map.entry('7', 7),
        Map.entry('8', 8),
        Map.entry('9', 9),
        Map.entry('1', 10), // '1' represents 10
        Map.entry('J', 11),
        Map.entry('Q', 12),
        Map.entry('K', 13),
        Map.entry('A', 14)
    );

    private final List<String> hand;
    private final List<Integer> values;
    private final List<Character> suits;

    Poker(List<String> hand) {
        this.hand = hand;
        this.values = hand.stream()
            .map(card -> RANK_VALUES.get(card.charAt(0)))
            .sorted(Comparator.reverseOrder())
            .collect(Collectors.toList());
        this.suits = hand.stream()
            .map(card -> card.length() == 2 ? card.charAt(1) : card.charAt(2))
            .collect(Collectors.toList());
    }

    List<String> getBestHands() {
        return Collections.singletonList(hand.get(0));
    }

    private boolean isStraight() {
        Set<Integer> uniqueValues = new HashSet<>(values);
        if (uniqueValues.size() != 5) return false;
        
        int min = Collections.min(values);
        int max = Collections.max(values);
        return max - min == 4;
    }

    private boolean isFlush() {
        Set<Character> uniqueSuits = new HashSet<>(suits);
        return uniqueSuits.size() == 1;
    }

    private boolean isStraightFlush() {
        return isStraight() && isFlush();
    }

    private boolean isFourOfAKind() {
        return getCounts().contains(4);
    }

    private boolean isFullHouse() {
        Map<Integer, Integer> counts = getCounts();
        return counts.containsValue(3) && counts.containsValue(2);
    }

    private boolean isThreeOfAKind() {
        return getCounts().contains(3);
    }

    private boolean isTwoPair() {
        return getCounts().values().stream().filter(count -> count == 2).count() == 2;
    }

    private boolean isOnePair() {
        return getCounts().contains(2);
    }

    private Map<Integer, Integer> getCounts() {
        Map<Integer, Integer> counts = new HashMap<>();
        for (int value : values) {
            counts.put(value, counts.getOrDefault(value, 0) + 1);
        }
        return counts;
    }
}
