class BottleSong {

    String recite(int startBottles, int takeDown) {
        StringBuilder verses = new StringBuilder();
        for (int i = 0; i < takeDown && startBottles - i > 0; i++) {
            int current = startBottles - i;
            String verse = generateVerse(current);
            if (verses.length() > 0) {
                verses.append("\n\n");
            }
            verses.append(verse);
        }
        return verses.toString();
    }

    private String generateVerse(int current) {
        String currentLine = getBottleLine(current);
        int next = current - 1;
        String nextLine = getBottleLine(next);
        
        String line1 = currentLine + " hanging on the wall,";
        String line2 = currentLine + " hanging on the wall,";
        String line3 = "And if one green bottle should accidentally fall,";
        String line4 = "There'll be " + nextLine.toLowerCase() + " hanging on the wall.";
        
        return String.join("\n", line1, line2, line3, line4);
    }

    private String getBottleLine(int n) {
        if (n == 0) {
            return "No green bottles";
        }
        String numberWord;
        switch (n) {
            case 10: numberWord = "Ten"; break;
            case 9: numberWord = "Nine"; break;
            case 8: numberWord = "Eight"; break;
            case 7: numberWord = "Seven"; break;
            case 6: numberWord = "Six"; break;
            case 5: numberWord = "Five"; break;
            case 4: numberWord = "Four"; break;
            case 3: numberWord = "Three"; break;
            case 2: numberWord = "Two"; break;
            case 1: numberWord = "One"; break;
            default: return "";
        }
        String bottles = (n == 1) ? "bottle" : "bottles";
        return numberWord + " green " + bottles;
    }

}
