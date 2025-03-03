import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class VariableLengthQuantity {

    List<Long> encode(List<Long> numbers) {
        List<Long> result = new ArrayList<>();
        for (long number : numbers) {
            long num = number & 0xFFFFFFFFL; // Ensure it's a 32-bit unsigned integer
            if (num == 0) {
                result.add(0L);
                continue;
            }
            
            List<Integer> chunks = new ArrayList<>();
            long temp = num;
            do {
                chunks.add((int)(temp & 0x7F));
                temp = temp >> 7;
            } while (temp != 0);
            
            Collections.reverse(chunks);
            
            for (int i = 0; i < chunks.size(); i++) {
                int chunk = chunks.get(i);
                if (i != chunks.size() - 1) {
                    chunk |= 0x80; // Set the continuation bit for all bytes except the last
                }
                result.add((long)(chunk & 0xFF));
            }
        }
        return result;
    }

    List<Long> decode(List<Long> bytes) {
        List<Long> result = new ArrayList<>();
        long current = 0;
        
        for (long b : bytes) {
            int byteVal = (int)(b & 0xFF);
            current = (current << 7) | (byteVal & 0x7F);
            
            if ((byteVal & 0x80) == 0) {
                // This is the last byte of the current number
                result.add(current & 0xFFFFFFFFL);
                current = 0;
            }
        }
        
        return result;
    }
}
