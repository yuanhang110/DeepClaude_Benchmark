import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

class VariableLengthQuantity {

    List<String> encode(List<Integer> numbers) {
        List<String> result = new ArrayList<>();
        for (int number : numbers) {
            List<String> encodedBytes = new ArrayList<>();
            if (number == 0) {
                encodedBytes.add("00");
            } else {
                while (number > 0) {
                    byte b = (byte)(number & 0x7F);
                    number >>>= 7;
                    if (!encodedBytes.isEmpty()) {
                        b |= 0x80; // Set continuation bit
                    }
                    encodedBytes.add(String.format("%02X", b));
                }
                Collections.reverse(encodedBytes);
            }
            result.addAll(encodedBytes);
        }
        return result;
    }

    List<String> decode(List<Integer> bytes) {
        List<String> result = new ArrayList<>();
        int number = 0;
        for (int b : bytes) {
            number = (number << 7) | (b & 0x7F);
            if ((b & 0x80) == 0) {
                result.add(String.format("%02X", number));
                number = 0;
            }
        }
        return result;
    }
}
