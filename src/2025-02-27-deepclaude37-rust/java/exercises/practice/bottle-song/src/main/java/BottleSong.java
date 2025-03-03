class BottleSong {

    String recite(int startBottles, int takeDown) {
        StringBuilder verses = new StringBuilder();
        for (int i = 0; i < takeDown; i++) {
            int current = startBottles - i;
            if (current <= 0) {
                break;
            }
            int next = current - 1;
            String currentWord = numberToWord(current);
            String nextWord = numberToWord(next);
            
            String line1 = String.format("%s green %s hanging on the wall,", 
                currentWord, current == 1 ? "bottle" : "bottles");
            String line2 = line1;
            String line3 = "And if one green bottle should accidentally fall,";
            String line4 = String.format("There'll be %s green %s hanging on the wall.", 
                nextWord.toLowerCase(), next == 1 ? "bottle" : "bottles");
            
            String verse = String.join("\n", line1, line2, line3, line4);
            if (verses.length() > 0) {
                verses.append("\n\n");
            }
            verses.append(verse);
        }
        return verses.toString();
    }

    private String numberToWord(int n) {
        switch (n) {
            case 10: return "Ten";
            case 9: return "Nine";
            case 8: return "Eight";
            case 7: return "Seven";
            case 6: return "Six";
            case 5: return "Five";
            case 4: return "Four";
            case 3: return "Three";
            case 2: return "Two";
            case 1: return "One";
            case 0: return "no";
            default: throw new IllegalArgumentException("Unsupported number: " + n);
        }
    }

}
