import java.util.*;

class Tree {
    private final String label;
    private final List<Tree> children;

    public Tree(String label) {
        this(label, new ArrayList<>());
    }

    public Tree(String label, List<Tree> children) {
        this.label = label;
        this.children = children;
    }

    public static Tree of(String label) {
        return new Tree(label);
    }

    public static Tree of(String label, List<Tree> children) {
        return new Tree(label, children);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tree tree = (Tree) o;
        return label.equals(tree.label)
                && children.size() == tree.children.size()
                && children.containsAll(tree.children)
                && tree.children.containsAll(children);
    }

    @Override
    public int hashCode() {
        return Objects.hash(label, children);
    }

    @Override
    public String toString() {
        return "Tree{" + label +
                ", " + children +
                "}";
    }

    public Tree fromPov(String fromNode) {
        if (this.label.equals(fromNode)) {
            return this;
        }

        // Find the target node
        Tree target = findNode(fromNode);
        if (target == null) {
            throw new UnsupportedOperationException("Tree could not be reoriented");
        }

        // Build a map from node to its parent
        Map<String, Tree> parentMap = new HashMap<>();
        buildParentMap(this, null, parentMap);

        // Create new tree with target as root
        return createNewTree(target, parentMap);
    }

    private Tree createNewTree(Tree target, Map<String, Tree> parentMap) {
        // Copy the children of the target node
        List<Tree> newChildren = new ArrayList<>();
        for (Tree child : target.children) {
            newChildren.add(createNewTree(child, parentMap));
        }

        // If target has a parent, add parent as a child (with the parent's other children)
        Tree parent = parentMap.get(target.label);
        if (parent != null) {
            List<Tree> parentChildren = new ArrayList<>();
            for (Tree child : parent.children) {
                if (!child.label.equals(target.label)) {
                    parentChildren.add(createNewTree(child, parentMap));
                }
            }

            // Add the grandparent (and its siblings) as a child of the parent
            Tree grandparent = parentMap.get(parent.label);
            if (grandparent != null) {
                // Add the parent's parent relationship to the new parent
                parentChildren.add(createParentBranch(parent, grandparent, parentMap));
            }

            newChildren.add(new Tree(parent.label, parentChildren));
        }

        return new Tree(target.label, newChildren);
    }

    private Tree createParentBranch(Tree child, Tree parent, Map<String, Tree> parentMap) {
        List<Tree> siblings = new ArrayList<>();
        for (Tree sibling : parent.children) {
            if (!sibling.label.equals(child.label)) {
                siblings.add(createNewTree(sibling, parentMap));
            }
        }

        // If there's a grandparent, include it
        Tree grandparent = parentMap.get(parent.label);
        if (grandparent != null) {
            siblings.add(createParentBranch(parent, grandparent, parentMap));
        }

        return new Tree(parent.label, siblings);
    }

    private Tree findNode(String label) {
        if (this.label.equals(label)) {
            return this;
        }
        for (Tree child : children) {
            Tree found = child.findNode(label);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    private void buildParentMap(Tree node, Tree parent, Map<String, Tree> parentMap) {
        if (parent != null) {
            parentMap.put(node.label, parent);
        }
        
        for (Tree child : node.children) {
            buildParentMap(child, node, parentMap);
        }
    }

    public List<String> pathTo(String fromNode, String toNode) {
        // Check if both nodes exist
        if (findNode(fromNode) == null || findNode(toNode) == null) {
            throw new UnsupportedOperationException("No path found");
        }
        
        // Reorient the tree with fromNode as root
        Tree reorientedTree = fromPov(fromNode);
        
        // Find path to toNode
        List<String> path = new ArrayList<>();
        if (!findPath(reorientedTree, toNode, path)) {
            throw new UnsupportedOperationException("No path found");
        }
        return path;
    }

    private boolean findPath(Tree node, String target, List<String> path) {
        path.add(node.label);
        
        if (node.label.equals(target)) {
            return true;
        }
        
        for (Tree child : node.children) {
            if (findPath(child, target, path)) {
                return true;
            }
        }
        
        path.remove(path.size() - 1);
        return false;
    }
}
