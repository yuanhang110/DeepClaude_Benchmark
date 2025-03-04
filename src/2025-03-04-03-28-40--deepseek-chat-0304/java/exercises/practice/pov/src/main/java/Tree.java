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

    private Tree findNode(String label) {
        if (label == null) {
            throw new IllegalArgumentException("Label cannot be null");
        }
        if (this.label.equals(label)) {
            return this;
        }
        for (Tree child : children) {
            if (child != null) {
                Tree found = child.findNode(label);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }

    public Tree fromPov(String fromNode) {
        if (fromNode == null) {
            throw new IllegalArgumentException("fromNode cannot be null");
        }
        Tree newRoot = findNode(fromNode);
        if (newRoot == null) {
            throw new UnsupportedOperationException("Tree could not be reoriented");
        }
        return buildTree(newRoot, null);
    }

    private Tree buildTree(Tree node, Tree parent) {
        List<Tree> newChildren = new ArrayList<>();
        // Add all children except parent
        for (Tree child : node.children) {
            if (child != parent) {
                newChildren.add(buildTree(child, node));
            }
        }
        // If parent exists, add it as a child
        if (parent != null) {
            newChildren.add(buildTree(parent, node));
        }
        return new Tree(node.label, newChildren);
    }

    public List<String> pathTo(String fromNode, String toNode) {
        if (fromNode == null || toNode == null) {
            throw new IllegalArgumentException("Nodes cannot be null");
        }
        Tree start = findNode(fromNode);
        Tree end = findNode(toNode);
        if (start == null || end == null) {
            throw new UnsupportedOperationException("No path found");
        }
        
        List<String> path = new ArrayList<>();
        findPath(start, end, path);
        return path;
    }

    private boolean findPath(Tree current, Tree target, List<String> path) {
        path.add(current.label);
        if (current.equals(target)) {
            return true;
        }
        for (Tree child : current.children) {
            if (findPath(child, target, path)) {
                return true;
            }
        }
        path.remove(path.size() - 1);
        return false;
    }
}
