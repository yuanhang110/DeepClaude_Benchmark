class PigLatinTranslator {
    public String translate(String word) {
        // Rule 1: starts with vowel or xr/yt
        if (startsWithVowel(word) || startsWithXrOrYt(word)) {
            return word + "ay";
        }
        
        // Rule 3: consonants (if any) followed by qu
        int quIndex = findQuIndex(word);
        if (quIndex != -1) {
            String prefix = word.substring(0, quIndex + 2); // includes 'qu'
            String suffix = word.substring(quIndex + 2);
            return suffix + prefix + "ay";
        }
        
        // Rule 4: consonants followed by y
        int yIndex = word.indexOf('y');
        if (yIndex > 0) { // y not first and has consonants before
            String prefix = word.substring(0, yIndex);
            if (allConsonants(prefix)) {
                String suffix = word.substring(yIndex);
                return suffix + prefix + "ay";
            }
        }
        
        // Rule 2: move consonants to end
        int firstVowelIndex = findFirstVowelIndex(word);
        if (firstVowelIndex == -1) {
            return word + "ay"; // all consonants
        }
        return word.substring(firstVowelIndex) + 
               word.substring(0, firstVowelIndex) + "ay";
    }

    private boolean startsWithVowel(String word) {
        if (word.isEmpty()) return false;
        return "aeiou".indexOf(word.charAt(0)) >= 0;
    }

    private boolean startsWithXrOrYt(String word) {
        return word.length() >= 2 && 
               (word.startsWith("xr") || word.startsWith("yt"));
    }

    private int findQuIndex(String word) {
        int index = word.indexOf("qu");
        if (index == -1) return -1;
        
        // Check that all characters before 'qu' are consonants
        return allConsonants(word.substring(0, index)) ? index : -1;
    }

    private int findFirstVowelIndex(String word) {
        for (int i = 0; i < word.length(); i++) {
            if ("aeiou".indexOf(word.charAt(i)) >= 0) {
                return i;
            }
        }
        return -1;
    }

    private boolean allConsonants(String str) {
        return str.chars().allMatch(c -> "aeiou".indexOf(c) < 0);
    }

}
