class Zipper {
    Zipper up;
    Zipper left;
    Zipper right;
    private int value;

    Zipper(int val) {
        this(val, null, null, null);
    }

    private Zipper(int val, Zipper up, Zipper left, Zipper right) {
        this.value = val;
        this.up = up;
        this.left = left;
        this.right = right;
    }

    BinaryTree toTree() {
        Zipper root = this;
        while (root.up != null) {
            root = root.up;
        }
        return new BinaryTree(root);
    }

    int getValue() {
        return value;
    }

    Zipper setLeft(Zipper leftChild) {
        if (leftChild == null) {
            return new Zipper(this.value, this.up, null, this.right);
        }
        // Create new left child with proper up pointer and recursively set children
        Zipper newLeft = new Zipper(leftChild.value, null, null, null);
        // Set up pointer after creation to avoid null this reference
        newLeft.up = this;
        if (leftChild.left != null) {
            Zipper newLeftChild = new Zipper(leftChild.left.value, newLeft, 
                leftChild.left.left, leftChild.left.right);
            newLeft.left = newLeftChild;
        }
        if (leftChild.right != null) {
            Zipper newRightChild = new Zipper(leftChild.right.value, newLeft,
                leftChild.right.left, leftChild.right.right);
            newLeft.right = newRightChild;
        }
        return new Zipper(this.value, this.up, newLeft, this.right);
    }

    Zipper setRight(Zipper rightChild) {
        if (rightChild == null) {
            return new Zipper(this.value, this.up, this.left, null);
        }
        // Create new right child with proper up pointer and recursively set children
        Zipper newRight = new Zipper(rightChild.value, null, null, null);
        // Set up pointer after creation to avoid null this reference
        newRight.up = this;
        if (rightChild.left != null) {
            Zipper newLeftChild = new Zipper(rightChild.left.value, newRight,
                rightChild.left.left, rightChild.left.right);
            newRight.left = newLeftChild;
        }
        if (rightChild.right != null) {
            Zipper newRightChild = new Zipper(rightChild.right.value, newRight,
                rightChild.right.left, rightChild.right.right);
            newRight.right = newRightChild;
        }
        return new Zipper(this.value, this.up, this.left, newRight);
    }

    Zipper setValue(int val) {
        return new Zipper(val, this.up, this.left, this.right);
    }
}

class BinaryTree {
    private Zipper root;

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
        if (node == null) {
            return "";
        }
        String left = printTree(node.left);
        String right = printTree(node.right);
        String result = Integer.toString(node.getValue());
        if (!left.isEmpty() || !right.isEmpty()) {
            result += " (" + left + ") (" + right + ")";
        }
        return result;
    }
}
