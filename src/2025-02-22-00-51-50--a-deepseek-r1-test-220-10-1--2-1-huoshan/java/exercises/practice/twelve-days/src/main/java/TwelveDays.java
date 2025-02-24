import java.util.ArrayList;
import java.util.List;

class TwelveDays {
    private static final String[] days = {
        "first", "second", "third", "fourth", "fifth", "sixth",
        "seventh", "eighth", "ninth", "tenth", "eleventh", "twelfth"
    };

    private static final String[] gifts = {
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
        String day = days[verseNumber - 1];
        List<String> giftsList = new ArrayList<>();
        
        // Build gifts list in reverse order (current day to first day)
        for (int i = verseNumber - 1; i >= 0; i--) {
            giftsList.add(gifts[i]);
        }
        
        // Join gifts with proper formatting
        String giftsString;
        if (verseNumber == 1) {
            giftsString = giftsList.get(0);
        } else {
            String allButLast = String.join(", ", giftsList.subList(0, giftsList.size() - 1));
            giftsString = allButLast + ", and " + giftsList.get(giftsList.size() - 1);
        }
        
        return String.format("On the %s day of Christmas my true love gave to me: %s.%n", day, giftsString);
    }

    String verses(int startVerse, int endVerse) {
        StringBuilder sb = new StringBuilder();
        for (int i = startVerse; i <= endVerse; i++) {
            sb.append(verse(i));
            if (i != endVerse) {
                sb.append(System.lineSeparator());
            }
        }
        return sb.toString();
    }
    
    String sing() {
        return verses(1, 12);
    }
}
