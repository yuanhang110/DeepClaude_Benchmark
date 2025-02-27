class Zipper {
    private int value;
    private Zipper up;
    private Zipper left;
    private Zipper right;

    Zipper(int val) {
        this.value = val;
        this.up = null;
        this.left = null;
        this.right = null;
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
        Zipper newZipper = new Zipper(this.value);
        newZipper.left = leftChild;
        newZipper.right = this.right;
        
        if (this.up != null) {
            if (this.up.left == this) {
                newZipper.up = this.up.setLeft(newZipper);
            } else if (this.up.right == this) {
                newZipper.up = this.up.setRight(newZipper);
            }
        }
        
        if (leftChild != null) {
            leftChild.up = newZipper;
        }
        return newZipper;
    }

    Zipper setRight(Zipper rightChild) {
        Zipper newZipper = new Zipper(this.value);
        newZipper.right = rightChild;
        newZipper.left = this.left;
        
        if (this.up != null) {
            if (this.up.left == this) {
                newZipper.up = this.up.setLeft(newZipper);
            } else if (this.up.right == this) {
                newZipper.up = this.up.setRight(newZipper);
            }
        }
        
        if (rightChild != null) {
            rightChild.up = newZipper;
        }
        return newZipper;
    }

    Zipper setValue(int val) {
        Zipper newZipper = new Zipper(val);
        newZipper.left = this.left;
        newZipper.right = this.right;
        
        if (this.up != null) {
            if (this.up.left == this) {
                newZipper.up = this.up.setLeft(newZipper);
            } else if (this.up.right == this) {
                newZipper.up = this.up.setRight(newZipper);
            }
        }
        
        return newZipper;
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
        return printNode(root);
    }

    private String printNode(Zipper node) {
        if (node == null) {
            return "";
        }
        String result = String.valueOf(node.getValue());
        String left = printNode(node.left);
        String right = printNode(node.right);
        if (!left.isEmpty() || !right.isEmpty()) {
            result += " (" + left + "," + right + ")";
        }
        return result;
    }
}
