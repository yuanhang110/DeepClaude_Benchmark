class FoodChain {
    private static final String[] ANIMALS = {
        "fly", "spider", "bird", "cat", "dog", "goat", "cow", "horse"
    };
    
    private static final String[] DESCRIPTIONS = {
        "",
        "It wriggled and jiggled and tickled inside her.",
        "How absurd to swallow a bird!",
        "Imagine that, to swallow a cat!",
        "What a hog, to swallow a dog!",
        "Just opened her throat and swallowed a goat!",
        "I don't know how she swallowed a cow!",
        "She's dead, of course!"
    };
    
    private String buildChain(int verse) {
        StringBuilder chain = new StringBuilder();
        for (int i = verse - 1; i > 0; i--) {
            chain.append("She swallowed the " + ANIMALS[i] + " to catch the " + ANIMALS[i-1]);
            if (i == 2) {
                chain.append(" that wriggled and jiggled and tickled inside her");
            }
            chain.append(".\n");
        }
        return chain.toString();
    }

    String verse(int verse) {
        if (verse < 1 || verse > 8) {
            throw new IllegalArgumentException("Verse number must be between 1 and 8");
        }
        
        if (verse == 8) {
            return "I know an old lady who swallowed a horse.\nShe's dead, of course!\n";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("I know an old lady who swallowed a " + ANIMALS[verse-1] + ".\n");
        if (verse > 1) {
            sb.append(DESCRIPTIONS[verse-1] + "\n");
        }
        sb.append(buildChain(verse));
        sb.append("I don't know why she swallowed the fly. Perhaps she'll die.\n");
        return sb.toString();
    }

    String verses(int startVerse, int endVerse) {
        if (startVerse < 1 || endVerse > 8 || startVerse > endVerse) {
            throw new IllegalArgumentException("Verses must be between 1 and 8, and startVerse must be <= endVerse");
        }
        
        StringBuilder sb = new StringBuilder();
        for (int i = startVerse; i <= endVerse; i++) {
            sb.append(verse(i));
            if (i < endVerse) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }
}
