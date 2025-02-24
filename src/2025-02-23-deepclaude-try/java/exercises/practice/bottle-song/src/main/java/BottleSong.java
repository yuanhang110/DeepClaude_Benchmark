import java.util.ArrayList;
import java.util.List;

public class BottleSong {

    public String recite(int startBottles, int takeDown) {
        List<String> verses = new ArrayList<>();
        int current = startBottles;
        while (takeDown > 0 && current >= 1) {
            verses.add(generateVerse(current));
            current--;
            takeDown--;
        }
        return String.join("\n\n", verses);
    }

    private String generateVerse(int current) {
        int next = current - 1;
        String line1 = getBottleLine(current);
        String line3 = "And if one green bottle should accidentally fall,";
        String line4 = getNextLine(next);
        return String.join("\n", line1, line1, line3, line4);
    }

    private String getBottleLine(int number) {
        String countWord = numberToWord(number);
        String bottles = (number == 1) ? "bottle" : "bottles";
        return String.format("%s green %s hanging on the wall,", countWord, bottles);
    }

    private String getNextLine(int nextNumber) {
        String nextCountWord = getNextCountWord(nextNumber);
        String nextBottles = (nextNumber == 1) ? "bottle" : "bottles";
        return String.format("There'll be %s green %s hanging on the wall.", nextCountWord, nextBottles);
    }

    private String getNextCountWord(int number) {
        if (number == 0) {
            return "no";
        } else {
            return numberToWord(number).toLowerCase();
        }
    }

    private String numberToWord(int number) {
        switch (number) {
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
            default: return "";
        }
    }
}
