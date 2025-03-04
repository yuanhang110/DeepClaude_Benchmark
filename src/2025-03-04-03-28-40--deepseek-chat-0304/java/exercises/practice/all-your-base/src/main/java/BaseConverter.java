class BaseConverter {
    private int originalBase;
    private int[] originalDigits;

    BaseConverter(int originalBase, int[] originalDigits) {
        if (originalBase < 2) {
            throw new IllegalArgumentException("Bases must be at least 2.");
        }
        
        if (originalDigits == null || originalDigits.length == 0) {
            this.originalDigits = new int[] {0};
            this.originalBase = originalBase;
            return;
        }
        
        // Check for leading zeros
        if (originalDigits.length > 1 && originalDigits[0] == 0) {
            throw new IllegalArgumentException("Digits may not contain leading zeros.");
        }
        
        for (int digit : originalDigits) {
            if (digit < 0 || digit >= originalBase) {
                throw new IllegalArgumentException("All digits must satisfy 0 <= d < base.");
            }
        }
        
        this.originalBase = originalBase;
        this.originalDigits = originalDigits;
    }

    int[] convertToBase(int newBase) {
        if (newBase < 2) {
            throw new IllegalArgumentException("Bases must be at least 2.");
        }
        
        // Convert original digits to base 10
        int num = 0;
        for (int i = 0; i < originalDigits.length; i++) {
            num = num * originalBase + originalDigits[i];
        }
        
        // Handle special case of zero
        if (num == 0) {
            return new int[] {0};
        }
        
        // Convert base 10 number to target base
        int digits = (int)(Math.log(num) / Math.log(newBase)) + 1;
        int[] result = new int[digits];
        
        for (int i = digits - 1; i >= 0; i--) {
            result[i] = num % newBase;
            num /= newBase;
        }
        
        return result;
    }
}
