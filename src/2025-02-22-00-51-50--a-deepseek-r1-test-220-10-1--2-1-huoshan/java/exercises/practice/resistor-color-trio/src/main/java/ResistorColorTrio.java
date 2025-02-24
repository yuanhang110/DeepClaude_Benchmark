class ResistorColorTrio {
    String label(String[] colors) {
        int first = colorValue(colors[0]);
        int second = colorValue(colors[1]);
        int exponent = colorValue(colors[2]);
        
        long mainValue = first * 10L + second;
        long totalOhms = mainValue * (long) Math.pow(10, exponent);
        
        if (totalOhms >= 1_000_000_000L && totalOhms % 1_000_000_000L == 0) {
            return String.format("%d gigaohms", totalOhms / 1_000_000_000L);
        } else if (totalOhms >= 1_000_000L && totalOhms % 1_000_000L == 0) {
            return String.format("%d megaohms", totalOhms / 1_000_000L);
        } else if (totalOhms >= 1000L && totalOhms % 1000L == 0) {
            return String.format("%d kiloohms", totalOhms / 1000L);
        }
        return String.format("%d ohms", totalOhms);
    }
    
    private int colorValue(String color) {
        switch (color.toLowerCase()) {
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
