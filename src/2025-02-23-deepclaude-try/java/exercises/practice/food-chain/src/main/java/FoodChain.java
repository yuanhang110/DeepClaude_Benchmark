class FoodChain {
    private static final String[] ANIMALS = {
        "fly", "spider", "bird", "cat", "dog", "goat", "cow", "horse"
    };
    
    private static final String[] COMMENTS = {
        "I don't know why she swallowed the fly. Perhaps she'll die.",
        "It wriggled and jiggled and tickled inside her.",
        "How absurd to swallow a bird!",
        "Imagine that, to swallow a cat!",
        "What a hog, to swallow a dog!",
        "Just opened her throat and swallowed a goat!",
        "I don't know how she swallowed a cow!",
        "She's dead, of course!"
    };

    String verse(int verse) {
        verse--; // Convert to 0-based index
        StringBuilder result = new StringBuilder();
        
        // Special case for the last verse (horse)
        if (verse == 7) {
            return "I know an old lady who swallowed a horse.\n" +
                   "She's dead, of course!";
        }
        
        // First line is always the same pattern
        result.append("I know an old lady who swallowed a ").append(ANIMALS[verse]).append(".\n");
        
        // Add the specific comment for this animal (if not fly)
        if (verse > 0) {
            result.append(COMMENTS[verse]).append("\n");
        }
        
        // Build the chain of swallowing
        for (int i = verse; i > 0; i--) {
            String line = "She swallowed the " + ANIMALS[i] + " to catch the " + ANIMALS[i-1];
            // Add special phrase for spider when it's the prey
            if (i - 1 == 1) {
                line += " that wriggled and jiggled and tickled inside her";
            }
            result.append(line).append(".\n");
        }
        
        // Always end with the fly line for verses 1-7
        result.append(COMMENTS[0]).append("\n");
        
        return result.toString();
    }

    String verses(int startVerse, int endVerse) {
        StringBuilder result = new StringBuilder();
        
        for (int i = startVerse; i <= endVerse; i++) {
            result.append(verse(i));
            if (i < endVerse) {
                result.append("\n");
            }
        }
        
        return result.toString();
    }
}
