class Record:
    def __init__(self, record_id, parent_id):
        self.record_id = record_id
        self.parent_id = parent_id


class Node:
    def __init__(self, node_id):
        self.node_id = node_id
        self.children = []


def BuildTree(records):
    """
    Build a tree structure from records containing ID and parent ID.
    
    Args:
        records: List of Record objects with record_id and parent_id attributes
        
    Returns:
        Root Node of the tree or None for empty input
    
    Raises:
        ValueError: If the tree structure is invalid
    """
    if not records:
        return None
        
    # Sort records by record_id for processing
    records.sort(key=lambda x: x.record_id)
    
    # Validate record IDs form a continuous sequence starting at 0
    if records[0].record_id != 0:
        raise ValueError("Record id is invalid or out of order.")
    
    if records[-1].record_id != len(records) - 1:
        raise ValueError("Record id is invalid or out of order.")
    
    # Create all nodes first
    nodes = {}
    for record in records:
        # Validate parent-child relationships
        if record.record_id != 0 and record.record_id <= record.parent_id:
            raise ValueError("Node parent_id should be smaller than it's record_id.")
            
        if record.record_id == record.parent_id and record.record_id != 0:
            raise ValueError("Only root should have equal record and parent id.")
            
        if record.record_id == 0 and record.parent_id != 0:
            raise ValueError("Node parent_id should be smaller than it's record_id.")
            
        # Create the node
        nodes[record.record_id] = Node(record.record_id)
    
    # Connect nodes to form the tree
    for record in records:
        if record.record_id != 0:  # Skip root when connecting
            parent_node = nodes[record.parent_id]
            child_node = nodes[record.record_id]
            parent_node.children.append(child_node)
    
    return nodes[0] if nodes else None
