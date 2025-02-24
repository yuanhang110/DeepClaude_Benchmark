import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class KindergartenGarden {
    private static final List<String> STUDENTS = Collections.unmodifiableList(
        Arrays.asList(
            "Alice", "Bob", "Charlie", "David",
            "Eve", "Fred", "Ginny", "Harriet",
            "Ileana", "Joseph", "Kincaid", "Larry"
        )
    );
    
    private final Map<String, List<Plant>> plantsMap = new HashMap<>();

    KindergartenGarden(String garden) {
        String[] rows = garden.split("\n");
        if (rows.length != 2) {
            throw new IllegalArgumentException("Garden must have exactly two rows");
        }
        
        List<String> row1Chunks = splitRow(rows[0]);
        List<String> row2Chunks = splitRow(rows[1]);
        
        for (int i = 0; i < STUDENTS.size(); i++) {
            String student = STUDENTS.get(i);
            String plantsStr = row1Chunks.get(i) + row2Chunks.get(i);
            List<Plant> plants = new ArrayList<>();
            for (char c : plantsStr.toCharArray()) {
                plants.add(Plant.fromCode(c));
            }
            plantsMap.put(student, plants);
        }
    }

    List<Plant> getPlantsOfStudent(String student) {
        return plantsMap.getOrDefault(student, Collections.emptyList());
    }

    private List<String> splitRow(String row) {
        List<String> chunks = new ArrayList<>();
        for (int i = 0; i < row.length(); i += 2) {
            int end = Math.min(i + 2, row.length());
            chunks.add(row.substring(i, end));
        }
        return chunks;
    }
}
