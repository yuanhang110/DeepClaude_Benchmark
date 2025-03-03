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
            
            // Find all Pythagorean triplets (a,b,c) where a+b+c = sum
            for (int a = 1; a <= Math.min((sum - 3) / 3, maxFactor); a++) {
                for (int b = a + 1; b <= Math.min((sum - a - 1) / 2, maxFactor); b++) {
                    int c = sum - a - b;
                    if (c > b && c <= maxFactor && (long)a * a + (long)b * b == (long)c * c) {
                        triplets.add(new PythagoreanTriplet(a, b, c));
                    }
                }
            }
            
            return triplets;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof PythagoreanTriplet)) return false;
        
        PythagoreanTriplet other = (PythagoreanTriplet) obj;
        return this.a == other.a && this.b == other.b && this.c == other.c;
    }
    
    @Override
    public int hashCode() {
        return a * 31 * 31 + b * 31 + c;
    }
}
