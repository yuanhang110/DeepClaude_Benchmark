import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class BookStore {

    double calculateBasketCost(List<Integer> books) {
        int[] counts = new int[5];
        for (int book : books) {
            counts[book - 1]++;
        }

        // Sort counts in descending order
        Arrays.sort(counts);
        reverse(counts);

        List<Integer> groups = new ArrayList<>();
        int[] currentCounts = Arrays.copyOf(counts, counts.length);

        while (true) {
            int groupSize = 0;
            for (int i = 0; i < currentCounts.length; i++) {
                if (currentCounts[i] > 0) {
                    groupSize++;
                } else {
                    break;
                }
            }

            if (groupSize == 0) {
                break;
            }

            groups.add(groupSize);

            // Subtract 1 from the first 'groupSize' elements
            for (int i = 0; i < groupSize; i++) {
                currentCounts[i]--;
            }

            // Re-sort in descending order
            Arrays.sort(currentCounts);
            reverse(currentCounts);
        }

        // Calculate replacements for 5+3 groups
        Map<Integer, Integer> freq = new HashMap<>();
        for (int group : groups) {
            freq.put(group, freq.getOrDefault(group, 0) + 1);
        }

        int numFive = freq.getOrDefault(5, 0);
        int numThree = freq.getOrDefault(3, 0);
        int replacements = Math.min(numFive, numThree);

        // Adjust frequencies
        freq.put(5, numFive - replacements);
        freq.put(3, numThree - replacements);
        freq.put(4, freq.getOrDefault(4, 0) + 2 * replacements);

        // Calculate total cost
        double total = 0.0;
        for (Map.Entry<Integer, Integer> entry : freq.entrySet()) {
            int size = entry.getKey();
            int count = entry.getValue();
            if (count <= 0) {
                continue;
            }
            total += count * calculateGroupCost(size);
        }

        // Round to two decimal places to avoid floating point errors
        return Math.round(total * 100) / 100.0;
    }

    private static void reverse(int[] array) {
        int left = 0;
        int right = array.length - 1;
        while (left < right) {
            int temp = array[left];
            array[left] = array[right];
            array[right] = temp;
            left++;
            right--;
        }
    }

    private static double calculateGroupCost(int groupSize) {
        double discount;
        switch (groupSize) {
            case 1:
                discount = 0.00;
                break;
            case 2:
                discount = 0.05;
                break;
            case 3:
                discount = 0.10;
                break;
            case 4:
                discount = 0.20;
                break;
            case 5:
                discount = 0.25;
                break;
            default:
                discount = 0.00;
        }
        return groupSize * 8 * (1 - discount);
    }
}
