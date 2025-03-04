public class Zipper {
    private Zipper up;
    private Zipper left;
    private Zipper right;
    private int value;

    public Zipper(int val) {
        this.value = val;
    }

    public BinaryTree toTree() {
        Zipper root = this;
        while (root.up != null) {
            root = root.up;
        }
        return new BinaryTree(root);
    }

    public int getValue() {
        return this.value;
    }

    public Zipper setLeft(Zipper leftChild) {
        this.left = leftChild;
        if (leftChild != null) {
            leftChild.up = this;
        }
        return this;
    }

    public Zipper setRight(Zipper rightChild) {
        this.right = rightChild;
        if (rightChild != null) {
            rightChild.up = this;
        }
        return this;
    }

    public Zipper setValue(int val) {
        this.value = val;
        return this;
    }

    public Zipper getUp() {
        return this.up;
    }

    public Zipper getLeft() {
        return this.left;
    }

    public Zipper getRight() {
        return this.right;
    }
}

public class BinaryTree {
    private final Zipper root;

    public BinaryTree(int value) {
        this.root = new Zipper(value);
    }

    public BinaryTree(Zipper root) {
        this.root = root;
    }

    public Zipper getRoot() {
        return this.root;
    }

    public String printTree() {
        return printTreeHelper(this.root, 0);
    }

    private String printTreeHelper(Zipper node, int depth) {
        if (node == null) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        result.append("  ".repeat(depth))
              .append(node.getValue())
              .append("\n")
              .append(printTreeHelper(node.getLeft(), depth + 1))
              .append(printTreeHelper(node.getRight(), depth + 1));
        return result.toString();
    }
}
