class PigLatinTranslator {
    public String translate(String word) {
        // Rule 1: starts with vowel, xr, yt
        if (word.matches("^([aeiou]|xr|yt)")) {
            return word + "ay";
        }

        // Rule 3: consonants (maybe none) followed by qu
        if (word.matches("^[^aeiou]*qu")) {
            return word.replaceAll("^([^aeiou]*qu)(.*)$", "$2$1ay");
        }

        // Rule 4: consonants followed by y
        if (word.matches("^[^aeiou]+y")) {
            return word.replaceAll("^([^aeiou]+)(y.*)$", "$2$1ay");
        }

        // Rule 2: move consonants
        return word.replaceAll("^([^aeiou]+)(.*)$", "$2$1ay");
    }
}
