import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class BookStore {

    public double calculateBasketCost(List<Integer> books) {
        int[] counts = new int[5];
        for (int book : books) {
            counts[book - 1]++;
        }

        List<Integer> countsList = new ArrayList<>();
        for (int count : counts) {
            countsList.add(count);
        }

        double total = 0.0;
        List<Integer> groups = new ArrayList<>();

        while (true) {
            Collections.sort(countsList, Collections.reverseOrder());

            int groupSize = 0;
            while (groupSize < 5 && countsList.get(groupSize) > 0) {
                groupSize++;
            }

            if (groupSize == 0) {
                break;
            }

            groups.add(groupSize);

            double discount = 0.0;
            switch (groupSize) {
                case 5: discount = 0.25; break;
                case 4: discount = 0.20; break;
                case 3: discount = 0.10; break;
                case 2: discount = 0.05; break;
                case 1: discount = 0.00; break;
            }

            total += groupSize * 8 * (1 - discount);

            for (int i = 0; i < groupSize; i++) {
                countsList.set(i, countsList.get(i) - 1);
            }
        }

        int num5 = Collections.frequency(groups, 5);
        int num3 = Collections.frequency(groups, 3);
        int replacements = Math.min(num5, num3);
        total -= replacements * 0.40;

        return total;
    }

}
