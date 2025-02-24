import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class House {
    private static final String[] PARTS = {
        "the house that Jack built.",
        "the malt\nthat lay in ",
        "the rat\nthat ate ",
        "the cat\nthat killed ",
        "the dog\nthat worried ",
        "the cow with the crumpled horn\nthat tossed ",
        "the maiden all forlorn\nthat milked ",
        "the man all tattered and torn\nthat kissed ",
        "the priest all shaven and shorn\nthat married ",
        "the rooster that crowed in the morn\nthat woke ",
        "the farmer sowing his corn\nthat kept ",
        "the horse and the hound and the horn\nthat belonged to "
    };

    public String verse(int verse) {
        if (verse < 1 || verse > 12) {
            throw new IllegalArgumentException("Verse must be between 1 and 12");
        }
        
        List<String> selectedParts = new ArrayList<>();
        for (int i = 0; i < verse; i++) {
            selectedParts.add(PARTS[i]);
        }
        Collections.reverse(selectedParts);
        
        StringBuilder sb = new StringBuilder("This is ");
        for (int i = 0; i < selectedParts.size(); i++) {
            if (i > 0) {
                sb.append("\n");
            }
            sb.append(selectedParts.get(i));
        }
        return sb.toString();
    }

    public String verses(int startVerse, int endVerse) {
        StringBuilder sb = new StringBuilder();
        for (int v = startVerse; v <= endVerse; v++) {
            if (v > startVerse) {
                sb.append("\n\n");
            }
            sb.append(verse(v));
        }
        return sb.toString();
    }

    public String sing() {
        return verses(1, 12);
    }
}
