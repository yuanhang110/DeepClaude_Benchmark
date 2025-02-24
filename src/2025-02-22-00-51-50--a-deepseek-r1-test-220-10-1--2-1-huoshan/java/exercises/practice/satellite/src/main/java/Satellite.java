import java.util.List;
import java.util.HashSet;
import java.util.Set;

public class Satellite {
    public Tree treeFromTraversals(List<Character> preorderInput, List<Character> inorderInput) {
        if (preorderInput == null || inorderInput == null) {
            throw new IllegalArgumentException("Traversals must not be null.");
        }
        if (preorderInput.size() != inorderInput.size()) {
            throw new IllegalArgumentException("Traversals have different lengths.");
        }
        Set<Character> preSet = new HashSet<>(preorderInput);
        Set<Character> inSet = new HashSet<>(inorderInput);
        if (!preSet.equals(inSet)) {
            throw new IllegalArgumentException("Traversals have different elements.");
        }
        return buildTree(preorderInput, inorderInput);
    }

    private Tree buildTree(List<Character> preorder, List<Character> inorder) {
        return buildTreeHelper(preorder, inorder, 0, 0, inorder.size() - 1);
    }

    private Tree buildTreeHelper(List<Character> preorder, List<Character> inorder, 
                                int preStart, int inStart, int inEnd) {
        if (preStart >= preorder.size() || inStart > inEnd) {
            return null;
        }
        
        Character rootVal = preorder.get(preStart);
        int rootIndex = -1;
        
        // Find root index in inorder within current bounds
        for (int i = inStart; i <= inEnd; i++) {
            if (inorder.get(i).equals(rootVal)) {
                rootIndex = i;
                break;
            }
        }

        int leftSize = rootIndex - inStart;
        Tree left = buildTreeHelper(preorder, inorder, preStart + 1, inStart, rootIndex - 1);
        Tree right = buildTreeHelper(preorder, inorder, preStart + 1 + leftSize, rootIndex + 1, inEnd);
        
        return new Tree(rootVal, left, right);
    }
}
