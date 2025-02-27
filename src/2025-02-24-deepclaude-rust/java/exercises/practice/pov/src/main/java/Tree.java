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
        if (!label.equals(tree.label)) return false;
        if (children.size() != tree.children.size()) return false;
        
        // Check if all children have matching counterparts
        List<Tree> otherChildren = new ArrayList<>(tree.children);
        for (Tree child : children) {
            Iterator<Tree> it = otherChildren.iterator();
            boolean found = false;
            while (it.hasNext()) {
                if (child.equals(it.next())) {
                    it.remove();
                    found = true;
                    break;
                }
            }
            if (!found) return false;
        }
        return true;
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
        // Find the path from current root to the target node
        List<Tree> path = new ArrayList<>();
        if (!findPath(this, fromNode, path)) {
            throw new UnsupportedOperationException("Tree could not be reoriented");
        }

        // Reverse the path to get from target to original root
        List<Tree> reversedPath = new ArrayList<>(path);
        Collections.reverse(reversedPath);

        Tree newRoot = null;
        // Process each node in reversed order (original root to target)
        for (int i = reversedPath.size() - 1; i >= 0; i--) {
            Tree currentNode = reversedPath.get(i);
            List<Tree> newChildren = new ArrayList<>();

            // Determine the child in the original path to exclude
            Tree childInPath = (i > 0) ? reversedPath.get(i - 1) : null;

            // Add all children except the one in the path
            for (Tree child : currentNode.children) {
                if (child != childInPath) {
                    newChildren.add(child);
                }
            }

            // Add the newRoot (parent in the new tree) if it exists
            if (newRoot != null) {
                newChildren.add(newRoot);
            }

            newRoot = new Tree(currentNode.label, newChildren);
        }

        return newRoot;
    }

    private boolean findPath(Tree node, String target, List<Tree> path) {
        path.add(node);
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

    public List<String> pathTo(String fromNode, String toNode) {
        Tree reorientedTree = this.fromPov(fromNode);
        List<Tree> path = new ArrayList<>();
        if (!reorientedTree.findPath(reorientedTree, toNode, path)) {
            throw new UnsupportedOperationException("No path found");
        }
        List<String> result = new ArrayList<>();
        for (Tree node : path) {
            result.add(node.label);
        }
        return result;
    }
}
