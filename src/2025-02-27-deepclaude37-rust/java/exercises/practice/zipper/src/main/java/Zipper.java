class Zipper {
    private final BinaryTree tree;
    private final int value;
    private final Zipper left;
    private final Zipper right;
    private final Path trail;
    
    private static class Path {
        final Direction dir;
        final int value;
        final Zipper left;
        final Zipper right;
        final Path parent;
        
        Path(Direction dir, int value, Zipper left, Zipper right, Path parent) {
            this.dir = dir;
            this.value = value;
            this.left = left;
            this.right = right;
            this.parent = parent;
        }
    }
    
    private enum Direction {
        LEFT, RIGHT
    }
    
    private Zipper(BinaryTree tree, int value, Zipper left, Zipper right, Path trail) {
        this.tree = tree;
        this.value = value;
        this.left = left;
        this.right = right;
        this.trail = trail;
    }
    
    static Zipper from_tree(BinaryTree tree) {
        if (tree == null) {
            throw new IllegalArgumentException("Cannot create zipper from null tree");
        }
        return new Zipper(tree, tree.getRoot().getValue(), 
                          tree.getRoot().left != null ? fromNode(tree.getRoot().left, null) : null,
                          tree.getRoot().right != null ? fromNode(tree.getRoot().right, null) : null, 
                          null);
    }
    
    private static Zipper fromNode(Zipper node, Path trail) {
        if (node == null) return null;
        return new Zipper(null, node.getValue(), node.left, node.right, trail);
    }
    
    BinaryTree toTree() {
        // Navigate to the root of the tree
        Zipper root = this;
        while (root.up() != null) {
            root = root.up();
        }
        return root.tree != null ? root.tree : new BinaryTree(root);
    }

    int getValue() {
        return this.value;
    }

    Zipper setLeft(Zipper leftChild) {
        return new Zipper(null, value, leftChild, right, trail);
    }

    Zipper setRight(Zipper rightChild) {
        return new Zipper(null, value, left, rightChild, trail);
    }

    Zipper setValue(int val) {
        return new Zipper(null, val, left, right, trail);
    }
    
    Zipper left() {
        if (left == null) return null;
        return new Zipper(null, left.value, left.left, left.right,
                          new Path(Direction.LEFT, value, null, right, trail));
    }
    
    Zipper right() {
        if (right == null) return null;
        return new Zipper(null, right.value, right.left, right.right,
                          new Path(Direction.RIGHT, value, left, null, trail));
    }
    
    Zipper up() {
        if (trail == null) return null;
        
        if (trail.dir == Direction.LEFT) {
            return new Zipper(null, trail.value, 
                             new Zipper(null, value, left, right, null), 
                             trail.right, trail.parent);
        } else {
            return new Zipper(null, trail.value, 
                             trail.left, 
                             new Zipper(null, value, left, right, null), 
                             trail.parent);
        }
    }
    
    Zipper prev() {
        if (trail == null) return null;
        if (trail.dir == Direction.RIGHT && trail.left != null) {
            Zipper node = new Zipper(null, trail.value, trail.left, null, trail.parent);
            while (node.right != null) {
                node = node.right();
            }
            return node;
        }
        return null;
    }
    
    Zipper next() {
        if (trail == null) return null;
        if (trail.dir == Direction.LEFT && trail.right != null) {
            Zipper node = new Zipper(null, trail.value, null, trail.right, trail.parent);
            while (node.left != null) {
                node = node.left();
            }
            return node;
        }
        return null;
    }
    
    Zipper insertBefore(Zipper newNode) {
        Zipper parent = up();
        if (parent == null) return null;
        
        if (trail.dir == Direction.LEFT) {
            return parent.setLeft(newNode).right();
        } else {
            Zipper result = parent.setRight(newNode);
            return result.right().prev();
        }
    }
    
    Zipper insertAfter(Zipper newNode) {
        Zipper parent = up();
        if (parent == null) return null;
        
        if (trail.dir == Direction.LEFT) {
            Zipper result = parent.setLeft(newNode);
            return result.left().next();
        } else {
            return parent.setRight(newNode).left();
        }
    }
    
    Zipper delete() {
        Zipper parent = up();
        if (parent == null) return null;
        
        if (trail.dir == Direction.LEFT) {
            return parent.setLeft(null);
        } else {
            return parent.setRight(null);
        }
    }
}

class BinaryTree {
    private final Zipper root;
    
    BinaryTree(int value) {
        this.root = new Zipper(null, value, null, null, null);
    }

    BinaryTree(Zipper root) {
        this.root = root;
    }

    Zipper getRoot() {
        return this.root;
    }

    String printTree() {
        return printNode(root);
    }
    
    private String printNode(Zipper node) {
        if (node == null) {
            return "()";
        }
        String left = printNode(node.left);
        String right = printNode(node.right);
        return "(" + node.getValue() + " " + left + " " + right + ")";
    }
    
    static Zipper zipper(BinaryTree tree) {
        return Zipper.from_tree(tree);
    }
}
