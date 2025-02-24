import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class BaseConverter {
    private final int originalBase;
    private final int[] originalDigits;
    private long decimalValue;

    BaseConverter(int originalBase, int[] originalDigits) {
        if (originalBase < 2) {
            throw new IllegalArgumentException("Base must be at least 2");
        }
        if (originalDigits.length == 0) {
            throw new IllegalArgumentException("Digits cannot be empty");
        }
        if (originalDigits.length > 1 && originalDigits[0] == 0) {
            throw new IllegalArgumentException("Digits cannot have leading zeros");
        }
        for (int digit : originalDigits) {
            if (digit < 0 || digit >= originalBase) {
                throw new IllegalArgumentException("Invalid digit for base");
            }
        }
        this.originalBase = originalBase;
        this.originalDigits = originalDigits.clone();

        long value = 0;
        for (int d : this.originalDigits) {
            value = value * this.originalBase + d;
        }
        this.decimalValue = value;
    }

    int[] convertToBase(int newBase) {
        if (newBase < 2) {
            throw new IllegalArgumentException("New base must be at least 2");
        }
        if (decimalValue == 0) {
            return new int[]{0};
        }
        List<Integer> digits = new ArrayList<>();
        long value = decimalValue;
        while (value > 0) {
            int remainder = (int)(value % newBase);
            digits.add(remainder);
            value = value / newBase;
        }
        Collections.reverse(digits);
        return digits.stream().mapToInt(i -> i).toArray();
    }

}
