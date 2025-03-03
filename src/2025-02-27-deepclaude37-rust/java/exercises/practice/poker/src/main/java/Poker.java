import java.util.*;
import java.util.stream.Collectors;

class Poker {
    private final List<String> hand;

    Poker(List<String> hand) {
        this.hand = hand;
    }

    List<String> getBestHands() {
        return hand;
    }

}
