import org.json.JSONObject;

import java.util.*;
import org.json.JSONObject;
import org.json.JSONArray;

class RestApi {
    private final Map<String, User> users = new HashMap<>();

    RestApi(User... initialUsers) {
        for (User user : initialUsers) {
            users.put(user.name(), user);
        }
    }

    String get(String url) {
        if ("/users".equals(url)) {
            return getUsersResponse(new ArrayList<>(users.values()));
        }
        throw new IllegalArgumentException("Unknown URL: " + url);
    }

    String get(String url, JSONObject payload) {
        if ("/users".equals(url) && payload.has("users")) {
            JSONArray userNames = payload.getJSONArray("users");
            List<User> requestedUsers = new ArrayList<>();
            for (int i = 0; i < userNames.length(); i++) {
                String name = userNames.getString(i);
                if (users.containsKey(name)) {
                    requestedUsers.add(users.get(name));
                }
            }
            return getUsersResponse(requestedUsers);
        }
        throw new IllegalArgumentException("Invalid request");
    }

    String post(String url, JSONObject payload) {
        if ("/add".equals(url)) {
            String name = payload.getString("user");
            if (users.containsKey(name)) {
                return createUserJson(users.get(name)).toString();
            }
            User newUser = User.builder().setName(name).build();
            users.put(name, newUser);
            return createUserJson(newUser).toString();
        }
        
        if ("/iou".equals(url)) {
            String lender = payload.getString("lender");
            String borrower = payload.getString("borrower");
            double amount = payload.getDouble("amount");
            
            User lenderUser = users.get(lender);
            User borrowerUser = users.get(borrower);
            
            User.Builder lenderBuilder = User.builder().setName(lender);
            User.Builder borrowerBuilder = User.builder().setName(borrower);
            
            // Copy existing IOUs
            for (Iou iou : lenderUser.owes()) {
                lenderBuilder.owes(iou.name(), iou.amount());
            }
            for (Iou iou : lenderUser.owedBy()) {
                lenderBuilder.owedBy(iou.name(), iou.amount());
            }
            for (Iou iou : borrowerUser.owes()) {
                borrowerBuilder.owes(iou.name(), iou.amount());
            }
            for (Iou iou : borrowerUser.owedBy()) {
                borrowerBuilder.owedBy(iou.name(), iou.amount());
            }
            
            // Add new IOU
            lenderBuilder.owedBy(borrower, amount);
            borrowerBuilder.owes(lender, amount);
            
            User updatedLender = lenderBuilder.build();
            User updatedBorrower = borrowerBuilder.build();
            
            users.put(lender, updatedLender);
            users.put(borrower, updatedBorrower);
            
            List<User> updatedUsers = Arrays.asList(updatedLender, updatedBorrower);
            Collections.sort(updatedUsers, Comparator.comparing(User::name));
            return getUsersResponse(updatedUsers);
        }
        
        throw new IllegalArgumentException("Unknown URL: " + url);
    }

    private String getUsersResponse(List<User> userList) {
        JSONObject response = new JSONObject();
        JSONArray usersArray = new JSONArray();
        
        Collections.sort(userList, Comparator.comparing(User::name));
        
        for (User user : userList) {
            usersArray.put(createUserJson(user));
        }
        
        response.put("users", usersArray);
        return response.toString();
    }

    private JSONObject createUserJson(User user) {
        JSONObject userJson = new JSONObject();
        userJson.put("name", user.name());
        
        JSONObject owes = new JSONObject();
        for (Iou iou : user.owes()) {
            owes.put(iou.name(), iou.amount());
        }
        userJson.put("owes", owes);
        
        JSONObject owedBy = new JSONObject();
        for (Iou iou : user.owedBy()) {
            owedBy.put(iou.name(), iou.amount());
        }
        userJson.put("owed_by", owedBy);
        
        double balance = user.owedBy().stream().mapToDouble(Iou::amount).sum() -
                        user.owes().stream().mapToDouble(Iou::amount).sum();
        userJson.put("balance", balance);
        
        return userJson;
    }
}
