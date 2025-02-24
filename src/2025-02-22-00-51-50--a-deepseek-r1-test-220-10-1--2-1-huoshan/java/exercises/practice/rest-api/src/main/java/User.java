import java.util.*;
import static java.util.Collections.unmodifiableMap;
import org.json.JSONObject;

/** POJO representing a User in the database. */
public class User {
    private final String name;
    private final Map<String, Double> owes;
    private final Map<String, Double> owedBy;

    private User(String name, Map<String, Double> owes, Map<String, Double> owedBy) {
        this.name = name;
        this.owes = new HashMap<>(owes);
        this.owedBy = new HashMap<>(owedBy);
    }

    public String name() {
        return name;
    }

    /** IOUs this user owes to other users. */
    public Map<String, Double> owes() {
        return unmodifiableMap(owes);
    }

    /** IOUs other users owe to this user. */
    public Map<String, Double> owedBy() {
        return unmodifiableMap(owedBy);
    }

    public double balance() {
        double owed = owedBy.values().stream().mapToDouble(Double::doubleValue).sum();
        double owesTotal = owes.values().stream().mapToDouble(Double::doubleValue).sum();
        return owed - owesTotal;
    }

    public String toJson() {
        return String.format(
            "{\"name\":\"%s\",\"owes\":%s,\"owed_by\":%s,\"balance\":%.1f}",
            name,
            formatMap(owes),
            formatMap(owedBy),
            balance()
        );
    }

    private String formatMap(Map<String, Double> map) {
        if (map.isEmpty()) return "{}";
        StringBuilder sb = new StringBuilder("{");
        for (Map.Entry<String, Double> entry : map.entrySet()) {
            sb.append(String.format("\"%s\":%.1f,", entry.getKey(), entry.getValue()));
        }
        sb.setLength(sb.length() - 1);  // Remove trailing comma
        sb.append("}");
        return sb.toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String name;
        private final Map<String, Double> owes = new HashMap<>();
        private final Map<String, Double> owedBy = new HashMap<>();

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder owes(String name, double amount) {
            owes.merge(name, amount, Double::sum);
            return this;
        }

        public Builder owedBy(String name, double amount) {
            owedBy.merge(name, amount, Double::sum);
            return this;
        }

        public User build() {
            return new User(name, owes, owedBy);
        }
    }
}
