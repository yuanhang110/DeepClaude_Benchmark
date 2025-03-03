import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

class KindergartenGarden {
    private static final List<String> STUDENTS = Arrays.asList(
        "Alice", "Bob", "Charlie", "David",
        "Eve", "Fred", "Ginny", "Harriet",
        "Ileana", "Joseph", "Kincaid", "Larry"
    );
    
    private final String row1;
    private final String row2;

    KindergartenGarden(String garden) {
        String[] rows = garden.split("\n");
        row1 = rows[0];
        row2 = rows[1];
    }

    List<Plant> getPlantsOfStudent(String student) {
        int index = STUDENTS.indexOf(student);
        int startPos = index * 2;
        List<Plant> plants = new ArrayList<>();
        
        // Add plants from first row
        addPlants(row1, startPos, plants);
        
        // Add plants from second row
        addPlants(row2, startPos, plants);
        
        return plants;
    }
    
    private void addPlants(String row, int startPos, List<Plant> plants) {
        int endPos = Math.min(startPos + 2, row.length());
        for (int i = startPos; i < endPos; i++) {
            plants.add(getPlantFromCode(row.charAt(i)));
        }
    }
    
    private Plant getPlantFromCode(char code) {
        switch (code) {
            case 'V': return Plant.VIOLETS;
            case 'R': return Plant.RADISHES;
            case 'C': return Plant.CLOVER;
            case 'G': return Plant.GRASS;
            default: throw new IllegalArgumentException("Unknown plant code: " + code);
        }
    }
    
    enum Plant {
        VIOLETS("Violets"),
        RADISHES("Radishes"),
        CLOVER("Clover"),
        GRASS("Grass");
        
        private final String displayName;
        
        Plant(String displayName) {
            this.displayName = displayName;
        }
        
        @Override
        public String toString() {
            return displayName;
        }
    }
}
