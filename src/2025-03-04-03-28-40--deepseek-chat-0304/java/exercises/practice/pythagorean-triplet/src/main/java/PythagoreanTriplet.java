import java.util.ArrayList;
import java.util.List;

class PythagoreanTriplet {
    private final int a;
    private final int b;
    private final int c;

    PythagoreanTriplet(int a, int b, int c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    static TripletListBuilder makeTripletsList() {
        return new TripletListBuilder();
    }

    static class TripletListBuilder {
        private int sum;
        private int maxFactor = Integer.MAX_VALUE;

        TripletListBuilder thatSumTo(int sum) {
            this.sum = sum;
            return this;
        }

        TripletListBuilder withFactorsLessThanOrEqualTo(int maxFactor) {
            this.maxFactor = maxFactor;
            return this;
        }

        List<PythagoreanTriplet> build() {
            List<PythagoreanTriplet> triplets = new ArrayList<>();
            
            for (int a = 1; a <= sum / 3; a++) {
                for (int b = a + 1; b <= (sum - a) / 2; b++) {
                    int c = sum - a - b;
                    if (c > b && c <= maxFactor && isPythagoreanTriplet(a, b, c)) {
                        triplets.add(new PythagoreanTriplet(a, b, c));
                    }
                }
            }
            
            return triplets;
        }

        private boolean isPythagoreanTriplet(int a, int b, int c) {
            return a * a + b * b == c * c;
        }
    }
}
