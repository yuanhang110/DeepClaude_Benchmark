import io.reactivex.Observable;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Hangman {

    Observable<Output> play(Observable<String> words, Observable<String> letters) {
        if (words == null || letters == null) {
            return Observable.just(Output.empty());
        }
        
        return words
            .switchMap(word -> {
                if (word == null || word.isEmpty()) {
                    return Observable.just(Output.empty());
                }
                
                Set<String> guesses = new HashSet<>();
                Set<String> misses = new HashSet<>();
                
                return letters
                    .filter(letter -> letter != null && letter.length() == 1)
                    .map(String::toLowerCase)
                    .map(letter -> {
                        if (word.toLowerCase().contains(letter)) {
                            guesses.add(letter);
                        } else {
                            misses.add(letter);
                        }
                    
                    String discovered = IntStream.range(0, word.length())
                        .mapToObj(i -> guesses.contains(String.valueOf(word.charAt(i))) 
                            ? String.valueOf(word.charAt(i)) 
                            : "_")
                        .collect(Collectors.joining());
                        
                    Status status = determineStatus(word, guesses, misses);
                    
                    return new Output(
                        word,
                        discovered,
                        guesses,
                        misses,
                        status.getParts(),
                        status
                    );
                })
                .startWith(Output.empty());
        });
    }
    
    private Status determineStatus(String word, Set<String> guesses, Set<String> misses) {
        if (word.chars().allMatch(c -> guesses.contains(String.valueOf((char)c)))) {
            return Status.WIN;
        } else if (misses.size() >= 6) {
            return Status.LOSE;
        } else {
            return Status.PLAYING;
        }
    }
}
