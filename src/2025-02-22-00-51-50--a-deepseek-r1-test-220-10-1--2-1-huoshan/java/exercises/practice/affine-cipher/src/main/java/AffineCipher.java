public class AffineCipher {
    private static int gcd(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    private static int modInverse(int a, int m) {
        a = a % m;
        if (a < 0) {
            a += m;
        }
        for (int x = 1; x < m; x++) {
            int product = a * x;
            if (product % m == 1) {
                return x;
            }
        }
        throw new ArithmeticException("No modular inverse found");
    }

    private static void validateCoefficient1(int a) {
        if (gcd(a, 26) != 1) {
            throw new IllegalArgumentException("Coefficient 1 and 26 must be coprime.");
        }
    }

    private static String processText(String text) {
        StringBuilder sb = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (Character.isLetter(c)) {
                sb.append(Character.toLowerCase(c));
            } else if (Character.isDigit(c)) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public String encode(String text, int coefficient1, int coefficient2) {
        validateCoefficient1(coefficient1);
        String processed = processText(text);
        StringBuilder encoded = new StringBuilder();

        for (char c : processed.toCharArray()) {
            if (Character.isLetter(c)) {
                int i = c - 'a';
                int encrypted = (coefficient1 * i + coefficient2) % 26;
                if (encrypted < 0) {
                    encrypted += 26;
                }
                encoded.append((char) (encrypted + 'a'));
            } else {
                encoded.append(c);
            }
        }

        // Split into groups of 5 characters
        StringBuilder grouped = new StringBuilder();
        int count = 0;
        for (int i = 0; i < encoded.length(); i++) {
            grouped.append(encoded.charAt(i));
            count++;
            if (count == 5 && i != encoded.length() - 1) {
                grouped.append(' ');
                count = 0;
            }
        }

        return grouped.toString();
    }

    public String decode(String text, int coefficient1, int coefficient2) {
        validateCoefficient1(coefficient1);
        int aInv = modInverse(coefficient1, 26);
        String processed = processText(text);
        StringBuilder decoded = new StringBuilder();

        for (char c : processed.toCharArray()) {
            if (Character.isLetter(c)) {
                int y = c - 'a';
                int decrypted = (aInv * (y - coefficient2)) % 26;
                if (decrypted < 0) {
                    decrypted += 26;
                }
                decoded.append((char) (decrypted + 'a'));
            } else {
                decoded.append(c);
            }
        }

        return decoded.toString();
    }
}
