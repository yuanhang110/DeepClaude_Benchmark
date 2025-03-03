import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

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
        throw new UnsupportedOperationException("Invalid endpoint");
    }

    String get(String url, String payloadJson) {
        if ("/users".equals(url)) {
            List<String> requestedNames = new ArrayList<>();
            String usersKey = "\"users\":";
            int usersIndex = payloadJson.indexOf(usersKey);
            if (usersIndex != -1) {
                int arrStart = payloadJson.indexOf('[', usersIndex + usersKey.length());
                int arrEnd = payloadJson.indexOf(']', arrStart);
                if (arrStart != -1 && arrEnd != -1) {
                    String usersArr = payloadJson.substring(arrStart + 1, arrEnd);
                    // Split by commas but not those inside quotes
                    if (!usersArr.isEmpty()) {
                        for (String name : usersArr.split(",")) {
                            name = name.trim();
                            if (name.startsWith("\"") && name.endsWith("\"")) {
                                requestedNames.add(name.substring(1, name.length() - 1));
                            }
                        }
                    }
                }
            }
            return getUsers(requestedNames);
        }
        throw new UnsupportedOperationException("Invalid endpoint");
    }

    String post(String url, String payloadJson) {
        if ("/add".equals(url)) {
            String userKey = "\"user\":";
            int userIndex = payloadJson.indexOf(userKey);
            if (userIndex == -1) {
                throw new IllegalArgumentException("Missing user parameter");
            }
            
            int valueStart = payloadJson.indexOf('"', userIndex + userKey.length());
            int valueEnd = payloadJson.indexOf('"', valueStart + 1);
            
            if (valueStart == -1 || valueEnd == -1) {
                throw new IllegalArgumentException("Invalid user parameter format");
            }
            
            String userName = payloadJson.substring(valueStart + 1, valueEnd);
            if (users.containsKey(userName)) {
                throw new IllegalArgumentException("User already exists");
            }
            User newUser = User.builder().setName(userName).build();
            users.put(userName, newUser);
            return toJson(newUser);
        } else if ("/iou".equals(url)) {
            String lender = extractString(payloadJson, "lender");
            String borrower = extractString(payloadJson, "borrower");
            double amount = extractDouble(payloadJson, "amount");

            if (!users.containsKey(lender) || !users.containsKey(borrower)) {
                throw new IllegalArgumentException("User not found");
            }

            // Process borrower
            User currentBorrower = users.get(borrower);
            Map<String, Double> borrowerOwes = new HashMap<>(currentBorrower.owes());
            Map<String, Double> borrowerOwedBy = new HashMap<>(currentBorrower.owedBy());

            if (borrowerOwedBy.containsKey(lender)) {
                double current = borrowerOwedBy.get(lender);
                double newAmount = current - amount;
                if (newAmount > 0) {
                    borrowerOwedBy.put(lender, newAmount);
                } else {
                    borrowerOwedBy.remove(lender);
                    if (newAmount < 0) {
                        borrowerOwes.put(lender, borrowerOwes.getOrDefault(lender, 0.0) + (-newAmount));
                    }
                }
            } else {
                double currentOwes = borrowerOwes.getOrDefault(lender, 0.0);
                borrowerOwes.put(lender, currentOwes + amount);
            }

            User updatedBorrower = User.builder()
                .setName(borrower)
                .owes(borrowerOwes)
                .owedBy(borrowerOwedBy)
                .build();

            // Process lender
            User currentLender = users.get(lender);
            Map<String, Double> lenderOwes = new HashMap<>(currentLender.owes());
            Map<String, Double> lenderOwedBy = new HashMap<>(currentLender.owedBy());

            if (lenderOwes.containsKey(borrower)) {
                double current = lenderOwes.get(borrower);
                double newAmount = current - amount;
                if (newAmount > 0) {
                    lenderOwes.put(borrower, newAmount);
                } else {
                    lenderOwes.remove(borrower);
                    if (newAmount < 0) {
                        lenderOwedBy.put(borrower, lenderOwedBy.getOrDefault(borrower, 0.0) + (-newAmount));
                    }
                }
            } else {
                double currentOwedBy = lenderOwedBy.getOrDefault(borrower, 0.0);
                lenderOwedBy.put(borrower, currentOwedBy + amount);
            }

            User updatedLender = User.builder()
                .setName(lender)
                .owes(lenderOwes)
                .owedBy(lenderOwedBy)
                .build();

            users.put(borrower, updatedBorrower);
            users.put(lender, updatedLender);

            List<User> responseUsers = new ArrayList<>();
            responseUsers.add(updatedBorrower);
            responseUsers.add(updatedLender);
            responseUsers.sort(Comparator.comparing(User::name));

            StringBuilder response = new StringBuilder();
            response.append("{\"users\":[");
            
            boolean first = true;
            for (User user : responseUsers) {
                if (!first) {
                    response.append(",");
                }
                response.append(toJson(user));
                first = false;
            }
            
            response.append("]}");
            return response.toString();
        }
        throw new UnsupportedOperationException("Invalid endpoint");
    }

    private String getAllUsers() {
        return getUsers(new ArrayList<>(users.keySet()));
    }

    private String getUsers(List<String> userNames) {
        StringBuilder response = new StringBuilder();
        response.append("{\"users\":[");
        
        boolean first = true;
        for (User user : userNames.stream()
                .filter(users::containsKey)
                .map(users::get)
                .sorted(Comparator.comparing(User::name))
                .toList()) {
            if (!first) {
                response.append(",");
            }
            response.append(toJson(user));
            first = false;
        }
        
        response.append("]}");
        return response.toString();
    }

    private String toJson(User user) {
        StringBuilder json = new StringBuilder();
        json.append("{");
        
        // Name
        json.append("\"name\":\"").append(user.name()).append("\",");
        
        // Owes 
        json.append("\"owes\":{");
        Map<String, Double> owes = new TreeMap<>(user.owes()); // Ensure sorted keys
        boolean firstOwes = true;
        for (Map.Entry<String, Double> entry : owes.entrySet()) {
            if (!firstOwes) {
                json.append(",");
            }
            json.append("\"").append(entry.getKey()).append("\":").append(entry.getValue());
            firstOwes = false;
        }
        json.append("},");
        
        // Owed by
        json.append("\"owed_by\":{");
        Map<String, Double> owedBy = new TreeMap<>(user.owedBy()); // Ensure sorted keys
        boolean firstOwedBy = true;
        for (Map.Entry<String, Double> entry : owedBy.entrySet()) {
            if (!firstOwedBy) {
                json.append(",");
            }
            json.append("\"").append(entry.getKey()).append("\":").append(entry.getValue());
            firstOwedBy = false;
        }
        json.append("},");
        
        // Balance
        double balance = user.owedBy().values().stream().mapToDouble(Double::doubleValue).sum()
            - user.owes().values().stream().mapToDouble(Double::doubleValue).sum();
        json.append("\"balance\":").append(balance);
        
        json.append("}");
        return json.toString();
    }
    
    private String extractString(String json, String key) {
        String keyPattern = "\"" + key + "\":";
        int keyIndex = json.indexOf(keyPattern);
        if (keyIndex == -1) {
            throw new IllegalArgumentException("Missing " + key + " parameter");
        }
        
        int valueStart = json.indexOf('"', keyIndex + keyPattern.length());
        int valueEnd = json.indexOf('"', valueStart + 1);
        
        if (valueStart == -1 || valueEnd == -1) {
            throw new IllegalArgumentException("Invalid " + key + " parameter format");
        }
        
        return json.substring(valueStart + 1, valueEnd);
    }
    
    private double extractDouble(String json, String key) {
        String keyPattern = "\"" + key + "\":";
        int keyIndex = json.indexOf(keyPattern);
        if (keyIndex == -1) {
            throw new IllegalArgumentException("Missing " + key + " parameter");
        }
        
        int valueStart = keyIndex + keyPattern.length();
        int valueEnd = json.indexOf(',', valueStart);
        if (valueEnd == -1) {
            valueEnd = json.indexOf('}', valueStart);
        }
        
        if (valueStart == -1 || valueEnd == -1) {
            throw new IllegalArgumentException("Invalid " + key + " parameter format");
        }
        
        String valueStr = json.substring(valueStart, valueEnd).trim();
        return Double.parseDouble(valueStr);
    }
}
