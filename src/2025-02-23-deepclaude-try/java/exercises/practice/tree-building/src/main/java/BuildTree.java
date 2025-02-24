import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class BuildTree {

    public TreeNode buildTree(ArrayList<Record> records) throws InvalidRecordsException {
        if (records.isEmpty()) {
            return null;
        }

        records.sort(Comparator.comparing(Record::getRecordId));
        
        // Validate records and create nodes
        HashMap<Integer, TreeNode> nodes = new HashMap<>();
        for (int i = 0; i < records.size(); i++) {
            Record record = records.get(i);
            if (record.getRecordId() != i) {
                throw new InvalidRecordsException("Invalid Records");
            }
            
            // Validate parent-child relationships
            if (record.getRecordId() == 0 && record.getParentId() != 0) {
                throw new InvalidRecordsException("Invalid Records");
            }
            if (record.getRecordId() < record.getParentId()) {
                throw new InvalidRecordsException("Invalid Records");
            }
            if (record.getRecordId() == record.getParentId() && record.getRecordId() != 0) {
                throw new InvalidRecordsException("Invalid Records");
            }
            
            nodes.put(record.getRecordId(), new TreeNode(record.getRecordId()));
        }
        
        // Build tree structure
        for (Record record : records) {
            if (record.getRecordId() != 0) {  // Skip root node
                TreeNode parent = nodes.get(record.getParentId());
                TreeNode child = nodes.get(record.getRecordId());
                parent.getChildren().add(child);
            }
        }
        
        return nodes.get(0);  // Return root node
    }

}
