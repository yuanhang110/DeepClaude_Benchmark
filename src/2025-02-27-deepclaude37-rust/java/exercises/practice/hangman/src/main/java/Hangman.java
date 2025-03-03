import io.reactivex.Observable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

class Hangman {

    Observable<Output> play(Observable<String> words, Observable<String> letters) {
        return words.switchMap(word ->
            letters.scan(
                initialState(word),
                (current, letter) -> computeNextState(current, letter, word)
            )
        );
    }

    private Output initialState(String word) {
        String discovered = word.replaceAll(".", "_");
        return new Output(
            word,
            discovered,
            Collections.emptySet(),
            Collections.emptySet(),
            Collections.emptyList(),
            Status.PLAYING
        );
    }

    private Output computeNextState(Output current, String letter, String secret) {
        if (current.status != Status.PLAYING) {
            return current;
        }

        String lowerLetter = letter.toLowerCase();
        if (current.guess.contains(lowerLetter) || current.misses.contains(lowerLetter)) {
            return current;
        }

        Set<String> newGuess = new HashSet<>(current.guess);
        Set<String> newMisses = new HashSet<>(current.misses);
        boolean isHit = secret.toLowerCase().contains(lowerLetter);

        if (isHit) {
            newGuess.add(lowerLetter);
        } else {
            newMisses.add(lowerLetter);
        }

        char[] discoveredChars = current.discovered.toCharArray();
        char lowerChar = lowerLetter.charAt(0);
        for (int i = 0; i < secret.length(); i++) {
            if (Character.toLowerCase(secret.charAt(i)) == lowerChar) {
                discoveredChars[i] = secret.charAt(i);
            }
        }
        String newDiscovered = new String(discoveredChars);

        int missCount = newMisses.size();
        int maxParts = Part.values().length;
        List<Part> newParts = List.of(Part.values()).subList(0, Math.min(missCount, maxParts));

        Status newStatus = newDiscovered.equals(secret) ? Status.WON :
                          missCount >= maxParts ? Status.LOST :
                          Status.PLAYING;

        return new Output(
            secret,
            newDiscovered,
            newGuess,
            newMisses,
            newParts,
            newStatus
        );
    }
}
