import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

class BuildTree {

    TreeNode buildTree(ArrayList<Record> records) throws InvalidRecordsException {
        records.sort(Comparator.comparing(Record::getRecordId));
        
        // Validate records are contiguous starting from 0
        for (int i = 0; i < records.size(); i++) {
            if (records.get(i).getRecordId() != i) {
                throw new InvalidRecordsException("Invalid Records");
            }
        }

        HashMap<Integer, TreeNode> nodeMap = new HashMap<>();
        
        // Create all nodes first
        for (Record record : records) {
            // Validate root node
            if (record.getRecordId() == 0) {
                if (record.getParentId() != 0) {
                    throw new InvalidRecordsException("Invalid Records");
                }
            } else {
                // Validate non-root nodes
                if (record.getParentId() >= record.getRecordId()) {
                    throw new InvalidRecordsException("Invalid Records");
                }
            }
            
            TreeNode node = new TreeNode(record.getRecordId());
            nodeMap.put(record.getRecordId(), node);
        }

        // Build parent-child relationships
        for (Record record : records) {
            if (record.getRecordId() == 0) {
                continue; // root has no parent
            }
            
            TreeNode parent = nodeMap.get(record.getParentId());
            TreeNode child = nodeMap.get(record.getRecordId());
            
            if (parent == null) {
                throw new InvalidRecordsException("Invalid Records");
            }
            
            parent.getChildren().add(child);
        }

        return records.isEmpty() ? null : nodeMap.get(0);
    }

}
