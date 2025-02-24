class Zipper {
    private final int value;
    private Zipper parent;
    private Zipper left;
    private Zipper right;

    Zipper(int val) {
        this.value = val;
        this.parent = null;
        this.left = null;
        this.right = null;
    }

    Zipper(int val, Zipper parent) {
        this.value = val;
        this.parent = parent;
        this.left = null;
        this.right = null;
    }

    Zipper up() {
        return parent;
    }

    Zipper left() {
        return left;
    }

    Zipper right() {
        return right;
    }

    BinaryTree toTree() {
        Zipper root = this;
        while (root.parent != null) {
            root = root.parent;
        }
        return new BinaryTree(root);
    }

    int getValue() {
        return value;
    }

    Zipper setLeft(Zipper leftChild) {
        this.left = leftChild;
        if (leftChild != null) {
            leftChild.parent = this;
        }
        return this;
    }

    Zipper setRight(Zipper rightChild) {
        this.right = rightChild;
        if (rightChild != null) {
            rightChild.parent = this;
        }
        return this;
    }

    Zipper setValue(int val) {
        return new Zipper(val, parent);
    }
}

class BinaryTree {
    private final Zipper root;

    BinaryTree(int value) {
        this.root = new Zipper(value);
    }

    BinaryTree(Zipper root) {
        this.root = root;
    }

    Zipper getRoot() {
        return root;
    }

    String printTree() {
        return printTree(root);
    }

    private String printTree(Zipper node) {
        if (node == null) return "()";
        return String.format("(%d %s %s)", 
            node.getValue(), 
            printTree(node.left), 
            printTree(node.right));
    }
}
