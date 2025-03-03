public class AffineCipher {
    
    private static final int ALPHABET_SIZE = 26;
    
    public String encode(String text, int coefficient1, int coefficient2) {
        // Check if coefficient1 and ALPHABET_SIZE are coprime
        if (gcd(coefficient1, ALPHABET_SIZE) != 1) {
            throw new IllegalArgumentException("a and m must be coprime.");
        }
        
        StringBuilder result = new StringBuilder();
        
        // Process each character in input text
        for (char c : text.toCharArray()) {
            if (Character.isLetter(c)) {
                // Convert to lowercase and get position (0-25)
                int position = Character.toLowerCase(c) - 'a';
                
                // Apply affine cipher formula: E(x) = (ax + b) mod m
                int encryptedPosition = (coefficient1 * position + coefficient2) % ALPHABET_SIZE;
                // Ensure positive modulo result
                if (encryptedPosition < 0) {
                    encryptedPosition += ALPHABET_SIZE;
                }
                
                // Convert back to character and append
                result.append((char) (encryptedPosition + 'a'));
            } else if (Character.isDigit(c)) {
                // Keep digits as is
                result.append(c);
            }
            // Skip spaces and punctuation
        }
        
        // Group the output in chunks of 5 letters
        return groupIntoFive(result.toString());
    }

    public String decode(String text, int coefficient1, int coefficient2) {
        // Check if coefficient1 and ALPHABET_SIZE are coprime
        if (gcd(coefficient1, ALPHABET_SIZE) != 1) {
            throw new IllegalArgumentException("a and m must be coprime.");
        }
        
        // Calculate the modular multiplicative inverse of coefficient1
        int aInverse = findModularMultiplicativeInverse(coefficient1);
        
        StringBuilder result = new StringBuilder();
        
        // Remove all spaces and process each character
        for (char c : text.toCharArray()) {
            if (Character.isLetter(c)) {
                // Convert to lowercase and get position (0-25)
                int position = Character.toLowerCase(c) - 'a';
                
                // Apply affine cipher decryption formula: D(y) = a^-1(y - b) mod m
                // Calculate (position - coefficient2) and ensure it's positive before multiplying
                int diff = position - coefficient2;
                while (diff < 0) {
                    diff += ALPHABET_SIZE;
                }
                
                int decryptedPosition = (aInverse * diff) % ALPHABET_SIZE;
                // Ensure positive modulo result
                if (decryptedPosition < 0) {
                    decryptedPosition += ALPHABET_SIZE;
                }
                
                // Convert back to character and append
                result.append((char) (decryptedPosition + 'a'));
            } else if (Character.isDigit(c)) {
                // Keep digits as is
                result.append(c);
            }
            // Skip spaces and punctuation
        }
        
        return result.toString();
    }
    
    // Helper method to find greatest common divisor using Euclidean algorithm
    private int gcd(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }
    
    // Helper method to find modular multiplicative inverse
    private int findModularMultiplicativeInverse(int a) {
        for (int x = 1; x < ALPHABET_SIZE; x++) {
            if ((a * x) % ALPHABET_SIZE == 1) {
                return x;
            }
        }
        throw new IllegalArgumentException("No modular multiplicative inverse exists for " + a);
    }
    
    // Helper method to group text into chunks of 5 characters
    private String groupIntoFive(String text) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            result.append(text.charAt(i));
            if (i % 5 == 4 && i < text.length() - 1) {
                result.append(" ");
            }
        }
        return result.toString();
    }
}
