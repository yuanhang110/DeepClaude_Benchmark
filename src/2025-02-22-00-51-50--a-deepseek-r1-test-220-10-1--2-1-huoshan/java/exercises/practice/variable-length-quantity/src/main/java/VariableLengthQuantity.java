import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class VariableLengthQuantity {

    List<String> encode(List<Long> numbers) {
        List<String> result = new ArrayList<>();
        for (Long number : numbers) {
            if (number < 0 || number > 0xFFFFFFFFL) {
                throw new IllegalArgumentException("Number out of 32-bit range");
            }
            
            List<Long> chunks = new ArrayList<>();
            long num = number;
            do {
                chunks.add(num & 0x7F);
                num >>>= 7;
            } while (num != 0);
            
            Collections.reverse(chunks);
            
            for (int i = 0; i < chunks.size(); i++) {
                long chunk = chunks.get(i);
                if (i != chunks.size() - 1) {
                    chunk |= 0x80;
                }
                result.add(String.format("%02X", chunk));
            }
        }
        return result;
    }

    List<String> decode(List<Long> bytes) {
        List<String> result = new ArrayList<>();
        long current = 0;
        int count = 0;
        
        for (Long b : bytes) {
            if (b < 0 || b > 0xFF) {
                throw new IllegalArgumentException("Invalid byte value");
            }
            
            current = (current << 7) | (b & 0x7F);
            count++;
            
            if ((b & 0x80) == 0) {
                if (count > 5 || (count == 5 && current > 0xFFFF_FFFFL)) {
                    throw new IllegalArgumentException("Overflow 32-bit unsigned");
                }
                
                result.add(String.format("%08X", current));
                current = 0;
                count = 0;
            }
        }
        
        if (count > 0) {
            throw new IllegalArgumentException("Incomplete sequence");
        }
        
        return result;
    }
}
