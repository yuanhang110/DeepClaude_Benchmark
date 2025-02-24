import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class BaseConverter {
    private final long decimalValue;

    BaseConverter(int originalBase, int[] originalDigits) {
        if (originalBase < 2) {
            throw new IllegalArgumentException("Base must be at least 2");
        }
        if (originalDigits.length == 0) {
            throw new IllegalArgumentException("Digits array cannot be empty");
        }
        
        // Check all digits are valid
        boolean allZero = true;
        long leadingDigit = 0;
        for (int i = 0; i < originalDigits.length; i++) {
            int digit = originalDigits[i];
            if (digit < 0 || digit >= originalBase) {
                throw new IllegalArgumentException("All digits must be >= 0 and < " + originalBase);
            }
            if (digit != 0) {
                allZero = false;
                if (i == 0) {
                    leadingDigit = digit;
                }
            }
        }
        
        // Handle zero case
        if (allZero) {
            decimalValue = 0;
            return;
        }
        
        // Check if the number might be too large
        if (originalDigits.length > 64 || 
            (leadingDigit > 0 && originalDigits.length * Math.log(originalBase) / Math.log(2) > 63)) {
            throw new IllegalArgumentException("Number is too large");
        }
        
        // Calculate decimal value
        long value = 0;
        long power = 1;
        for (int i = originalDigits.length - 1; i >= 0; i--) {
            // Check for overflow before multiplication and addition
            if (power > Long.MAX_VALUE / originalBase || 
                value > Long.MAX_VALUE - originalDigits[i] * power) {
                throw new IllegalArgumentException("Number is too large");
            }
            value += originalDigits[i] * power;
            power *= originalBase;
        }
        decimalValue = value;
    }

    int[] convertToBase(int newBase) {
        if (newBase < 2) {
            throw new IllegalArgumentException("New base must be at least 2");
        }
        
        if (decimalValue == 0) {
            return new int[]{0};
        }
        
        List<Integer> digits = new ArrayList<>();
        long num = decimalValue;
        while (num > 0) {
            digits.add((int)(num % newBase));
            num /= newBase;
        }
        
        Collections.reverse(digits);
        return digits.stream().mapToInt(i -> i).toArray();
    }

}
