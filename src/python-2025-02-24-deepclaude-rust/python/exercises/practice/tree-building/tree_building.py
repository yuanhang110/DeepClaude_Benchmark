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
        
    # Sort records by ID for sequential processing
    records.sort(key=lambda x: x.record_id)
    
    # Get sorted record IDs
    ordered_id = [r.record_id for r in records]
    
    # Validate record IDs are contiguous starting from 0
    if ordered_id != list(range(len(records))):
        raise ValueError("Record id is invalid or out of order.")
    
    # Create all nodes
    nodes = [Node(i) for i in range(len(records))]
    
    # Process each record to build the tree
    for record in records:
        # Special validation for root
        if record.record_id == 0:
            if record.parent_id != 0:
                raise ValueError("Node parent_id should be smaller than it's record_id.")
            continue
            
        # Validate parent/child relationship
        if record.parent_id == record.record_id:
            raise ValueError("Only root should have equal record and parent id.")
        if record.parent_id > record.record_id:
            raise ValueError("Node parent_id should be smaller than it's record_id.")
            
        # Add this node as a child of its parent
        parent = nodes[record.parent_id]
        child = nodes[record.record_id]
        parent.children.append(child)
        
    # Sort children by node_id for consistent output
    for node in nodes:
        node.children.sort(key=lambda x: x.node_id)
        
    return nodes[0]
