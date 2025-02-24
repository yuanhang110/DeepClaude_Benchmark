class PhoneNumber {
    private String number;

    PhoneNumber(String numberString) {
        if (numberString == null) {
            throw new IllegalArgumentException("Phone number cannot be null");
        }
        
        // Clean the input - remove all non-digits
        String cleaned = numberString.replaceAll("[^0-9]", "");
        
        // Handle 11-digit numbers that start with 1
        if (cleaned.length() == 11 && cleaned.startsWith("1")) {
            cleaned = cleaned.substring(1);
        }
        
        // Validate length
        if (cleaned.length() != 10) {
            throw new IllegalArgumentException("Invalid phone number length");
        }
        
        // Validate area code and exchange code (first and fourth digits must be 2-9)
        if (cleaned.charAt(0) < '2' || cleaned.charAt(0) > '9' ||
            cleaned.charAt(3) < '2' || cleaned.charAt(3) > '9') {
            throw new IllegalArgumentException("Invalid area code or exchange code");
        }
        
        this.number = cleaned;
    }

    String getNumber() {
        return number;
    }

}
