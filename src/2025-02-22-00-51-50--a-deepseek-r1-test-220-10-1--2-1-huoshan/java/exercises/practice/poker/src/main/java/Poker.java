import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;
import java.util.stream.Collectors;

class Poker {
    private final List<String> hands;
    
    private enum HandType {
        STRAIGHT_FLUSH, FOUR_OF_A_KIND, FULL_HOUSE, FLUSH, STRAIGHT, 
        THREE_OF_A_KIND, TWO_PAIR, ONE_PAIR, HIGH_CARD
    }

    private static class HandRank {
        final HandType type;
        final List<Integer> key;
        
        HandRank(HandType type, List<Integer> key) {
            this.type = type;
            this.key = key;
        }
    }

    private static class Card {
        final int rankValue;
        final char suit;

        Card(int rankValue, char suit) {
            this.rankValue = rankValue;
            this.suit = suit;
        }
    }

    Poker(List<String> hand) {
        this.hands = new ArrayList<>(hand);
    }

    List<String> getBestHands() {
        List<HandRank> handRanks = new ArrayList<>();
        for (String hand : hands) {
            List<Card> cards = parseHand(hand);
            handRanks.add(evaluateHand(cards));
        }

        int maxRank = handRanks.stream()
            .mapToInt(r -> r.type.ordinal())
            .min()
            .orElse(-1);

        List<HandRank> maxHandRanks = handRanks.stream()
            .filter(r -> r.type.ordinal() == maxRank)
            .collect(Collectors.toList());

        List<HandRank> bestRanks = compareSameRank(maxHandRanks);

        List<String> bestHands = new ArrayList<>();
        for (int i = 0; i < handRanks.size(); i++) {
            if (bestRanks.contains(handRanks.get(i))) {
                bestHands.add(hands.get(i));
            }
        }
        return bestHands;
    }

    private List<Card> parseHand(String hand) {
        List<Card> cards = new ArrayList<>();
        for (String cardStr : hand.split(" ")) {
            String rank = cardStr.substring(0, cardStr.length() - 1);
            char suit = cardStr.charAt(cardStr.length() - 1);
            int value;
            switch (rank) {
                case "J": value = 11; break;
                case "Q": value = 12; break;
                case "K": value = 13; break;
                case "A": value = 14; break;
                default: value = Integer.parseInt(rank);
            }
            cards.add(new Card(value, suit));
        }
        Collections.sort(cards, (a, b) -> b.rankValue - a.rankValue);
        return cards;
    }

    private HandRank evaluateHand(List<Card> cards) {
        boolean isFlush = cards.stream().map(c -> c.suit).distinct().count() == 1;
        boolean isStraight = true;
        
        // Check normal straight
        for (int i = 0; i < cards.size() - 1; i++) {
            if (cards.get(i).rankValue - cards.get(i + 1).rankValue != 1) {
                isStraight = false;
                break;
            }
        }
        
        // Check low Ace straight (A-2-3-4-5)
        if (!isStraight && cards.get(0).rankValue == 14 
                && cards.get(1).rankValue == 5 
                && cards.get(2).rankValue == 4 
                && cards.get(3).rankValue == 3 
                && cards.get(4).rankValue == 2) {
            isStraight = true;
            Collections.rotate(cards, 1); // Rotate to make 5 the first card
        }

        Map<Integer, Integer> rankCount = new HashMap<>();
        for (Card c : cards) {
            rankCount.put(c.rankValue, rankCount.getOrDefault(c.rankValue, 0) + 1);
        }

        List<Integer> pairs = new ArrayList<>();
        int threeOfAKind = 0;
        int fourOfAKind = 0;
        for (Map.Entry<Integer, Integer> entry : rankCount.entrySet()) {
            if (entry.getValue() == 4) {
                fourOfAKind = entry.getKey();
            } else if (entry.getValue() == 3) {
                threeOfAKind = entry.getKey();
            } else if (entry.getValue() == 2) {
                pairs.add(entry.getKey());
            }
        }

        if (isStraight && isFlush) {
            return new HandRank(HandType.STRAIGHT_FLUSH, getHighCards(cards, 5));
        } else if (fourOfAKind > 0) {
            List<Integer> key = new ArrayList<>();
            key.add(fourOfAKind);
            key.add(cards.stream().filter(c -> c.rankValue != fourOfAKind).findFirst().get().rankValue);
            return new HandRank(HandType.FOUR_OF_A_KIND, key);
        } else if (threeOfAKind > 0 && !pairs.isEmpty()) {
            List<Integer> key = new ArrayList<>();
            key.add(threeOfAKind);
            key.add(pairs.get(0));
            return new HandRank(HandType.FULL_HOUSE, key);
        } else if (isFlush) {
            return new HandRank(HandType.FLUSH, getHighCards(cards, 5));
        } else if (isStraight) {
            return new HandRank(HandType.STRAIGHT, getHighCards(cards, 5));
        } else if (threeOfAKind > 0) {
            List<Integer> key = new ArrayList<>();
            key.add(threeOfAKind);
            key.addAll(getHighCards(cards, 2, threeOfAKind));
            return new HandRank(HandType.THREE_OF_A_KIND, key);
        } else if (pairs.size() == 2) {
            List<Integer> key = new ArrayList<>();
            key.add(Math.max(pairs.get(0), pairs.get(1)));
            key.add(Math.min(pairs.get(0), pairs.get(1)));
            key.addAll(getHighCards(cards, 1, pairs.get(0), pairs.get(1)));
            return new HandRank(HandType.TWO_PAIR, key);
        } else if (pairs.size() == 1) {
            List<Integer> key = new ArrayList<>();
            key.add(pairs.get(0));
            key.addAll(getHighCards(cards, 3, pairs.get(0)));
            return new HandRank(HandType.ONE_PAIR, key);
        } else {
            return new HandRank(HandType.HIGH_CARD, getHighCards(cards, 5));
        }
    }

    private List<Integer> getHighCards(List<Card> cards, int count) {
        return cards.stream()
            .map(c -> c.rankValue)
            .limit(count)
            .collect(Collectors.toList());
    }

    private List<Integer> getHighCards(List<Card> cards, int count, Integer... exclude) {
        List<Integer> excluded = Arrays.asList(exclude);
        return cards.stream()
            .map(c -> c.rankValue)
            .filter(r -> !excluded.contains(r))
            .sorted(Collections.reverseOrder())
            .limit(count)
            .collect(Collectors.toList());
    }

    private List<HandRank> compareSameRank(List<HandRank> ranks) {
        List<HandRank> best = new ArrayList<>();
        for (HandRank rank : ranks) {
            if (best.isEmpty()) {
                best.add(rank);
                continue;
            }
            int cmp = compareKeys(rank.key, best.get(0).key);
            if (cmp > 0) {
                best.clear();
                best.add(rank);
            } else if (cmp == 0) {
                best.add(rank);
            }
        }
        return best;
    }

    private int compareKeys(List<Integer> key1, List<Integer> key2) {
        for (int i = 0; i < key1.size(); i++) {
            int cmp = Integer.compare(key1.get(i), key2.get(i));
            if (cmp != 0) {
                return cmp;
            }
        }
        return 0;
    }
}
