class ResistorColorTrio {
    String label(String[] colors) {
        int first = colorCode(colors[0]);
        int second = colorCode(colors[1]);
        int exponent = colorCode(colors[2]);
        
        long[] multipliers = {1L, 10L, 100L, 1000L, 10_000L, 100_000L, 1_000_000L, 10_000_000L, 100_000_000L, 1_000_000_000L};
        long mainValue = (first * 10L) + second;
        long totalOhms = mainValue * multipliers[exponent];
        
        if (totalOhms >= 1_000_000_000L && totalOhms % 1_000_000_000L == 0) {
            return (totalOhms / 1_000_000_000L) + " gigaohms";
        } else if (totalOhms >= 1_000_000L && totalOhms % 1_000_000L == 0) {
            return (totalOhms / 1_000_000L) + " megaohms";
        } else if (totalOhms >= 1_000L && totalOhms % 1_000L == 0) {
            return (totalOhms / 1_000L) + " kiloohms";
        } else {
            return totalOhms + " ohms";
        }
    }
    
    private int colorCode(String color) {
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
