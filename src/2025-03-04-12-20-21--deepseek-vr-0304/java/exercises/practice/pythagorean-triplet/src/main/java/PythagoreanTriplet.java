import java.util.List;
import java.util.ArrayList;

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
        private int maxFactor;

        TripletListBuilder thatSumTo(int sum) {
            this.sum = sum;
            this.maxFactor = sum; // default if withFactors is not called
            return this;
        }

        TripletListBuilder withFactorsLessThanOrEqualTo(int maxFactor) {
            this.maxFactor = maxFactor;
            return this;
        }

        List<PythagoreanTriplet> build() {
            List<PythagoreanTriplet> triplets = new ArrayList<>();
            for (int a = 1; a <= maxFactor; a++) {
                for (int b = a + 1; b <= maxFactor; b++) {
                    int c = sum - a - b;
                    if (c > b && c <= maxFactor && a*a + b*b == c*c) {
                        triplets.add(new PythagoreanTriplet(a, b, c));
                    }
                }
            }
            return triplets;
        }

    }

}
