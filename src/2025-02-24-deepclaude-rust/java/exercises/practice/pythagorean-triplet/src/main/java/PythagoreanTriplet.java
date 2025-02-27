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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PythagoreanTriplet that = (PythagoreanTriplet) o;
        return a == that.a && b == that.b && c == that.c;
    }

    @Override
    public int hashCode() {
        return a * 31 * 31 + b * 31 + c;
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
                    if (c > b && (long)a * a + (long)b * b == (long)c * c) {
                        if (a <= maxFactor && b <= maxFactor && c <= maxFactor) {
                            triplets.add(new PythagoreanTriplet(a, b, c));
                        }
                    }
                }
            }
            return triplets;
        }
    }

}
