import java.util.List;
import java.util.ArrayList;

class KindergartenGarden {
    enum Plant {
        GRASS('G'),
        CLOVER('C'),
        RADISH('R'),
        VIOLET('V');

        private final char code;

        Plant(char code) {
            this.code = code;
        }

        static Plant fromCode(char code) {
            switch(code) {
                case 'G': return GRASS;
                case 'C': return CLOVER;
                case 'R': return RADISH;
                case 'V': return VIOLET;
                default: throw new IllegalArgumentException("Unknown code: " + code);
            }
        }
    }

    private static final List<String> STUDENTS = List.of(
        "Alice", "Bob", "Charlie", "David",
        "Eve", "Fred", "Ginny", "Harriet",
        "Ileana", "Joseph", "Kincaid", "Larry"
    );

    private final List<String> topRow;
    private final List<String> bottomRow;

    KindergartenGarden(String garden) {
        String[] rows = garden.split("\n");
        if (rows.length != 2) {
            throw new IllegalArgumentException("Garden must have exactly two rows");
        }
        this.topRow = splitIntoChunks(rows[0], 2);
        this.bottomRow = splitIntoChunks(rows[1], 2);
    }

    private List<String> splitIntoChunks(String s, int chunkSize) {
        List<String> chunks = new ArrayList<>();
        for (int i = 0; i < s.length(); i += chunkSize) {
            int end = Math.min(i + chunkSize, s.length());
            chunks.add(s.substring(i, end));
        }
        return chunks;
    }

    List<Plant> getPlantsOfStudent(String student) {
        int studentIndex = STUDENTS.indexOf(student);
        if (studentIndex == -1) {
            throw new IllegalArgumentException("Student not found: " + student);
        }
        List<Plant> plants = new ArrayList<>();
        String topChunk = topRow.get(studentIndex);
        String bottomChunk = bottomRow.get(studentIndex);
        for (char c : topChunk.toCharArray()) {
            plants.add(Plant.fromCode(c));
        }
        for (char c : bottomChunk.toCharArray()) {
            plants.add(Plant.fromCode(c));
        }
        return plants;
    }
}
