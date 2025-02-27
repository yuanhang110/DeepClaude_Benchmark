class PhoneNumber {
    private final String number;

    PhoneNumber(String numberString) {
        if (numberString == null || numberString.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone number cannot be null or empty");
        }

        String cleaned = numberString.replaceAll("[^0-9]", "");

        if (cleaned.isEmpty()) {
            throw new IllegalArgumentException("Phone number contains no digits");
        }

        if (cleaned.length() == 11 && cleaned.charAt(0) == '1') {
            cleaned = cleaned.substring(1);
        }

        if (cleaned.length() != 10) {
            throw new IllegalArgumentException("Invalid phone number length");
        }

        if (cleaned.charAt(0) < '2' || cleaned.charAt(0) > '9') {
            throw new IllegalArgumentException("Area code must start with 2-9");
        }

        if (cleaned.charAt(3) < '2' || cleaned.charAt(3) > '9') {
            throw new IllegalArgumentException("Exchange code must start with 2-9");
        }

        this.number = cleaned;
    }

    String getNumber() {
        return number;
    }
}
