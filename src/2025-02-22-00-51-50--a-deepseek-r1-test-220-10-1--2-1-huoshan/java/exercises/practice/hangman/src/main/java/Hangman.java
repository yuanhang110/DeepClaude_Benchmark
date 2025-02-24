import io.reactivex.Observable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;

class Hangman {

    Observable<Output> play(Observable<String> words, Observable<String> letters) {
        return words.switchMap(word -> 
            letters
            .filter(letter -> letter.length() == 1) // Only allow single-character guesses
            .scan(
                createInitialState(word),
                (state, letter) -> processGuess(letter, state)
            )
        );
    }

    private Output createInitialState(String word) {
        return new Output(
            word,
            new String(new char[word.length()]).replace('\0', '_'), // Create underscores
            Collections.emptySet(),
            Collections.emptySet(),
            Collections.emptyList(),
            Status.ONGOING
        );
    }

    private Output processGuess(String letter, Output current) {
        if (current.status != Status.ONGOING) {
            return current;  // Game already over
        }

        // Check if letter was already guessed
        if (current.guess.contains(letter) || current.misses.contains(letter)) {
            return current;
        }

        // Create new guess/miss sets
        Set<String> newGuess = new HashSet<>(current.guess);
        Set<String> newMisses = new HashSet<>(current.misses);
        
        if (current.secret.contains(letter)) {
            newGuess.add(letter);
        } else {
            newMisses.add(letter);
        }

        // Calculate new discovered word
        String discovered = calculateDiscovered(current.secret, newGuess);
        
        // Determine status
        Status status = Status.ONGOING;
        if (discovered.equals(current.secret)) {
            status = Status.WON;
        } else if (newMisses.size() >= 6) {  // 6 body parts
            status = Status.LOST;
        }

        return new Output(
            current.secret,
            discovered,
            newGuess,
            newMisses,
            calculateParts(newMisses.size()),
            status
        );
    }

    private String calculateDiscovered(String secret, Set<String> guesses) {
        StringBuilder sb = new StringBuilder();
        for (char c : secret.toCharArray()) {
            sb.append(guesses.contains(String.valueOf(c)) ? c : '_');
        }
        return sb.toString();
    }

    private List<Part> calculateParts(int missCount) {
        List<Part> parts = new ArrayList<>();
        // Add parts based on number of misses
        for (int i = 0; i < missCount && i < 6; i++) {
            parts.add(Part.values()[i]);
        }
        return Collections.unmodifiableList(parts);
    }
}
