class BottleSong {

    String recite(int startBottles, int takeDown) {
        StringBuilder song = new StringBuilder();
        for (int i = 0; i < takeDown; i++) {
            int current = startBottles - i;
            String verse = buildVerse(current);
            if (i > 0) {
                song.append("\n\n");
            }
            song.append(verse);
        }
        return song.toString();
    }

    private String buildVerse(int current) {
        String line = getBottleLine(current);
        return String.join("\n", line, line, 
            "And if one green bottle should accidentally fall,",
            getNextBottleLine(current - 1));
    }

    private String getBottleLine(int number) {
        return String.format("%s green %s hanging on the wall,", 
            numberToWord(number), 
            number == 1 ? "bottle" : "bottles");
    }

    private String getNextBottleLine(int next) {
        String word = next == 0 ? "no" : numberToWord(next).toLowerCase();
        String bottles = next == 1 ? "bottle" : "bottles";
        return String.format("There'll be %s green %s hanging on the wall.", word, bottles);
    }

    private String numberToWord(int number) {
        if (number < 1 || number > 10) {
            throw new IllegalArgumentException("Invalid bottle count: " + number);
        }
        String[] words = { "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten" };
        return words[number - 1];
    }
}
