import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

class Poker {
    private List<String> hand;
    
    Poker(List<String> hand) {
        this.hand = new ArrayList<>(hand);
    }

    List<String> getBestHands() {
        if (hand == null || hand.isEmpty()) {
            return Collections.emptyList();
        }
        
        List<Hand> hands = new ArrayList<>();
        for (String card : hand) {
            hands.add(new Hand(Arrays.asList(card)));
        }
        
        Collections.sort(hands);
        int maxRank = hands.get(hands.size() - 1).getRank();
        
        List<String> bestHands = new ArrayList<>();
        for (Hand h : hands) {
            if (h.getRank() == maxRank) {
                bestHands.addAll(h.getCards());
            }
        }
        
        return bestHands;
    }
    
    private static class Hand implements Comparable<Hand> {
        private List<String> cards;
        private int rank;
        
        Hand(List<String> cards) {
            this.cards = cards;
            this.rank = calculateRank();
        }
        
        List<String> getCards() {
            return cards;
        }
        
        int getRank() {
            return rank;
        }
        
        private int calculateRank() {
            // Basic ranking - can be expanded for full poker hand evaluation
            if (cards.isEmpty()) return 0;
            
            String card = cards.get(0);
            char value = card.charAt(0);
            
            // Convert face cards to numeric values
            switch(value) {
                case 'A': return 14;
                case 'K': return 13;
                case 'Q': return 12;
                case 'J': return 11;
                case '1': return 10; // Handle '10'
                default: return Character.getNumericValue(value);
            }
        }
        
        @Override
        public int compareTo(Hand other) {
            return Integer.compare(this.rank, other.rank);
        }
    }
}
