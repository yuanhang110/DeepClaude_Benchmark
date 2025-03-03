import static java.util.Collections.unmodifiableMap;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

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

    /** Amounts this user owes to other users. */
    public Map<String, Double> owes() {
        return unmodifiableMap(owes);
    }

    /** Amounts other users owe to this user. */
    public Map<String, Double> owedBy() {
        return unmodifiableMap(owedBy);
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

        public Builder owes(Map<String, Double> owes) {
            this.owes.clear();
            this.owes.putAll(owes);
            return this;
        }

        public Builder owedBy(Map<String, Double> owedBy) {
            this.owedBy.clear();
            this.owedBy.putAll(owedBy);
            return this;
        }

        public User build() {
            return new User(name, owes, owedBy);
        }
    }
}
