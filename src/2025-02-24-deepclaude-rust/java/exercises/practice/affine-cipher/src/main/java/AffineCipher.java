public class AffineCipher {
    
    public String encode(String text, int coefficient1, int coefficient2) {
        int a = coefficient1;
        int b = coefficient2;
        if (gcd(a, 26) != 1) {
            throw new IllegalArgumentException("Coefficient1 and 26 must be coprime.");
        }
        String processed = preprocess(text);
        StringBuilder encoded = new StringBuilder();
        for (char c : processed.toCharArray()) {
            if (Character.isLetter(c)) {
                int x = c - 'a';
                int encrypted = (a * x + b) % 26;
                if (encrypted < 0) {
                    encrypted += 26;
                }
                encoded.append((char) (encrypted + 'a'));
            } else {
                encoded.append(c);
            }
        }
        return groupText(encoded.toString());
    }

    public String decode(String text, int coefficient1, int coefficient2) {
        int a = coefficient1;
        int b = coefficient2;
        if (gcd(a, 26) != 1) {
            throw new IllegalArgumentException("Coefficient1 and 26 must be coprime.");
        }
        int inverseA = modInverse(a, 26);
        String processed = preprocess(text);
        StringBuilder decoded = new StringBuilder();
        for (char c : processed.toCharArray()) {
            if (Character.isLetter(c)) {
                int y = c - 'a';
                int temp = (y - b) % 26;
                temp = (temp + 26) % 26; // Ensure non-negative
                int x = (inverseA * temp) % 26;
                if (x < 0) {
                    x += 26;
                }
                decoded.append((char) (x + 'a'));
            } else {
                decoded.append(c);
            }
        }
        return decoded.toString();
    }

    private String preprocess(String text) {
        StringBuilder filtered = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (Character.isLetterOrDigit(c)) {
                if (Character.isLetter(c)) {
                    filtered.append(Character.toLowerCase(c));
                } else {
                    filtered.append(c);
                }
            }
        }
        return filtered.toString();
    }

    private String groupText(String text) {
        StringBuilder grouped = new StringBuilder();
        int count = 0;
        for (char c : text.toCharArray()) {
            grouped.append(c);
            count++;
            if (count % 5 == 0 && count < text.length()) {
                grouped.append(' ');
            }
        }
        return grouped.toString();
    }

    private int gcd(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    private int modInverse(int a, int m) {
        a = Math.floorMod(a, m);  // Ensure positive modulo
        if (a == 0) {
            throw new IllegalArgumentException("No modular inverse exists.");
        }
        
        int m0 = m;
        int y = 0, x = 1;
        
        while (a > 1) {
            int q = a / m;
            int t = m;
            
            m = a % m;
            a = t;
            t = y;
            
            y = x - q * y;
            x = t;
        }
        
        return x < 0 ? x + m0 : x;
    }
}
