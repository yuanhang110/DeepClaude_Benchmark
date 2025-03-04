class PhoneNumber {
    private final String number;

    PhoneNumber(String numberString) {
        if (numberString == null) {
            throw new IllegalArgumentException("Phone number cannot be null");
        }

        // Remove all non-digit characters
        String cleaned = numberString.replaceAll("\\D", "");
        
        // Handle country code
        if (cleaned.length() == 11 && cleaned.startsWith("1")) {
            cleaned = cleaned.substring(1);
        }
        
        // Validate length
        if (cleaned.length() != 10) {
            throw new IllegalArgumentException("Number must be 10 digits after removing country code and non-digit characters");
        }
        
        // Validate NXX patterns
        char areaCodeFirst = cleaned.charAt(0);
        char exchangeCodeFirst = cleaned.charAt(3);
        if (areaCodeFirst < '2' || areaCodeFirst > '9') {
            throw new IllegalArgumentException("Area code must start with digits 2-9");
        }
        if (exchangeCodeFirst < '2' || exchangeCodeFirst > '9') {
            throw new IllegalArgumentException("Exchange code must start with digits 2-9");
        }
        
        this.number = cleaned;
    }

    String getNumber() {
        return number;
    }
}
