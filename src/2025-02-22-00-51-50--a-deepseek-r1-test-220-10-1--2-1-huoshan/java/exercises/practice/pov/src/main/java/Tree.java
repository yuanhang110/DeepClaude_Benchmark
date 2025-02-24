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
        
        // Find path to target node using iterative DFS
        Stack<Iterator<Tree>> stack = new Stack<>();
        List<Tree> path = new ArrayList<>();
        path.add(this);
        stack.push(children.iterator());
        
        while (!stack.isEmpty()) {
            Iterator<Tree> it = stack.peek();
            if (!it.hasNext()) {
                stack.pop();
                path.remove(path.size() - 1);
                continue;
            }
            
            Tree child = it.next();
            path.add(child);
            if (child.label.equals(fromNode)) {
                // Found our target node - now rebuild the tree
                Tree current = child;
                for (int i = path.size() - 2; i >= 0; i--) {
                    Tree parent = path.get(i);
                    List<Tree> siblings = new ArrayList<>(parent.children);
                    siblings.remove(current);
                    current = new Tree(parent.label, List.of(current));
                    current.children.addAll(siblings);
                }
                return current;
            }
            
            stack.push(child.children.iterator());
        }
        
        throw new UnsupportedOperationException("Tree could not be reoriented");
    }

    public List<String> pathTo(String fromNode, String toNode) {
        Tree povTree = fromPov(fromNode);
        List<String> path = new ArrayList<>();
        if (findPath(povTree, toNode, path)) {
            return path;
        }
        throw new UnsupportedOperationException("No path found");
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
