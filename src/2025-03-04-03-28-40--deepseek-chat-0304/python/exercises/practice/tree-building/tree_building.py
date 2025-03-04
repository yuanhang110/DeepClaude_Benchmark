class Record:
    def __init__(self, record_id, parent_id):
        self.record_id = record_id
        self.parent_id = parent_id


class Node:
    def __init__(self, node_id):
        self.node_id = node_id
        self.children = []


def BuildTree(records):
    if not records:
        return None
        
    # Sort records by ID and validate continuity
    records.sort(key=lambda x: x.record_id)
    ordered_ids = [r.record_id for r in records]
    
    # Validate record IDs are continuous starting from 0
    if ordered_ids != list(range(len(records))):
        raise ValueError('Record id is invalid or out of order.')
    
    # Create nodes and build lookup dictionary
    nodes = {r.record_id: Node(r.record_id) for r in records}
    
    # Build tree structure
    for record in records:
        if record.record_id == 0:
            # Validate root node
            if record.parent_id != 0:
                raise ValueError("Node parent_id should be smaller than it's record_id.")
            continue
            
        # Validate parent-child relationship
        if record.record_id == record.parent_id:
            raise ValueError('Only root should have equal record and parent id.')
        if record.record_id < record.parent_id:
            raise ValueError("Node parent_id should be smaller than it's record_id.")
            
        # Get parent node and add current node as its child
        if record.parent_id not in nodes:
            raise ValueError('Parent node not found')
            
        parent_node = nodes[record.parent_id]
        parent_node.children.append(nodes[record.record_id])
    
    return nodes[0]
