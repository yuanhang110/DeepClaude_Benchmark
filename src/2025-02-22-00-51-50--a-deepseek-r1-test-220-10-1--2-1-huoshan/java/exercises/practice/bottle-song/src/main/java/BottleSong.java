public class BottleSong {

    public String recite(int startBottles, int takeDown) {
        if (startBottles < 0 || takeDown < 0 || startBottles > 10) {
            throw new IllegalArgumentException("Bottle count must be between 0-10");
        }
        
        StringBuilder verses = new StringBuilder();
        for (int i = 0; i < takeDown && (startBottles - i) > 0; i++) {
            int current = startBottles - i;
            int next = current - 1;
            
            String line1 = String.format("%s green %s hanging on the wall,", numberToWord(current), getBottleText(current));
            String line2 = line1;
            String line3 = "And if one green bottle should accidentally fall,";
            String line4 = String.format("There'll be %s green %s hanging on the wall.", numberToWord(next).toLowerCase(), getBottleText(next));
            
            String verse = String.join("\n", line1, line2, line3, line4);
            if (verses.length() > 0) {
                verses.append("\n\n");
            }
            verses.append(verse);
        }
        return verses.toString();
    }

    private static String numberToWord(int n) {
        switch (n) {
            case 0: return "No";
            case 1: return "One";
            case 2: return "Two";
            case 3: return "Three";
            case 4: return "Four";
            case 5: return "Five";
            case 6: return "Six";
            case 7: return "Seven";
            case 8: return "Eight";
            case 9: return "Nine";
            case 10: return "Ten";
            default: return "";
        }
    }

    private static String getBottleText(int count) {
        if (count < 0) return "bottles";
        return count == 1 ? "bottle" : "bottles";
    }

}
