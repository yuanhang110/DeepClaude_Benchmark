public class AffineCipher {
    
    private int gcd(int a, int b) {
        a = Math.abs(a);
        b = Math.abs(b);
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    private int modInverse(int a, int m) {
        a = Math.floorMod(a, m);
        for (int x = 1; x < m; x++) {
            if (Math.floorMod(a * x, m) == 1) {
                return x;
            }
        }
        throw new IllegalArgumentException("No modular inverse exists.");
    }

    public String encode(String text, int coefficient1, int coefficient2) {
        int a = coefficient1;
        int b = coefficient2;
        if (gcd(a, 26) != 1) {
            throw new IllegalArgumentException("Coefficient 'a' must be coprime with 26.");
        }

        StringBuilder processed = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (Character.isLetter(c)) {
                processed.append(Character.toLowerCase(c));
            } else if (Character.isDigit(c)) {
                processed.append(c);
            }
        }
        String filtered = processed.toString();

        StringBuilder result = new StringBuilder();
        for (char c : filtered.toCharArray()) {
            if (Character.isLetter(c)) {
                int x = c - 'a';
                int encrypted = Math.floorMod(a * x + b, 26);
                result.append((char) (encrypted + 'a'));
            } else if (Character.isDigit(c)) {
                result.append(c);
            }
        }

        // Group into 5-letter chunks
        StringBuilder grouped = new StringBuilder();
        int count = 0;
        for (char c : result.toString().toCharArray()) {
            grouped.append(c);
            count++;
            if (count == 5) {
                grouped.append(' ');
                count = 0;
            }
        }
        return grouped.toString().trim();
    }

    public String decode(String text, int coefficient1, int coefficient2) {
        int a = coefficient1;
        int b = coefficient2;
        if (gcd(a, 26) != 1) {
            throw new IllegalArgumentException("Coefficient 'a' must be coprime with 26.");
        }

        int aInv = modInverse(a, 26);

        StringBuilder filtered = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (Character.isLetterOrDigit(c)) {
                filtered.append(Character.toLowerCase(c));
            }
        }
        String input = filtered.toString();

        StringBuilder result = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (Character.isLetter(c)) {
                int y = c - 'a';
                int decrypted = Math.floorMod(aInv * (y - b), 26);
                result.append((char) (decrypted + 'a'));
            } else if (Character.isDigit(c)) {
                result.append(c);
            }
        }

        return result.toString();
    }
}
