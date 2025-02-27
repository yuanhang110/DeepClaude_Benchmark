import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class BaseConverter {

    private final int decimalValue;

    BaseConverter(int originalBase, int[] originalDigits) {
        if (originalBase < 2) {
            throw new IllegalArgumentException("Original base must be at least 2.");
        }
        if (originalDigits.length == 0) {
            throw new IllegalArgumentException("Digits array cannot be empty.");
        }
        for (int digit : originalDigits) {
            if (digit < 0 || digit >= originalBase) {
                throw new IllegalArgumentException("Digit " + digit + " is invalid for base " + originalBase + ".");
            }
        }
        if (originalDigits.length > 1) {
            if (originalDigits[0] == 0) {
                boolean allZeros = true;
                for (int digit : originalDigits) {
                    if (digit != 0) {
                        allZeros = false;
                        break;
                    }
                }
                if (allZeros) {
                    throw new IllegalArgumentException("Invalid leading zeros. Use a single zero instead.");
                } else {
                    throw new IllegalArgumentException("Leading zeros are not allowed.");
                }
            }
        }
        int decimal = 0;
        for (int digit : originalDigits) {
            decimal = decimal * originalBase + digit;
        }
        decimalValue = decimal;
    }

    int[] convertToBase(int newBase) {
        if (newBase < 2) {
            throw new IllegalArgumentException("New base must be at least 2.");
        }
        if (decimalValue == 0) {
            return new int[]{0};
        }
        List<Integer> digits = new ArrayList<>();
        int current = decimalValue;
        while (current > 0) {
            digits.add(current % newBase);
            current /= newBase;
        }
        Collections.reverse(digits);
        int[] result = new int[digits.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = digits.get(i);
        }
        return result;
    }

}
