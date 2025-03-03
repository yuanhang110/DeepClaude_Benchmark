class PhoneNumber {
    private final String number;

    PhoneNumber(String numberString) {
        // Remove all non-digit characters
        String cleaned = numberString.replaceAll("[^0-9]", "");
        
        // Handle country code if present
        if (cleaned.length() == 11) {
            if (cleaned.startsWith("1")) {
                cleaned = cleaned.substring(1);
            } else {
                throw new IllegalArgumentException("11 digits must start with 1");
            }
        }
        
        // Validate the length
        if (cleaned.length() != 10) {
            throw new IllegalArgumentException("Number must be 10 digits");
        }
        
        // Validate area code (first digit)
        if (cleaned.charAt(0) < '2' || cleaned.charAt(0) > '9') {
            throw new IllegalArgumentException("Area code must start with a digit from 2-9");
        }
        
        // Validate exchange code (fourth digit)
        if (cleaned.charAt(3) < '2' || cleaned.charAt(3) > '9') {
            throw new IllegalArgumentException("Exchange code must start with a digit from 2-9");
        }
        
        this.number = cleaned;
    }

    String getNumber() {
        return number;
    }
}
