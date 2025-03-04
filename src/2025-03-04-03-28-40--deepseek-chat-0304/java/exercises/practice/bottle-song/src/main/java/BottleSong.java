/**
 * The BottleSong class generates the lyrics for the "Ten Green Bottles" song.
 * It can generate verses starting from any number of bottles and for any number of verses.
 */
public class BottleSong {

    /**
     * Generates a single verse for the given number of bottles.
     * @param bottles The number of bottles in the verse (must be >= 0)
     * @return The verse as a formatted string
     * @throws IllegalArgumentException if bottles is negative
     */
    private String getVerse(int bottles) {
        if (bottles < 0) {
            throw new IllegalArgumentException("Number of bottles cannot be negative");
        }
        
        if (bottles == 0) {
            return "No green bottles hanging on the wall,\n" +
                   "No green bottles hanging on the wall,\n" +
                   "There are no green bottles hanging on the wall.\n";
        }
        
        String bottleWord = bottles == 1 ? "bottle" : "bottles";
        String nextBottleWord = (bottles - 1) == 1 ? "bottle" : "bottles";
        String nextCount = bottles - 1 == 0 ? "no" : String.valueOf(bottles - 1);
        
        return String.format("%d green %s hanging on the wall,\n" +
                           "%d green %s hanging on the wall,\n" +
                           "And if one green bottle should accidentally fall,\n" +
                           "There'll be %s green %s hanging on the wall.\n",
                           bottles, bottleWord,
                           bottles, bottleWord,
                           nextCount, nextBottleWord);
    }

    /**
     * Generates the song lyrics starting from a given number of bottles and for a specified number of verses.
     * @param startBottles The starting number of bottles (must be >= 0)
     * @param takeDown The number of verses to generate (must be >= 0 and <= startBottles + 1)
     * @return The song lyrics as a formatted string
     * @throws IllegalArgumentException if parameters are invalid
     */
    public String recite(int startBottles, int takeDown) {
        if (startBottles < 0) {
            throw new IllegalArgumentException("Starting bottles cannot be negative");
        }
        if (takeDown < 0) {
            throw new IllegalArgumentException("Number of verses cannot be negative");
        }
        if (takeDown > startBottles + 1) {
            throw new IllegalArgumentException("Cannot take down more bottles than available");
        }

        StringBuilder song = new StringBuilder();
        for (int i = startBottles; i > startBottles - takeDown; i--) {
            song.append(getVerse(i));
            if (i > startBottles - takeDown + 1) {
                song.append("\n");
            }
        }
        return song.toString();
    }
}
