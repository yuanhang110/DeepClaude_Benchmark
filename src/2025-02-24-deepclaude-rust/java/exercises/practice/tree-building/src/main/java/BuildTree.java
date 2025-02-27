import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

class BuildTree {

    TreeNode buildTree(ArrayList<Record> records) throws InvalidRecordsException {
        if (records.isEmpty()) {
            return null;
        }

        // Sort records by ID
        records.sort(Comparator.comparing(Record::getRecordId));

        // Validate records form a continuous sequence from 0 to size-1
        if (records.get(0).getRecordId() != 0 || 
            records.get(records.size() - 1).getRecordId() != records.size() - 1) {
            throw new InvalidRecordsException("Invalid Records");
        }

        Map<Integer, TreeNode> nodes = new HashMap<>();

        // Create nodes and validate parent relationships
        for (Record record : records) {
            int id = record.getRecordId();
            int parentId = record.getParentId();

            // Validate parent relationships
            if ((id == 0 && parentId != 0) ||                    // Root must have parent 0
                (id != 0 && id == parentId) ||                   // Non-root can't be its own parent
                (id < parentId)) {                               // Parent ID must be less than child ID
                throw new InvalidRecordsException("Invalid Records");
            }

            // Create new node
            TreeNode node = new TreeNode(id);
            nodes.put(id, node);

            // If not root, add as child to parent
            if (id != 0) {
                TreeNode parent = nodes.get(parentId);
                parent.getChildren().add(node);
            }
        }

        return nodes.get(0);
    }
}
