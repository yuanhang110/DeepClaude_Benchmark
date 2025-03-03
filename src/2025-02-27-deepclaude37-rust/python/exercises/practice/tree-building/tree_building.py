class Record:
    def __init__(self, record_id, parent_id):
        self.record_id = record_id
        self.parent_id = parent_id


class Node:
    def __init__(self, node_id):
        self.node_id = node_id
        self.children = []


def BuildTree(records):
    """Build a tree from the given records."""
    if not records:
        return None
        
    # Sort and validate records
    records.sort(key=lambda x: x.record_id)
    
    # Check if record IDs are continuous from 0 to n-1
    ordered_ids = [record.record_id for record in records]
    if ordered_ids != list(range(len(records))):
        raise ValueError("Record id is invalid or out of order.")
    
    # Create nodes using a dictionary for O(1) lookups
    nodes = {}
    for record in records:
        # Create a new node for this record
        nodes[record.record_id] = Node(record.record_id)
        
        # Check root node
        if record.record_id == 0:
            if record.parent_id != 0:
                raise ValueError("Node parent_id should be smaller than it's record_id.")
            continue
            
        # Validate parent relationship
        if record.parent_id >= record.record_id:
            if record.parent_id == record.record_id:
                raise ValueError("Only root should have equal record and parent id.")
            else:
                raise ValueError("Node parent_id should be smaller than it's record_id.")
            
        # Add this node as a child to its parent
        nodes[record.parent_id].children.append(nodes[record.record_id])
    
    # Return the root node (if any nodes exist)
    return nodes[0] if nodes else None
