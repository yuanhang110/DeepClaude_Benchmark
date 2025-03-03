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

    public String verse(int verseNumber) {
        StringBuilder verse = new StringBuilder();
        
        // Start of the verse
        verse.append("On the ")
             .append(ORDINALS[verseNumber - 1])
             .append(" day of Christmas my true love gave to me: ");
        
        // Add gifts in reverse order (from verseNumber down to 1)
        for (int i = verseNumber; i >= 1; i--) {
            // For day 1 gift when not the only gift, add "and "
            if (i == 1 && verseNumber > 1) {
                verse.append("and ");
            }
            
            verse.append(GIFTS[i - 1]);
            
            // Add comma separator except for the last gift
            if (i > 1) {
                verse.append(", ");
            }
        }
        
        // End the verse with a period
        verse.append(".");
        
        return verse.toString();
    }

    public String verses(int startVerse, int endVerse) {
        StringBuilder result = new StringBuilder();
        
        for (int i = startVerse; i <= endVerse; i++) {
            result.append(verse(i));
            
            // Add blank line between verses, but not after the last verse
            if (i < endVerse) {
                result.append("\n\n");
            }
        }
        
        return result.toString();
    }
    
    public String sing() {
        return verses(1, 12);
    }
}
