import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class RestApi {
    private final Map<String, User> users;

    RestApi(User... users) {
        this.users = new HashMap<>();
        for (User user : users) {
            this.users.put(user.name(), user);
        }
    }

    String get(String url) {
        if ("/users".equals(url)) {
            return getAllUsers();
        }
        return "";
    }

    String get(String url, JSONObject payload) {
        if ("/users".equals(url)) {
            return getSpecificUsers(payload);
        }
        return "";
    }

    String post(String url, JSONObject payload) {
        if ("/add".equals(url)) {
            return addUser(payload);
        } else if ("/iou".equals(url)) {
            return addIou(payload);
        }
        return "";
    }

    private String getAllUsers() {
        List<User> userList = new ArrayList<>(users.values());
        userList.sort(Comparator.comparing(User::name));
        return toUsersJson(userList);
    }

    private String getSpecificUsers(JSONObject payload) {
        List<String> requestedNames = payload.getJSONArray("users").toList()
            .stream().map(Object::toString).collect(Collectors.toList());
        
        List<User> userList = users.values().stream()
            .filter(user -> requestedNames.contains(user.name()))
            .sorted(Comparator.comparing(User::name))
            .collect(Collectors.toList());
            
        return toUsersJson(userList);
    }

    private String addUser(JSONObject payload) {
        String name = payload.getString("user");
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("User name cannot be null or empty");
        }
        if (users.containsKey(name)) {
            throw new IllegalArgumentException("User already exists");
        }
        User newUser = User.builder().setName(name).build();
        users.put(name, newUser);
        return newUser.toJson().toString();
    }

    private String addIou(JSONObject payload) {
        String lender = payload.getString("lender");
        String borrower = payload.getString("borrower");
        double amount = payload.getDouble("amount");

        if (lender == null || borrower == null) {
            throw new IllegalArgumentException("Lender and borrower cannot be null");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        if (!users.containsKey(lender)) {
            throw new IllegalArgumentException("Lender does not exist");
        }
        if (!users.containsKey(borrower)) {
            throw new IllegalArgumentException("Borrower does not exist");
        }

        User lenderUser = users.get(lender);
        User borrowerUser = users.get(borrower);

        // Update lender's owedBy
        User updatedLender = User.builder()
            .setName(lender)
            .owedBy(borrower, amount)
            .build();

        // Update borrower's owes
        User updatedBorrower = User.builder()
            .setName(borrower)
            .owes(lender, amount)
            .build();

        // Merge with existing data
        users.put(lender, mergeUsers(lenderUser, updatedLender));
        users.put(borrower, mergeUsers(borrowerUser, updatedBorrower));

        List<User> result = List.of(users.get(lender), users.get(borrower));
        result.sort(Comparator.comparing(User::name));
        return toUsersJson(result);
    }

    private User mergeUsers(User existing, User updates) {
        User.Builder builder = User.builder().setName(existing.name());
        
        // Add existing owes
        for (Iou iou : existing.owes()) {
            builder.owes(iou.name(), iou.amount());
        }
        // Add update owes
        for (Iou iou : updates.owes()) {
            builder.owes(iou.name(), iou.amount());
        }
        
        // Add existing owedBy
        for (Iou iou : existing.owedBy()) {
            builder.owedBy(iou.name(), iou.amount());
        }
        // Add update owedBy
        for (Iou iou : updates.owedBy()) {
            builder.owedBy(iou.name(), iou.amount());
        }
        
        return builder.build();
    }

    private String toUsersJson(List<User> users) {
        JSONObject result = new JSONObject();
        result.put("users", users.stream()
            .map(User::toJson)
            .collect(Collectors.toList()));
        return result.toString();
    }

}
