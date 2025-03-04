class ResistorColorTrio {
    String label(String[] colors) {
        int first = getValue(colors[0]);
        int second = getValue(colors[1]);
        int exponent = getValue(colors[2]);

        long mainValue = first * 10L + second;
        long multiplier = 1;
        for (int i = 0; i < exponent; i++) {
            multiplier *= 10;
        }
        long totalOhms = mainValue * multiplier;

        String unit;
        double formattedValue;

        if (totalOhms >= 1_000_000_000L) {
            unit = "gigaohms";
            formattedValue = totalOhms / 1_000_000_000.0;
        } else if (totalOhms >= 1_000_000L) {
            unit = "megaohms";
            formattedValue = totalOhms / 1_000_000.0;
        } else if (totalOhms >= 1_000L) {
            unit = "kiloohms";
            formattedValue = totalOhms / 1_000.0;
        } else {
            unit = "ohms";
            formattedValue = totalOhms;
        }

        if (unit.equals("ohms")) {
            return String.format("%d ohms", (long) formattedValue);
        } else {
            if (formattedValue == (long) formattedValue) {
                return String.format("%d %s", (long) formattedValue, unit);
            } else {
                return String.format("%.1f %s", formattedValue, unit);
            }
        }
    }

    private int getValue(String color) {
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
}
