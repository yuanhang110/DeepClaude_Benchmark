class PigLatinTranslator {
    public String translate(String word) {
        // Rule 1: Check vowel, xr, or yt start
        if (word.startsWith("xr") || word.startsWith("yt") || isVowel(word.charAt(0))) {
            return word + "ay";
        }
        
        // Rule 3: Check for 'qu' after consonants
        for (int i = 0; i < word.length() - 1; i++) {
            if (word.charAt(i) == 'q' && word.charAt(i + 1) == 'u' 
                && allConsonants(word.substring(0, i))) {
                return word.substring(i + 2) + word.substring(0, i + 2) + "ay";
            }
        }
        
        // Rule 4: Check for consonant(s) followed by y
        for (int i = 1; i < word.length(); i++) {
            if (word.charAt(i) == 'y' && allConsonants(word.substring(0, i))) {
                return word.substring(i) + word.substring(0, i) + "ay";
            }
        }
        
        // Rule 2: Move leading consonants to end
        int firstVowel = -1;
        for (int i = 0; i < word.length(); i++) {
            if (isVowel(word.charAt(i))) {
                firstVowel = i;
                break;
            }
        }
        if (firstVowel > 0) {
            return word.substring(firstVowel) + word.substring(0, firstVowel) + "ay";
        }
        
        // Fallback for words with only consonants
        return word + "ay";
    }
    
    private boolean isVowel(char c) {
        return "aeiou".indexOf(c) != -1;
    }
    
    private boolean allConsonants(String str) {
        for (char c : str.toCharArray()) {
            if (isVowel(c)) {
                return false;
            }
        }
        return true;
    }

}
