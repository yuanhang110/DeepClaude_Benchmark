public class FoodChain {
    private static final String[] ANIMALS = {
        "fly",
        "spider",
        "bird",
        "cat",
        "dog",
        "goat",
        "cow"
    };

    private static final String[] INTRODUCTION = {
        "I know an old lady who swallowed a fly.",
        "I know an old lady who swallowed a spider.",
        "I know an old lady who swallowed a bird.",
        "I know an old lady who swallowed a cat.",
        "I know an old lady who swallowed a dog.",
        "I know an old lady who swallowed a goat.",
        "I know an old lady who swallowed a cow."
    };

    private static final String[] ACTIONS = {
        "",
        "It wriggled and jiggled and tickled inside her.",
        "How absurd to swallow a bird!",
        "Imagine that, to swallow a cat!",
        "What a hog, to swallow a dog!",
        "Just opened her throat and swallowed a goat!",
        "I don't know how she swallowed a cow!"
    };

    public String verse(int number) {
        if (number == 8) {
            return "I know an old lady who swallowed a horse.\nShe's dead, of course!";
        }

        int index = number - 1;
        StringBuilder sb = new StringBuilder();
        sb.append(INTRODUCTION[index]).append("\n");
        if (!ACTIONS[index].isEmpty()) {
            sb.append(ACTIONS[index]).append("\n");
        }

        for (int j = index; j >= 1; j--) {
            String nextAnimal = ANIMALS[j - 1];
            String action = "";
            if (j - 1 == 1) { // Handle spider's action
                action = " that " + ACTIONS[1].substring(3).replaceFirst("\\.$", "");
            }
            sb.append("She swallowed the ").append(ANIMALS[j])
              .append(" to catch the ").append(nextAnimal)
              .append(action).append(".\n");
        }

        sb.append("I don't know why she swallowed the fly. Perhaps she'll die.\n");
        
        return sb.toString().trim();
    }

    public String verses(int startVerse, int endVerse) {
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
