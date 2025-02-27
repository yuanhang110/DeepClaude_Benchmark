import io.reactivex.Observable;
import java.util.*;
import java.util.stream.Collectors;

class Hangman {

    private static final List<Part> PARTS_ORDER = Arrays.asList(
        Part.HEAD, Part.BODY, Part.LEFT_ARM, Part.RIGHT_ARM, Part.LEFT_LEG, Part.RIGHT_LEG
    );
    private static final int MAX_MISSES = PARTS_ORDER.size();

    private Output initialState(String secret) {
        String discovered = secret.chars()
            .mapToObj(c -> "_")
            .collect(Collectors.joining());
        return new Output(
            secret,
            discovered,
            Collections.emptySet(),
            Collections.emptySet(),
            Collections.emptyList(),
            Status.ONGOING
        );
    }

    private Output processLetter(Output current, String letter) {
        if (current.status != Status.ONGOING) {
            return current;
        }

        String l = letter.toLowerCase();
        if (current.guess.contains(l) || current.misses.contains(l)) {
            return current;
        }

        if (current.secret.toLowerCase().contains(l)) {
            Set<String> newGuess = new HashSet<>(current.guess);
            newGuess.add(l);
            String newDiscovered = buildDiscovered(current.secret, newGuess);
            boolean isWon = newDiscovered.equals(current.secret);
            return new Output(
                current.secret,
                newDiscovered,
                Collections.unmodifiableSet(newGuess),
                current.misses,
                current.parts,
                isWon ? Status.WON : Status.ONGOING
            );
        } else {
            Set<String> newMisses = new HashSet<>(current.misses);
            newMisses.add(l);
            List<Part> newParts = new ArrayList<>(current.parts);
            if (newParts.size() < MAX_MISSES) {
                newParts.add(PARTS_ORDER.get(newParts.size()));
            }
            Status newStatus = newParts.size() >= MAX_MISSES ? Status.LOST : Status.ONGOING;
            return new Output(
                current.secret,
                current.discovered,
                current.guess,
                Collections.unmodifiableSet(newMisses),
                Collections.unmodifiableList(newParts),
                newStatus
            );
        }
    }

    private String buildDiscovered(String secret, Set<String> guesses) {
        StringBuilder sb = new StringBuilder();
        for (char c : secret.toCharArray()) {
            String lowerC = String.valueOf(c).toLowerCase();
            sb.append(guesses.contains(lowerC) ? c : '_');
        }
        return sb.toString();
    }

    Observable<Output> play(Observable<String> words, Observable<String> letters) {
        return words.switchMap(word ->
            letters.scan(initialState(word), this::processLetter)
        );
    }
}
