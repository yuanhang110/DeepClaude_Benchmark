class House {
    private static final String[] PARTS = {
        "house that Jack built.",
        "malt\nthat lay in the ",
        "rat\nthat ate the ",
        "cat\nthat killed the ",
        "dog\nthat worried the ",
        "cow with the crumpled horn\nthat tossed the ",
        "maiden all forlorn\nthat milked the ",
        "man all tattered and torn\nthat kissed the ",
        "priest all shaven and shorn\nthat married the ",
        "rooster that crowed in the morn\nthat woke the ",
        "farmer sowing his corn\nthat kept the ",
        "horse and the hound and the horn\nthat belonged to the "
    };

    String verse(int verseNumber) {
        StringBuilder builder = new StringBuilder("This is the ");
        for (int i = verseNumber - 1; i >= 0; i--) {
            builder.append(PARTS[i]);
        }
        return builder.toString();
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

    String sing() {
        return verses(1, 12);
    }
}
