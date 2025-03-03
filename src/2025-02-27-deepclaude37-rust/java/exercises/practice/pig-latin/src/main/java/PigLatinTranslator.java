class PigLatinTranslator {
    public String translate(String word) {
        // Rule 1: If word begins with vowel sound or starts with "xr" or "yt"
        if (startsWithVowelSound(word)) {
            return word + "ay";
        }
        
        // Rule 3: If word starts with consonants followed by "qu"
        if (startsWithConsonantsAndQu(word)) {
            int quIndex = word.indexOf("qu");
            String prefix = word.substring(0, quIndex + 2);
            String suffix = word.substring(quIndex + 2);
            return suffix + prefix + "ay";
        }
        
        // Rule 4: If word starts with consonants followed by "y"
        if (startsWithConsonantsAndY(word)) {
            int yIndex = word.indexOf('y');
            String prefix = word.substring(0, yIndex);
            String suffix = word.substring(yIndex);
            return suffix + prefix + "ay";
        }
        
        // Rule 2: If word begins with consonants
        int firstVowelIndex = findFirstVowelIndex(word);
        if (firstVowelIndex > 0) {
            String prefix = word.substring(0, firstVowelIndex);
            String suffix = word.substring(firstVowelIndex);
            return suffix + prefix + "ay";
        }
        
        // Fallback case
        return word + "ay";
    }
    
    private boolean startsWithVowelSound(String word) {
        if (word.isEmpty()) {
            return false;
        }
        char first = word.charAt(0);
        if (first == 'a' || first == 'e' || first == 'i' || first == 'o' || first == 'u') {
            return true;
        }
        return word.length() >= 2 && 
               (word.startsWith("xr") || word.startsWith("yt"));
    }
    
    private boolean startsWithConsonantsAndQu(String word) {
        int quIndex = word.indexOf("qu");
        if (quIndex < 0) {
            return false;
        }
        
        for (int i = 0; i < quIndex; i++) {
            char c = word.charAt(i);
            if (c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u') {
                return false;
            }
        }
        return true;
    }
    
    private boolean startsWithConsonantsAndY(String word) {
        int yIndex = word.indexOf('y');
        if (yIndex <= 0) {
            return false;
        }
        
        for (int i = 0; i < yIndex; i++) {
            char c = word.charAt(i);
            if (c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u') {
                return false;
            }
        }
        return true;
    }
    
    private int findFirstVowelIndex(String word) {
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u') {
                return i;
            }
        }
        return -1;
    }
}
