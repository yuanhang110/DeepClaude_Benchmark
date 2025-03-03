class FoodChain {
    private static final String[][] ANIMALS = {
        {"fly", "", "I don't know why she swallowed the fly. Perhaps she'll die."},
        {"spider", "It wriggled and jiggled and tickled inside her.", "She swallowed the spider to catch the fly."},
        {"bird", "How absurd to swallow a bird!", "She swallowed the bird to catch the spider that wriggled and jiggled and tickled inside her."},
        {"cat", "Imagine that, to swallow a cat!", "She swallowed the cat to catch the bird."},
        {"dog", "What a hog, to swallow a dog!", "She swallowed the dog to catch the cat."},
        {"goat", "Just opened her throat and swallowed a goat!", "She swallowed the goat to catch the dog."},
        {"cow", "I don't know how she swallowed a cow!", "She swallowed the cow to catch the goat."},
        {"horse", "She's dead, of course!", ""}
    };

    String verse(int verse) {
        if (verse < 1 || verse > 8) {
            throw new IllegalArgumentException("Verse must be between 1 and 8");
        }

        StringBuilder sb = new StringBuilder();
        int index = verse - 1;
        
        // First line of each verse
        sb.append("I know an old lady who swallowed a ").append(ANIMALS[index][0]).append(".\n");
        
        // Second line (description of the animal or action)
        if (!ANIMALS[index][1].isEmpty()) {
            sb.append(ANIMALS[index][1]).append("\n");
        }
        
        // For the horse verse, we're done after the description
        if (verse == 8) {
            return sb.toString();
        }
        
        // Add the chain of animals in reverse order
        for (int i = index; i >= 0; i--) {
            if (!ANIMALS[i][2].isEmpty()) {
                sb.append(ANIMALS[i][2]);
                if (i > 0) {
                    sb.append("\n");
                }
            }
        }
        
        return sb.toString();
    }

    String verses(int startVerse, int endVerse) {
        StringBuilder sb = new StringBuilder();
        for (int i = startVerse; i <= endVerse; i++) {
            sb.append(verse(i));
            if (i < endVerse) {
                sb.append("\n\n");
            }
        }
        return sb.toString();
    }
}
