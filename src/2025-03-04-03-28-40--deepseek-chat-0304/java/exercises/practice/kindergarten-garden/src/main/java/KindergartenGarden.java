import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;

class KindergartenGarden {
    
    private static final String[] STUDENTS = {
        "Alice", "Bob", "Charlie", "David",
        "Eve", "Fred", "Ginny", "Harriet",
        "Ileana", "Joseph", "Kincaid", "Larry"
    };
    
    private static final Map<Character, Plant> PLANT_MAP = Map.of(
        'G', Plant.GRASS,
        'C', Plant.CLOVER,
        'R', Plant.RADISHES,
        'V', Plant.VIOLETS
    );
    
    private Map<String, List<Plant>> studentPlants;

    KindergartenGarden(String garden) {
        this(garden, STUDENTS);
    }

    KindergartenGarden(String garden, String[] students) {
        if (garden == null || students == null) {
            throw new IllegalArgumentException("Garden and students cannot be null");
        }

        studentPlants = new HashMap<>();
        String[] rows = garden.split("\n");
        
        // Ensure we have exactly two rows
        if (rows.length != 2) {
            throw new IllegalArgumentException("Garden must have exactly two rows");
        }

        // Sort students alphabetically
        Arrays.sort(students);
        
        for (int i = 0; i < students.length; i++) {
            List<Plant> plants = new ArrayList<>();
            
            // Calculate positions
            int pos1 = i * 2;
            int pos2 = i * 2 + 1;
            
            // Add plants from first row if positions are valid
            if (pos1 < rows[0].length() && pos2 < rows[0].length()) {
                plants.add(PLANT_MAP.getOrDefault(rows[0].charAt(pos1), Plant.GRASS));
                plants.add(PLANT_MAP.getOrDefault(rows[0].charAt(pos2), Plant.GRASS));
            }
            
            // Add plants from second row if positions are valid
            if (pos1 < rows[1].length() && pos2 < rows[1].length()) {
                plants.add(PLANT_MAP.getOrDefault(rows[1].charAt(pos1), Plant.GRASS));
                plants.add(PLANT_MAP.getOrDefault(rows[1].charAt(pos2), Plant.GRASS));
            }
            
            studentPlants.put(students[i], plants);
        }
    }

    List<Plant> getPlantsOfStudent(String student) {
        if (student == null) {
            throw new IllegalArgumentException("Student cannot be null");
        }
        return studentPlants.getOrDefault(student, List.of());
    }
}
