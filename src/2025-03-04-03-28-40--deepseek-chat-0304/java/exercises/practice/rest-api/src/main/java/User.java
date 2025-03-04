import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;

/** POJO representing a User in the database. */
public class User {
    private final String name;
    private final List<Iou> owes;
    private final List<Iou> owedBy;
    private final Map<String, Double> owesMap;
    private final Map<String, Double> owedByMap;

    private User(String name, List<Iou> owes, List<Iou> owedBy) {
        this.name = name;
        this.owes = new ArrayList<>(owes);
        this.owedBy = new ArrayList<>(owedBy);
        this.owesMap = new HashMap<>();
        this.owedByMap = new HashMap<>();
        
        for (Iou iou : owes) {
            owesMap.put(iou.name(), owesMap.getOrDefault(iou.name(), 0.0) + iou.amount());
        }
        for (Iou iou : owedBy) {
            owedByMap.put(iou.name(), owedByMap.getOrDefault(iou.name(), 0.0) + iou.amount());
        }
    }

    public String name() {
        return name;
    }

    /** IOUs this user owes to other users. */
    public List<Iou> owes() {
        return unmodifiableList(owes);
    }

    /** IOUs other users owe to this user. */
    public List<Iou> owedBy() {
        return unmodifiableList(owedBy);
    }

    public static Builder builder() {
        return new Builder();
    }

    public double balance() {
        double totalOwed = owesMap.values().stream().mapToDouble(Double::doubleValue).sum();
        double totalOwedBy = owedByMap.values().stream().mapToDouble(Double::doubleValue).sum();
        return totalOwedBy - totalOwed;
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("owes", new JSONObject(owesMap));
        json.put("owed_by", new JSONObject(owedByMap));
        json.put("balance", balance());
        return json;
    }

    public static class Builder {
        private String name;
        private final List<Iou> owes = new ArrayList<>();
        private final List<Iou> owedBy = new ArrayList<>();

        public Builder setName(String name) {
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Name cannot be null or empty");
            }
            this.name = name;
            return this;
        }

        public Builder owes(String name, double amount) {
            owes.add(new Iou(name, amount));
            return this;
        }

        public Builder owedBy(String name, double amount) {
            owedBy.add(new Iou(name, amount));
            return this;
        }

        public User build() {
            return new User(name, owes, owedBy);
        }
    }
}
