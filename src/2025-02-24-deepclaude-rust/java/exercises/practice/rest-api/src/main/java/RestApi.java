import org.json.JSONObject;
import org.json.JSONArray;
import java.util.*;

class RestApi {
    private final Map<String, User> users;

    RestApi(User... users) {
        this.users = new HashMap<>();
        for (User user : users) {
            this.users.put(user.name(), user);
        }
    }

    private JSONObject userToJson(User user) {
        JSONObject json = new JSONObject();
        json.put("name", user.name());

        Map<String, Double> owesMap = new HashMap<>();
        Map<String, Double> owedByMap = new HashMap<>();

        // Calculate total owes per user
        Map<String, Double> totalOwes = new HashMap<>();
        for (User.Iou iou : user.owes()) {
            totalOwes.merge(iou.getName(), iou.getAmount(), Double::sum);
        }

        // Calculate total owedBy per user
        Map<String, Double> totalOwedBy = new HashMap<>();
        for (User.Iou iou : user.owedBy()) {
            totalOwedBy.merge(iou.getName(), iou.getAmount(), Double::sum);
        }

        // Calculate net amounts
        Set<String> allUsers = new HashSet<>();
        allUsers.addAll(totalOwes.keySet());
        allUsers.addAll(totalOwedBy.keySet());

        for (String name : allUsers) {
            double owesAmount = totalOwes.getOrDefault(name, 0.0);
            double owedByAmount = totalOwedBy.getOrDefault(name, 0.0);
            double net = owesAmount - owedByAmount;

            if (net > 0) {
                owesMap.put(name, net);
            } else if (net < 0) {
                owedByMap.put(name, -net);
            }
        }

        // Calculate balance
        double balance = totalOwedBy.values().stream().mapToDouble(Double::doubleValue).sum() 
                      - totalOwes.values().stream().mapToDouble(Double::doubleValue).sum();

        json.put("owes", new JSONObject(owesMap));
        json.put("owed_by", new JSONObject(owedByMap));
        json.put("balance", Math.round(balance * 100.0) / 100.0);  // Round to 2 decimal places

        return json;
    }

    String get(String url) {
        if ("/users".equals(url)) {
            List<User> userList = new ArrayList<>(users.values());
            userList.sort(Comparator.comparing(User::name));
            JSONArray usersJson = new JSONArray();
            for (User user : userList) {
                usersJson.put(userToJson(user));
            }
            return new JSONObject().put("users", usersJson).toString();
        }
        throw new IllegalArgumentException("Unsupported URL: " + url);
    }

    String get(String url, JSONObject payload) {
        if ("/users".equals(url)) {
            JSONArray requestedNames = payload.getJSONArray("users");
            List<User> userList = new ArrayList<>();
            for (int i = 0; i < requestedNames.length(); i++) {
                String name = requestedNames.getString(i);
                User user = users.get(name);
                if (user != null) {
                    userList.add(user);
                }
            }
            userList.sort(Comparator.comparing(User::name));
            JSONArray usersJson = new JSONArray();
            for (User user : userList) {
                usersJson.put(userToJson(user));
            }
            return new JSONObject().put("users", usersJson).toString();
        }
        throw new IllegalArgumentException("Unsupported URL: " + url);
    }

    String post(String url, JSONObject payload) {
        if ("/add".equals(url)) {
            String userName = payload.getString("user");
            if (users.containsKey(userName)) {
                throw new IllegalArgumentException("User already exists: " + userName);
            }
            User newUser = User.builder().setName(userName).build();
            users.put(userName, newUser);
            return userToJson(newUser).toString();
        } else if ("/iou".equals(url)) {
            String lenderName = payload.getString("lender");
            String borrowerName = payload.getString("borrower");
            double amount = payload.getDouble("amount");

            User lender = users.get(lenderName);
            User borrower = users.get(borrowerName);
            
            if (lender == null || borrower == null) {
                throw new IllegalArgumentException("Lender or borrower not found");
            }

            // Update lender (add to their owedBy list)
            User.Builder lenderBuilder = User.builder().setName(lenderName);
            for (User.Iou iou : lender.owedBy()) {
                lenderBuilder.owedBy(iou.getName(), iou.getAmount());
            }
            lenderBuilder.owedBy(borrowerName, amount);
            for (User.Iou iou : lender.owes()) {
                lenderBuilder.owes(iou.getName(), iou.getAmount());
            }
            User newLender = lenderBuilder.build();
            users.put(lenderName, newLender);

            // Update borrower (add to their owes list)
            User.Builder borrowerBuilder = User.builder().setName(borrowerName);
            for (User.Iou iou : borrower.owes()) {
                borrowerBuilder.owes(iou.getName(), iou.getAmount());
            }
            borrowerBuilder.owes(lenderName, amount);
            for (User.Iou iou : borrower.owedBy()) {
                borrowerBuilder.owedBy(iou.getName(), iou.getAmount());
            }
            User newBorrower = borrowerBuilder.build();
            users.put(borrowerName, newBorrower);

            // Return the updated users sorted by name
            List<User> updatedUsers = Arrays.asList(newLender, newBorrower);
            updatedUsers.sort(Comparator.comparing(User::name));
            JSONArray usersJson = new JSONArray();
            for (User user : updatedUsers) {
                usersJson.put(userToJson(user));
            }
            return new JSONObject().put("users", usersJson).toString();
        }
        throw new IllegalArgumentException("Unsupported URL: " + url);
    }

}
