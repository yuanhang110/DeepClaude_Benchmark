import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

class BuildTree {

    TreeNode buildTree(ArrayList<Record> records) throws InvalidRecordsException {
        // Validate all IDs are unique and in range 0..size-1
        Set<Integer> ids = new HashSet<>();
        int maxId = -1;
        for (Record record : records) {
            int id = record.getRecordId();
            // Validate ID range
            if (id < 0 || id >= records.size()) {
                throw new InvalidRecordsException("Invalid Records");
            }
            // Check for duplicates
            if (!ids.add(id)) {
                throw new InvalidRecordsException("Invalid Records");
            }
            // Track maximum ID
            if (id > maxId) {
                maxId = id;
            }
        }
        
        // Validate ID sequence when there are records
        if (!records.isEmpty()) {
            // Max ID should match last possible ID
            if (maxId != records.size() - 1) {
                throw new InvalidRecordsException("Invalid Records");
            }
            // Must have root node (ID 0)
            if (!ids.contains(0)) {
                throw new InvalidRecordsException("Invalid Records");
            }
        }

        // Use a HashMap for O(1) node lookup
        HashMap<Integer, TreeNode> nodeMap = new HashMap<>();
        
        // First pass: Create all nodes and validate
        for (Record record : records) {
            int id = record.getRecordId();
            int parentId = record.getParentId();
            
            // Validate records
            if (id == 0 && parentId != 0) {
                throw new InvalidRecordsException("Invalid Records");
            }
            if (id < parentId) {
                throw new InvalidRecordsException("Invalid Records");
            }
            if (id == parentId && id != 0) {
                throw new InvalidRecordsException("Invalid Records");
            }
            
            // Create the node and add to map
            nodeMap.put(id, new TreeNode(id));
        }

        // Second pass: Add children in original record order
        for (Record record : records) {
            int id = record.getRecordId();
            int parentId = record.getParentId();
            
            if (id != 0) {  // Skip root
                TreeNode parent = nodeMap.get(parentId);
                TreeNode node = nodeMap.get(id);
                parent.getChildren().add(node);
            }
        }

        // Return root if any nodes exist
        if (!nodeMap.isEmpty()) {
            return nodeMap.get(0);
        }
        return null;
    }

}
