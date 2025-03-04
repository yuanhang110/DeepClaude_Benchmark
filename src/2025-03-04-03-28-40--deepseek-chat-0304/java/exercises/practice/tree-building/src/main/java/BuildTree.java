import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

class BuildTree {

    TreeNode buildTree(ArrayList<Record> records) throws InvalidRecordsException {
        if (records == null) {
            throw new InvalidRecordsException("Records cannot be null");
        }
        if (records.isEmpty()) {
            return null;
        }

        // Sort records by ID for validation and processing
        records.sort(Comparator.comparing(Record::getRecordId));
        
        // Validate record IDs are continuous starting from 0
        for (int i = 0; i < records.size(); i++) {
            if (records.get(i).getRecordId() != i) {
                throw new InvalidRecordsException("Invalid Records");
            }
        }

        Map<Integer, TreeNode> nodeMap = new HashMap<>();
        
        // First pass: create all nodes and validate parent-child relationships
        for (Record record : records) {
            int id = record.getRecordId();
            int parentId = record.getParentId();
            
            // Validate record
            if (id == 0) {
                if (parentId != 0) {
                    throw new InvalidRecordsException("Root node must have parent ID 0");
                }
            } else {
                if (parentId >= id || parentId < 0) {
                    throw new InvalidRecordsException("Invalid parent ID");
                }
            }
            
            // Create and store node
            TreeNode node = new TreeNode(id);
            nodeMap.put(id, node);
        }
        
        // Second pass: build tree structure
        for (Record record : records) {
            int id = record.getRecordId();
            int parentId = record.getParentId();
            
            if (id != parentId) { // Skip root node
                TreeNode parent = nodeMap.get(parentId);
                if (parent == null) {
                    throw new InvalidRecordsException("Parent node not found");
                }
                TreeNode child = nodeMap.get(id);
                if (child == null) {
                    throw new InvalidRecordsException("Child node not found");
                }
                parent.getChildren().add(child);
            }
        }
        
        TreeNode root = nodeMap.get(0);
        if (root == null) {
            throw new InvalidRecordsException("Root node not found");
        }
        return root;
    }
}
