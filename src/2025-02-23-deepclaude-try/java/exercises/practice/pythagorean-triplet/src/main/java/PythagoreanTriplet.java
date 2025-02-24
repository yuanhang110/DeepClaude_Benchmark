import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

class PythagoreanTriplet {
    private final int a;
    private final int b;
    private final int c;

    PythagoreanTriplet(int a, int b, int c) {
        this.a = a;
        this.b = b;
        this.c = c;
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
        return Objects.hash(a, b, c);
    }

    @Override
    public String toString() {
        return String.format("PythagoreanTriplet(%d, %d, %d)", a, b, c);
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
                if (a > maxFactor) break;
                int maxB = (sum - a) / 2;
                for (int b = a + 1; b <= maxB; b++) {
                    if (b > maxFactor) break;
                    int c = sum - a - b;
                    if (c > maxFactor || c <= b) continue;
                    if (a * a + b * b == c * c) {
                        triplets.add(new PythagoreanTriplet(a, b, c));
                    }
                }
            }
            return triplets;
        }
    }

}
