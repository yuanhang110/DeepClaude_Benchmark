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
        
    records.sort(key=lambda x: x.record_id)
    ordered_id = [i.record_id for i in records]
    
    # Validate ID sequence
    if ordered_id[0] != 0:
        raise ValueError("Record id is invalid or out of order.")
    if ordered_id[-1] != len(records) - 1:
        raise ValueError("Record id is invalid or out of order.")
    if records[-1].record_id != len(records) - 1:
        raise ValueError("Tree IDs must be continuous from 0 to len(records)-1")
    
    nodes = {r.record_id: Node(r.record_id) for r in records}
    root = None
    for record in records:
        node_id = record.record_id
        parent_id = record.parent_id
        
        # Validate root node
        if node_id == 0:
            if parent_id != 0:
                raise ValueError("Node parent_id should be smaller than it's record_id.")
            root = nodes[node_id]
            continue
            
        # Validate parent-child relationships
        if parent_id > node_id:
            raise ValueError("Node parent_id should be smaller than it's record_id.")
        if parent_id == node_id:
            if node_id != 0:
                raise ValueError("Only root should have equal record and parent id.")
        if parent_id not in nodes:
            raise ValueError(f"Node {node_id} references missing parent {parent_id}")
            
        # Add child to parent
        parent_node = nodes[parent_id]
        parent_node.children.append(nodes[node_id])
    
    return root
