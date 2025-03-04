class ResistorColorTrio {
    private static final String[] COLOR_CODES = {
        "black", "brown", "red", "orange", "yellow", 
        "green", "blue", "violet", "grey", "white"
    };

    String label(String[] colors) {
        if (colors == null || colors.length < 3) {
            throw new IllegalArgumentException("Exactly 3 colors are required");
        }

        // Validate individual colors
        for (String color : colors) {
            if (color == null) {
                throw new IllegalArgumentException("Color cannot be null");
            }
        }

        // Get the value for each color with bounds checking
        int firstDigit = getColorValue(colors[0]);
        int secondDigit = getColorValue(colors[1]);
        int zeros = getColorValue(colors[2]);

        // Validate the multiplier (zeros) to prevent overflow
        if (zeros < 0 || zeros > 9) {
            throw new IllegalArgumentException("Multiplier must be between 0 and 9");
        }

        // Calculate the resistance value with overflow checking
        long baseValue = (firstDigit * 10L) + secondDigit;
        if (baseValue < 0) {
            throw new IllegalArgumentException("Invalid base value calculation");
        }

        long value;
        try {
            value = Math.multiplyExact(baseValue, (long) Math.pow(10, zeros));
        } catch (ArithmeticException e) {
            throw new IllegalArgumentException("Resistance value too large", e);
        }

        // Determine the appropriate metric prefix
        if (value >= 1_000_000_000L) {
            return (value / 1_000_000_000L) + " gigaohms";
        } else if (value >= 1_000_000L) {
            return (value / 1_000_000L) + " megaohms";
        } else if (value >= 1_000L) {
            return (value / 1_000L) + " kiloohms";
        } else {
            return value + " ohms";
        }
    }

    private int getColorValue(String color) {
        if (color == null) {
            throw new IllegalArgumentException("Color cannot be null");
        }
        
        for (int i = 0; i < COLOR_CODES.length; i++) {
            if (COLOR_CODES[i].equalsIgnoreCase(color)) {
                return i;
            }
        }
        throw new IllegalArgumentException("Invalid color: " + color);
    }
}
