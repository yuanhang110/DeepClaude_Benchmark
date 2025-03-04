class FoodChain {

    private static final String[] ANIMALS = {
        "fly",
        "spider",
        "bird",
        "cat",
        "dog",
        "goat",
        "cow",
        "horse"
    };

    private static final String[] COMMENTS = {
        "",
        "It wriggled and jiggled and tickled inside her.",
        "How absurd to swallow a bird!",
        "Imagine that, to swallow a cat!",
        "What a hog, to swallow a dog!",
        "Just opened her throat and swallowed a goat!",
        "I don't know how she swallowed a cow!",
        ""
    };

    String verse(int verse) {
        if (verse == 8) {
            return "I know an old lady who swallowed a horse.\nShe's dead, of course!";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("I know an old lady who swallowed a ")
            .append(ANIMALS[verse - 1])
            .append(".\n");

        if (verse > 1) {
            sb.append(COMMENTS[verse - 1]).append("\n");
        }

        for (int j = verse - 1; j >= 1; j--) {
            sb.append("She swallowed the ")
                .append(ANIMALS[j])
                .append(" to catch the ")
                .append(ANIMALS[j - 1]);
            if (j - 1 == 1) { // When previous animal is spider
                sb.append(" that wriggled and jiggled and tickled inside her.");
            } else {
                sb.append(".");
            }
            sb.append("\n");
        }

        sb.append("I don't know why she swallowed the fly. Perhaps she'll die.\n");

        return sb.toString();
    }

    String verses(int startVerse, int endVerse) {
        StringBuilder sb = new StringBuilder();
        for (int i = startVerse; i <= endVerse; i++) {
            sb.append(verse(i));
            if (i != endVerse) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }
}
