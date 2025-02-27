import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class VariableLengthQuantity {

    List<Long> encode(List<Long> numbers) {
        List<Long> result = new ArrayList<>();
        for (Long number : numbers) {
            long current = number;
            if (current == 0) {
                result.add(0L);
                continue;
            }
            List<Long> chunks = new ArrayList<>();
            do {
                long chunk = current & 0x7F;
                chunks.add(chunk);
                current >>>= 7;
            } while (current != 0);
            Collections.reverse(chunks);
            for (int i = 0; i < chunks.size(); i++) {
                long b = chunks.get(i);
                if (i != chunks.size() - 1) {
                    b |= 0x80;
                }
                result.add(b);
            }
        }
        return result;
    }

    List<Long> decode(List<Long> bytes) {
        List<Long> result = new ArrayList<>();
        long currentNumber = 0;
        boolean incomplete = false;
        
        for (Long b : bytes) {
            long byteValue = b & 0xFFL;
            currentNumber = (currentNumber << 7) | (byteValue & 0x7FL);
            
            if ((byteValue & 0x80L) != 0) {
                incomplete = true;
            } else {
                if (incomplete) {
                    incomplete = false;
                }
                result.add(currentNumber);
                currentNumber = 0;
            }
        }
        
        if (incomplete) {
            throw new IllegalArgumentException("Incomplete sequence");
        }
        
        return result;
    }
}
