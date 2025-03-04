public class AffineCipher {
    private static final int M = 26;
    private static final int GROUP_SIZE = 5;
    
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
        for (int x = 1; x < m; x++) {
            if ((a * x) % m == 1) {
                return x;
            }
        }
        return -1;
    }
    
    private static String processText(String text, boolean keepSpaces) {
        if (text == null) {
            return "";
        }
        return text.toLowerCase().replaceAll("[^a-z0-9]", "");
    }
    
    private static String groupText(String text) {
        StringBuilder grouped = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            if (i > 0 && i % GROUP_SIZE == 0) {
                grouped.append(' ');
            }
            grouped.append(text.charAt(i));
        }
        return grouped.toString();
    }
    
    public String encode(String text, int coefficient1, int coefficient2) {
        if (text == null) {
            return "";
        }
        if (coefficient1 < 0 || coefficient2 < 0) {
            throw new IllegalArgumentException("Coefficients must be non-negative");
        }
        if (gcd(coefficient1, M) != 1) {
            throw new IllegalArgumentException("Error: a and m must be coprime.");
        }
        
        String processed = processText(text, false);
        StringBuilder ciphertext = new StringBuilder();
        
        for (char c : processed.toCharArray()) {
            if (Character.isDigit(c)) {
                ciphertext.append(c);
                continue;
            }
            
            int x = c - 'a';
            int encrypted = (coefficient1 * x + coefficient2) % M;
            if (encrypted < 0) encrypted += M;
            ciphertext.append((char) (encrypted + 'a'));
        }
        
        return groupText(ciphertext.toString());
    }

    public String decode(String text, int coefficient1, int coefficient2) {
        if (text == null) {
            return "";
        }
        if (coefficient1 < 0 || coefficient2 < 0) {
            throw new IllegalArgumentException("Coefficients must be non-negative");
        }
        if (gcd(coefficient1, M) != 1) {
            throw new IllegalArgumentException("Error: a and m must be coprime.");
        }
        
        String processed = processText(text, true);
        StringBuilder plaintext = new StringBuilder();
        int aInverse = modInverse(coefficient1, M);
        
        for (char c : processed.toCharArray()) {
            if (Character.isDigit(c)) {
                plaintext.append(c);
                continue;
            }
            
            int y = c - 'a';
            int decrypted = aInverse * (y - coefficient2) % M;
            if (decrypted < 0) decrypted += M;
            plaintext.append((char) (decrypted + 'a'));
        }
        
        return plaintext.toString();
    }
}
