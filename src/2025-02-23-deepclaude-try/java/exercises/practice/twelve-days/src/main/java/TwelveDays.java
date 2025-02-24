import java.util.ArrayList;
import java.util.Collections;

public class TwelveDays {
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
        int index = verseNumber - 1;
        String day = ORDINALS[index];

        ArrayList<String> giftsList = new ArrayList<>();
        for (int i = 0; i < verseNumber; i++) {
            giftsList.add(GIFTS[i]);
        }
        Collections.reverse(giftsList);

        String giftsString;
        if (giftsList.size() == 1) {
            giftsString = giftsList.get(0);
        } else {
            String allButLast = String.join(", ", giftsList.subList(0, giftsList.size() - 1));
            giftsString = allButLast + ", and " + giftsList.get(giftsList.size() - 1);
        }

        return String.format("On the %s day of Christmas my true love gave to me: %s.", day, giftsString);
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
