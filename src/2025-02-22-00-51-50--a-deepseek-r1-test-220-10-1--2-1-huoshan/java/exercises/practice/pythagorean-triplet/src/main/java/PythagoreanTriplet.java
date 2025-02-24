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
            int a, b, c;
            
            // Using Euclid's formula for generating Pythagorean triples
            for (int m = 2; m < sum/2; m++) {
                for (int n = 1; n < m; n++) {
                    // Generate primitive triple first
                    int aBase = m*m - n*n;
                    int bBase = 2*m*n;
                    int cBase = m*m + n*n;
                    int s = aBase + bBase + cBase;
                    
                    if (s == 0) continue; // Prevent division by zero
                    
                    if (sum % s == 0) {
                        int k = sum / s;
                        a = aBase * k;
                        b = bBase * k;
                        c = cBase * k;
                        
                        // Ensure a < b < c
                        if (a > b) {
                            int temp = a;
                            a = b;
                            b = temp;
                        }
                        
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
