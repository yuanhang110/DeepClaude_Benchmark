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
    Build a tree structure from records containing IDs and parent IDs.
    
    Args:
        records: List of Record objects with record_id and parent_id attributes
        
    Returns:
        The root Node of the tree, or None if records is empty
    
    Raises:
        ValueError: If the records do not form a valid tree
    """
    if not records:
        return None
    
    # Validate records
    records.sort(key=lambda x: x.record_id)
    record_ids = [record.record_id for record in records]
    
    # Check if IDs are continuous and start from 0
    if record_ids != list(range(len(records))):
        raise ValueError("Record id is invalid or out of order.")
    
    # Create all nodes
    nodes = {record.record_id: Node(record.record_id) for record in records}
    
    # Connect nodes based on parent-child relationships
    for record in records:
        # Root validation
        if record.record_id == 0:
            if record.parent_id != 0:
                raise ValueError("Node parent_id should be smaller than it's record_id.")
        # Non-root validation
        else:
            if record.parent_id == record.record_id:
                raise ValueError("Only root should have equal record and parent id.")
            elif record.parent_id > record.record_id:
                raise ValueError("Node parent_id should be smaller than it's record_id.")
            
            # Add this node as a child to its parent
            parent_node = nodes[record.parent_id]
            parent_node.children.append(nodes[record.record_id])
    
    return nodes[0] if nodes else None
