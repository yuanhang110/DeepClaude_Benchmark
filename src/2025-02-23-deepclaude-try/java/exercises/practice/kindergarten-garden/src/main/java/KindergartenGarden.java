import java.util.*;

class KindergartenGarden {
    private static final List<String> STUDENTS = Arrays.asList(
        "Alice", "Bob", "Charlie", "David",
        "Eve", "Fred", "Ginny", "Harriet",
        "Ileana", "Joseph", "Kincaid", "Larry"
    );
    
    private final Map<String, List<Plant>> plantsByStudent;
    
    KindergartenGarden(String garden) {
        String[] rows = garden.split("\n");
        String firstRow = rows[0];
        String secondRow = rows[1];
        
        plantsByStudent = new HashMap<>();
        
        for (int i = 0; i < STUDENTS.size(); i++) {
            List<Plant> plants = new ArrayList<>();
            plants.add(getPlant(firstRow.charAt(2 * i)));
            plants.add(getPlant(firstRow.charAt(2 * i + 1)));
            plants.add(getPlant(secondRow.charAt(2 * i)));
            plants.add(getPlant(secondRow.charAt(2 * i + 1)));
            plantsByStudent.put(STUDENTS.get(i), plants);
        }
    }
    
    private Plant getPlant(char c) {
        switch(c) {
            case 'G': return Plant.Grass;
            case 'C': return Plant.Clover;
            case 'R': return Plant.Radish;
            case 'V': return Plant.Violet;
            default: throw new IllegalArgumentException("Invalid plant code: " + c);
        }
    }

    List<Plant> getPlantsOfStudent(String student) {
        return plantsByStudent.getOrDefault(student, List.of());
    }

}
