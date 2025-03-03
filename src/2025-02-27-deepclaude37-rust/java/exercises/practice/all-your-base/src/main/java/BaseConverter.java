import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

class BaseConverter {
    private final int originalBase;
    private final int[] originalDigits;

    BaseConverter(int originalBase, int[] originalDigits) {
        if (originalBase < 2) {
            throw new IllegalArgumentException("Bases must be at least 2.");
        }
        if (originalDigits.length == 0) {
            throw new IllegalArgumentException("Digits array cannot be empty.");
        }
        if (originalDigits.length > 1 && originalDigits[0] == 0) {
            throw new IllegalArgumentException("Digits must not have leading zeros.");
        }
        for (int digit : originalDigits) {
            if (digit < 0 || digit >= originalBase) {
                throw new IllegalArgumentException("All digits must be between 0 and base - 1.");
            }
        }
        this.originalBase = originalBase;
        this.originalDigits = Arrays.copyOf(originalDigits, originalDigits.length);
    }

    int[] convertToBase(int newBase) {
        if (newBase < 2) {
            throw new IllegalArgumentException("Bases must be at least 2.");
        }

        // Convert from original base to decimal (base 10)
        long decimal = 0;
        for (int digit : originalDigits) {
            try {
                decimal = Math.addExact(Math.multiplyExact(decimal, originalBase), digit);
            } catch (ArithmeticException e) {
                throw new IllegalArgumentException("Number is too large to convert.", e);
            }
        }

        // Handle zero case
        if (decimal == 0) {
            return new int[]{0};
        }

        // Convert from decimal to new base
        ArrayList<Integer> newDigits = new ArrayList<>();
        while (decimal > 0) {
            newDigits.add((int) (decimal % newBase));
            decimal /= newBase;
        }

        // Reverse since we calculated digits from least to most significant
        Collections.reverse(newDigits);

        // Convert List to array
        int[] result = new int[newDigits.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = newDigits.get(i);
        }

        return result;
    }
}
