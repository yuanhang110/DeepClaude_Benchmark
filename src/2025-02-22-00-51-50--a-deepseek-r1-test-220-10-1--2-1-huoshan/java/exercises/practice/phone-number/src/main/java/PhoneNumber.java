class PhoneNumber {

    private String number;

    PhoneNumber(String numberString) {
        // Remove all non-digit characters
        String cleaned = numberString.replaceAll("[^0-9]", "");
        
        // Remove optional country code
        if (cleaned.length() == 11 && cleaned.startsWith("1")) {
            cleaned = cleaned.substring(1);
        }
        
        // Validate final length
        if (cleaned.length() != 10) {
            throw new IllegalArgumentException("Invalid phone number length");
        }
        
        // Validate area code (NXX) and exchange code (NXX)
        if (cleaned.charAt(0) == '0' || cleaned.charAt(0) == '1') {
            throw new IllegalArgumentException("Area code cannot start with zero or one");
        }
        if (cleaned.charAt(3) == '0' || cleaned.charAt(3) == '1') {
            throw new IllegalArgumentException("Exchange code cannot start with zero or one");
        }
        this.number = cleaned;
    }

    String getNumber() {
        return number;
    }

}
