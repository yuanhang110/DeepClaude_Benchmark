public class AffineCipher {
    
    public String encode(String text, int coefficient1, int coefficient2) {
        validateCoefficient1(coefficient1);
        String processed = processEncode(text, coefficient1, coefficient2);
        StringBuilder grouped = new StringBuilder();
        int len = processed.length();
        int groupPos = 0;
        for (int i = 0; i < len; i++) {
            if (groupPos == 5) {
                grouped.append(' ');
                groupPos = 0;
            }
            grouped.append(processed.charAt(i));
            groupPos++;
        }
        return grouped.toString();
    }

    public String decode(String text, int coefficient1, int coefficient2) {
        validateCoefficient1(coefficient1);
        int mmi = modInverse(coefficient1);
        String processedText = text.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
        return processDecode(processedText, coefficient1, coefficient2, mmi);
    }

    private static void validateCoefficient1(int a) {
        if (gcd(a, 26) != 1) {
            throw new IllegalArgumentException("Coefficient1 and 26 must be coprime.");
        }
    }

    private static int gcd(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    private static int modInverse(int a) {
        a = a % 26;
        for (int x = 1; x < 26; x++) {
            if ((a * x) % 26 == 1) {
                return x;
            }
        }
        throw new IllegalArgumentException("No modular inverse exists.");
    }

    private static String processEncode(String text, int a, int b) {
        StringBuilder result = new StringBuilder();
        for (char ch : text.toCharArray()) {
            if (Character.isLetter(ch)) {
                char lowerCh = Character.toLowerCase(ch);
                int x = lowerCh - 'a';
                int encrypted = (a * x + b) % 26;
                if (encrypted < 0) {
                    encrypted += 26;
                }
                result.append((char) (encrypted + 'a'));
            } else if (Character.isDigit(ch)) {
                result.append(ch);
            }
        }
        return result.toString();
    }

    private static String processDecode(String text, int a, int b, int mmi) {
        StringBuilder result = new StringBuilder();
        for (char ch : text.toCharArray()) {
            if (Character.isLetter(ch)) {
                char lowerCh = Character.toLowerCase(ch);
                int y = lowerCh - 'a';
                int decrypted = (mmi * (y - b)) % 26;
                if (decrypted < 0) {
                    decrypted += 26;
                }
                result.append((char) (decrypted + 'a'));
            } else if (Character.isDigit(ch)) {
                result.append(ch);
            }
        }
        return result.toString();
    }
}
