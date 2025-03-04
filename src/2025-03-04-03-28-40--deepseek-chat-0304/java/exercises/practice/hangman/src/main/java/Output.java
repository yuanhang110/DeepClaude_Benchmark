import java.util.Collections;
import java.util.List;
import java.util.Set;

enum Status {
    PLAYING(List.of()),
    WIN(List.of()),
    LOSE(List.of(Part.HEAD, Part.BODY, Part.LEFT_ARM, Part.RIGHT_ARM, 
                 Part.LEFT_LEG, Part.RIGHT_LEG));

    private final List<Part> parts;

    Status(List<Part> parts) {
        this.parts = parts;
    }

    public List<Part> getParts() {
        return parts;
    }
}

enum Part {
    HEAD, BODY, LEFT_ARM, RIGHT_ARM, LEFT_LEG, RIGHT_LEG
}

class Output {

    public final String secret;
    public final String discovered;
    public final Set<String> guess;
    public final Set<String> misses;
    public final List<Part> parts;
    public final Status status;

    Output(
        final String secret,
        final String discovered,
        final Set<String> guess,
        final Set<String> misses,
        final List<Part> parts,
        final Status status) {
        this.secret = secret != null ? secret : "";
        this.discovered = discovered != null ? discovered : "";
        this.guess = guess != null ? Set.copyOf(guess) : Collections.emptySet();
        this.misses = misses != null ? Set.copyOf(misses) : Collections.emptySet();
        this.parts = parts != null ? List.copyOf(parts) : Collections.emptyList();
        this.status = status != null ? status : Status.PLAYING;
    }

    static Output empty() {
        return new Output(
            null,
            null,
            Collections.emptySet(),
            Collections.emptySet(),
            Collections.emptyList(),
            null);
    }

}
