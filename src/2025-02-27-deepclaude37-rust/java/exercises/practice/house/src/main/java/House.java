class House {
    private static final String[] subjects = {
        "house that Jack built.",
        "malt",
        "rat",
        "cat",
        "dog",
        "cow with the crumpled horn",
        "maiden all forlorn",
        "man all tattered and torn",
        "priest all shaven and shorn",
        "rooster that crowed in the morn",
        "farmer sowing his corn",
        "horse and the hound and the horn"
    };

    private static final String[] actions = {
        "",
        "lay in the ",
        "ate the ",
        "killed the ",
        "worried the ",
        "tossed the ",
        "milked the ",
        "kissed the ",
        "married the ",
        "woke the ",
        "kept the ",
        "belonged to the "
    };

    String verse(int verse) {
        StringBuilder sb = new StringBuilder();
        sb.append("This is the ").append(subjects[verse - 1]);
        for (int j = 0; j < verse - 1; j++) {
            sb.append("\nthat ").append(actions[j + 1]).append(subjects[j]);
        }
        return sb.toString();
    }

    String verses(int startVerse, int endVerse) {
        StringBuilder sb = new StringBuilder();
        for (int i = startVerse; i <= endVerse; i++) {
            sb.append(verse(i));
            if (i != endVerse) {
                sb.append("\n\n");
            }
        }
        return sb.toString();
    }

    String sing() {
        return verses(1, 12);
    }
}
