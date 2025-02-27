class ResistorColorTrio {
    private static final String[] COLOR_CODES = {
        "black", "brown", "red", "orange", "yellow", 
        "green", "blue", "violet", "grey", "white"
    };

    private int getValue(String color) {
        for (int i = 0; i < COLOR_CODES.length; i++) {
            if (COLOR_CODES[i].equals(color)) {
                return i;
            }
        }
        throw new IllegalArgumentException("Invalid color: " + color);
    }

    String label(String[] colors) {
        int first = getValue(colors[0]);
        int second = getValue(colors[1]);
        int exponent = getValue(colors[2]);

        long mainValue = (long) first * 10 + second;
        long multiplier = (long) Math.pow(10, exponent);

        long total = mainValue * multiplier;

        String suffix = "ohms";
        long divisor = 1;

        if (total >= 1_000_000_000L && total % 1_000_000_000L == 0) {
            divisor = 1_000_000_000L;
            suffix = "giga" + suffix;
        } else if (total >= 1_000_000L && total % 1_000_000L == 0) {
            divisor = 1_000_000L;
            suffix = "mega" + suffix;
        } else if (total >= 1_000L && total % 1_000L == 0) {
            divisor = 1_000L;
            suffix = "kilo" + suffix;
        }

        long value = total / divisor;

        return String.format("%d %s", value, suffix);
    }
}
