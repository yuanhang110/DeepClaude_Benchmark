class Record:
    def __init__(self, record_id, parent_id):
        self.record_id = record_id
        self.parent_id = parent_id


class Node:
    def __init__(self, node_id):
        self.node_id = node_id
        self.children = []


def BuildTree(records):
    """Build a tree from the given records.
    
    Args:
        records: List of Record objects with record_id and parent_id attributes
        
    Returns:
        Root Node of the tree, or None if records is empty
        
    Raises:
        ValueError: If the records form an invalid tree structure
    """
    if not records:
        return None
        
    # Sort records by ID for validation
    records.sort(key=lambda x: x.record_id)
    
    # Validate records form a complete sequence from 0 to n-1
    if records[0].record_id != 0:
        raise ValueError("Record id is invalid or out of order.")
    if records[-1].record_id != len(records) - 1:
        raise ValueError("Record id is invalid or out of order.")
        
    # Create all nodes first using a dictionary for O(1) lookup
    nodes = {}
    for record in records:
        # Validate parent relationships
        if record.record_id == 0:
            if record.parent_id != 0:
                raise ValueError("Node parent_id should be smaller than it's record_id.")
        else:
            if record.parent_id == record.record_id:
                raise ValueError("Only root should have equal record and parent id.")
            elif record.parent_id > record.record_id:
                raise ValueError("Node parent_id should be smaller than it's record_id.")
            
        nodes[record.record_id] = Node(record.record_id)
    
    # Connect nodes in a single pass
    root = nodes[0]
    for record in records:
        if record.record_id != 0:  # Skip root
            parent = nodes[record.parent_id]
            parent.children.append(nodes[record.record_id])
            
    return root
