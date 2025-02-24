import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

class VariableLengthQuantity {

    List<String> encode(List<Long> numbers) {
        List<String> result = new ArrayList<>();
        for (Long number : numbers) {
            long num = number;
            List<Byte> chunks = new ArrayList<>();
            if (num == 0) {
                chunks.add((byte)0);
            } else {
                while (num != 0) {
                    byte chunk = (byte)(num & 0x7F);
                    chunks.add(chunk);
                    num >>>= 7;
                }
            }
            Collections.reverse(chunks);
            if (!chunks.isEmpty()) {
                for (int i = 0; i < chunks.size() - 1; i++) {
                    chunks.set(i, (byte)(chunks.get(i) | 0x80));
                }
                for (byte b : chunks) {
                    result.add(String.format("%02X", b & 0xFF));
                }
            }
        }
        return result;
    }

    List<String> decode(List<Long> bytes) {
        List<String> result = new ArrayList<>();
        int index = 0;
        while (index < bytes.size()) {
            long value = 0;
            int shift = 0;
            byte b;
            do {
                if (index >= bytes.size()) {
                    throw new IllegalArgumentException("Incomplete VLQ sequence");
                }
                long byteValue = bytes.get(index++);
                if (byteValue < 0 || byteValue > 255) {
                    throw new IllegalArgumentException("Invalid byte value: " + byteValue);
                }
                b = (byte) byteValue;
                long segment = (b & 0x7F);
                value |= segment << shift;
                shift += 7;
            } while ((b & 0x80) != 0 && shift <= 28);
            
            if (value > 0xFFFFFFFFL) {
                throw new IllegalArgumentException("Value exceeds 32 bits");
            }
            result.add(String.format("%08X", value));
        }
        return result;
    }
}
