import org.json.JSONObject;
import java.util.*;
import java.util.stream.Collectors;

class RestApi {
    private final Map<String, User> users;

    RestApi(User... users) {
        this.users = new TreeMap<>();
        for (User user : users) {
            this.users.put(user.name(), user);
        }
    }

    String get(String url) {
        if ("/users".equals(url)) {
            return getUsersJson(Collections.emptyList());
        }
        throw new IllegalArgumentException("Unknown endpoint: " + url);
    }

    String get(String url, JSONObject payload) {
        if ("/users".equals(url)) {
            List<String> requestedUsers = payload.getJSONArray("users").toList().stream()
                .map(Object::toString)
                .collect(Collectors.toList());
            return getUsersJson(requestedUsers);
        }
        throw new IllegalArgumentException("Unknown endpoint: " + url);
    }

    String post(String url, JSONObject payload) {
        if ("/add".equals(url)) {
            String userName = payload.getString("user");
            if (users.containsKey(userName)) {
                throw new IllegalArgumentException("User already exists: " + userName);
            }
            User newUser = User.builder().setName(userName).build();
            users.put(userName, newUser);
            return String.format("{\"users\":[%s]}", newUser.toJson());
        }
        
        if ("/iou".equals(url)) {
            String lender = payload.getString("lender");
            String borrower = payload.getString("borrower");
            double amount = payload.getDouble("amount");
            
            User lenderUser = updateUserIOU(lender, borrower, amount, true);
            User borrowerUser = updateUserIOU(borrower, lender, amount, false);
            
            List<User> updatedUsers = Arrays.asList(lenderUser, borrowerUser);
            Collections.sort(updatedUsers, Comparator.comparing(User::name));
            
            String usersJson = updatedUsers.stream()
                .map(User::toJson)
                .collect(Collectors.joining(","));
            return String.format("{\"users\":[%s]}", usersJson);
        }
        
        throw new IllegalArgumentException("Unknown endpoint: " + url);
    }

    private String getUsersJson(List<String> requestedUsers) {
        List<User> usersList = requestedUsers.isEmpty() ? 
            new ArrayList<>(users.values()) : 
            requestedUsers.stream()
                .map(users::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        
        Collections.sort(usersList, Comparator.comparing(User::name));
        
        String usersJson = usersList.stream()
            .map(User::toJson)
            .collect(Collectors.joining(","));
        return String.format("{\"users\":[%s]}", usersJson);
    }

    private User updateUserIOU(String userName, String otherUser, double amount, boolean isLender) {
        User existing = users.get(userName);
        User.Builder builder = User.builder()
            .setName(userName)
            .owedBy(existing.owedBy())
            .owes(existing.owes());

        if (isLender) {
            builder.owedBy(otherUser, amount);
        } else {
            builder.owes(otherUser, amount);
        }
        
        // Update inverse relationships
        User updated = builder.build();
        users.put(userName, updated);
        return updated;
    }

}
