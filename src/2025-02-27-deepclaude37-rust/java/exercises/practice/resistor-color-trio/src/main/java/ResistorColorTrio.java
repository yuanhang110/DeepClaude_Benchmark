class ResistorColorTrio {
    String label(String[] colors) {
        int firstDigit = colorValue(colors[0]);
        int secondDigit = colorValue(colors[1]);
        int exponent = colorValue(colors[2]);

        long multiplier = calculateMultiplier(exponent);
        long value = (firstDigit * 10L + secondDigit) * multiplier;

        if (value >= 1_000_000_000 && value % 1_000_000_000 == 0) {
            return String.format("%d gigaohms", value / 1_000_000_000);
        } else if (value >= 1_000_000 && value % 1_000_000 == 0) {
            return String.format("%d megaohms", value / 1_000_000);
        } else if (value >= 1_000 && value % 1_000 == 0) {
            return String.format("%d kiloohms", value / 1_000);
        } else {
            return String.format("%d ohms", value);
        }
    }

    private static int colorValue(String color) {
        switch (color) {
            case "black": return 0;
            case "brown": return 1;
            case "red": return 2;
            case "orange": return 3;
            case "yellow": return 4;
            case "green": return 5;
            case "blue": return 6;
            case "violet": return 7;
            case "grey": return 8;
            case "white": return 9;
            default: throw new IllegalArgumentException("Invalid color: " + color);
        }
    }

    private static long calculateMultiplier(int exponent) {
        return (long) Math.pow(10, exponent);
    }
}
