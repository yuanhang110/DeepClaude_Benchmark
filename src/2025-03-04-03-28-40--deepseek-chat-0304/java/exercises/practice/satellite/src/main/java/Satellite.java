import java.util.List;
import java.util.ArrayList;

public class Satellite {
    public Tree treeFromTraversals(List<Character> preorderInput, List<Character> inorderInput) {
        if (preorderInput == null || inorderInput == null) {
            throw new IllegalArgumentException("Traversals must not be null");
        }
        if (preorderInput.size() != inorderInput.size()) {
            throw new IllegalArgumentException("Traversals must have the same length");
        }
        if (preorderInput.isEmpty()) {
            throw new IllegalArgumentException("Traversals must not be empty");
        }
        
        // Check for duplicates
        if (preorderInput.stream().distinct().count() != preorderInput.size() ||
            inorderInput.stream().distinct().count() != inorderInput.size()) {
            throw new IllegalArgumentException("Traversals must not contain duplicate values");
        }
        
        // Check if traversals contain same elements
        if (!new ArrayList<>(preorderInput).containsAll(inorderInput) ||
            !new ArrayList<>(inorderInput).containsAll(preorderInput)) {
            throw new IllegalArgumentException("Traversals must contain the same elements");
        }
        
        return buildTree(preorderInput, inorderInput);
    }
    
    private Tree buildTree(List<Character> preorder, List<Character> inorder) {
        if (preorder.isEmpty() || inorder.isEmpty()) {
            return null;
        }
        
        if (preorder.size() != inorder.size()) {
            throw new IllegalStateException("Invalid traversal sizes during tree construction");
        }
        
        // First element in preorder is always the root
        Character rootValue = preorder.get(0);
        Tree root = new Tree(rootValue);
        
        // Find the index of root in inorder traversal
        int rootIndex = inorder.indexOf(rootValue);
        
        // Build left and right subtrees recursively
        List<Character> leftInorder = inorder.subList(0, rootIndex);
        List<Character> rightInorder = inorder.subList(rootIndex + 1, inorder.size());
        
        List<Character> leftPreorder = preorder.subList(1, 1 + leftInorder.size());
        List<Character> rightPreorder = preorder.subList(1 + leftInorder.size(), preorder.size());
        
        root.left = buildTree(leftPreorder, leftInorder);
        root.right = buildTree(rightPreorder, rightInorder);
        
        return root;
    }
}

class Tree {
    Character value;
    Tree left;
    Tree right;
    
    Tree(Character value) {
        this.value = value;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Tree other = (Tree) obj;
        if (!value.equals(other.value)) return false;
        if (left == null ^ other.left == null) return false;
        if (right == null ^ other.right == null) return false;
        if (left != null && !left.equals(other.left)) return false;
        if (right != null && !right.equals(other.right)) return false;
        return true;
    }
}
