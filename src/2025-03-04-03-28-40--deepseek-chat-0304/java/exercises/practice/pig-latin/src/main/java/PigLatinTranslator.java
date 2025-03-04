public class PigLatinTranslator {
    private static final String VOWELS = "aeiouAEIOU";
    private static final String[] SPECIAL_STARTS = {"xr", "yt", "Xr", "Yt"};
    
    public String translate(String input) {
        if (input == null || input.trim().isEmpty()) {
            return input;
        }
        
        String[] words = input.split(" ");
        StringBuilder result = new StringBuilder();
        
        for (String word : words) {
            if (result.length() > 0) {
                result.append(" ");
            }
            result.append(translateWord(word));
        }
        
        return result.toString();
    }
    
    private String translateWord(String word) {
        if (startsWithVowel(word) || startsWithSpecial(word)) {
            return word + "ay";
        }
        
        if (word.startsWith("qu")) {
            return word.substring(2) + "quay";
        }
        
        if (word.contains("qu")) {
            int quIndex = word.indexOf("qu");
            return word.substring(quIndex + 2) + word.substring(0, quIndex + 2) + "ay";
        }
        
        if (word.contains("y") && word.indexOf("y") > 0) {
            int yIndex = word.indexOf("y");
            return word.substring(yIndex) + word.substring(0, yIndex) + "ay";
        }
        
        int firstVowelIndex = getFirstVowelIndex(word);
        if (firstVowelIndex == -1) {
            return word + "ay";
        }
        
        return word.substring(firstVowelIndex) + word.substring(0, firstVowelIndex) + "ay";
    }
    
    private boolean startsWithVowel(String word) {
        if (word == null || word.isEmpty()) {
            return false;
        }
        return VOWELS.indexOf(word.charAt(0)) != -1;
    }
    
    private boolean startsWithSpecial(String word) {
        for (String start : SPECIAL_STARTS) {
            if (word.startsWith(start)) {
                return true;
            }
        }
        return false;
    }
    
    private int getFirstVowelIndex(String word) {
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (VOWELS.indexOf(c) != -1 || c == 'y') {
                return i;
            }
        }
        return -1;
    }
}
