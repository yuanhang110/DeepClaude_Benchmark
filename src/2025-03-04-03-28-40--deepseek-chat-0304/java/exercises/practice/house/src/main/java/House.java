public class House {
    private static final String[] RHYME_LINES = {
        "This is the house that Jack built.",
        "This is the malt\nthat lay in the house that Jack built.",
        "This is the rat\nthat ate the malt\nthat lay in the house that Jack built.",
        "This is the cat\nthat killed the rat\nthat ate the malt\nthat lay in the house that Jack built.",
        "This is the dog\nthat worried the cat\nthat killed the rat\nthat ate the malt\nthat lay in the house that Jack built.",
        "This is the cow with the crumpled horn\nthat tossed the dog\nthat worried the cat\nthat killed the rat\nthat ate the malt\nthat lay in the house that Jack built.",
        "This is the maiden all forlorn\nthat milked the cow with the crumpled horn\nthat tossed the dog\nthat worried the cat\nthat killed the rat\nthat ate the malt\nthat lay in the house that Jack built.",
        "This is the man all tattered and torn\nthat kissed the maiden all forlorn\nthat milked the cow with the crumpled horn\nthat tossed the dog\nthat worried the cat\nthat killed the rat\nthat ate the malt\nthat lay in the house that Jack built.",
        "This is the priest all shaven and shorn\nthat married the man all tattered and torn\nthat kissed the maiden all forlorn\nthat milked the cow with the crumpled horn\nthat tossed the dog\nthat worried the cat\nthat killed the rat\nthat ate the malt\nthat lay in the house that Jack built.",
        "This is the rooster that crowed in the morn\nthat woke the priest all shaven and shorn\nthat married the man all tattered and torn\nthat kissed the maiden all forlorn\nthat milked the cow with the crumpled horn\nthat tossed the dog\nthat worried the cat\nthat killed the rat\nthat ate the malt\nthat lay in the house that Jack built.",
        "This is the farmer sowing his corn\nthat kept the rooster that crowed in the morn\nthat woke the priest all shaven and shorn\nthat married the man all tattered and torn\nthat kissed the maiden all forlorn\nthat milked the cow with the crumpled horn\nthat tossed the dog\nthat worried the cat\nthat killed the rat\nthat ate the malt\nthat lay in the house that Jack built.",
        "This is the horse and the hound and the horn\nthat belonged to the farmer sowing his corn\nthat kept the rooster that crowed in the morn\nthat woke the priest all shaven and shorn\nthat married the man all tattered and torn\nthat kissed the maiden all forlorn\nthat milked the cow with the crumpled horn\nthat tossed the dog\nthat worried the cat\nthat killed the rat\nthat ate the malt\nthat lay in the house that Jack built."
    };

    public String verse(int verse) {
        if (verse < 1 || verse > RHYME_LINES.length) {
            throw new IllegalArgumentException("Verse number must be between 1 and " + RHYME_LINES.length);
        }
        return RHYME_LINES[verse - 1];
    }

    public String verses(int startVerse, int endVerse) {
        if (startVerse < 1 || endVerse > RHYME_LINES.length || startVerse > endVerse) {
            throw new IllegalArgumentException("Invalid verse range");
        }
        
        StringBuilder sb = new StringBuilder();
        for (int i = startVerse; i <= endVerse; i++) {
            sb.append(verse(i));
            if (i < endVerse) {
                sb.append("\n\n");
            }
        }
        return sb.toString();
    }

    public String sing() {
        return verses(1, RHYME_LINES.length);
    }
}
