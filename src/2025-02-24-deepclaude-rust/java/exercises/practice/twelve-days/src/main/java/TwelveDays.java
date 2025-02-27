class TwelveDays {
    private static final String[] ORDINALS = {
        "first", "second", "third", "fourth", "fifth", "sixth",
        "seventh", "eighth", "ninth", "tenth", "eleventh", "twelfth"
    };

    private static final String[] GIFTS = {
        "a Partridge in a Pear Tree",
        "two Turtle Doves",
        "three French Hens",
        "four Calling Birds",
        "five Gold Rings",
        "six Geese-a-Laying",
        "seven Swans-a-Swimming",
        "eight Maids-a-Milking",
        "nine Ladies Dancing",
        "ten Lords-a-Leaping",
        "eleven Pipers Piping",
        "twelve Drummers Drumming"
    };

    String verse(int verseNumber) {
        List<String> giftsList = new ArrayList<>();
        for (int i = verseNumber; i >= 1; i--) {
            giftsList.add(GIFTS[i - 1]);
        }

        String giftsString;
        if (giftsList.size() == 1) {
            giftsString = giftsList.get(0);
        } else {
            String allButLast = String.join(", ", giftsList.subList(0, giftsList.size() - 1));
            String last = "and " + giftsList.get(giftsList.size() - 1);
            giftsString = allButLast + ", " + last;
        }

        return String.format("On the %s day of Christmas my true love gave to me: %s.\n", ORDINALS[verseNumber - 1], giftsString);
    }

    String verses(int startVerse, int endVerse) {
        StringBuilder sb = new StringBuilder();
        for (int i = startVerse; i <= endVerse; i++) {
            sb.append(verse(i));
            if (i < endVerse) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }
    
    String sing() {
        return verses(1, 12);
    }
}
import java.util.ArrayList;
import java.util.List;
