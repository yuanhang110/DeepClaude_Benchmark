import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Satellite {
    public Tree treeFromTraversals(List<Character> preorderInput, List<Character> inorderInput) {
        if (preorderInput.size() != inorderInput.size()) {
            throw new IllegalArgumentException("traversals must have the same length");
        }
        
        Set<Character> preSet = new HashSet<>(preorderInput);
        Set<Character> inSet = new HashSet<>(inorderInput);
        if (!preSet.equals(inSet)) {
            throw new IllegalArgumentException("traversals must have the same elements");
        }
        
        if (preSet.size() != preorderInput.size()) {
            throw new IllegalArgumentException("traversals must contain unique items");
        }
        
        return buildTree(preorderInput, inorderInput);
    }
    
    private Tree buildTree(List<Character> preorder, List<Character> inorder) {
        if (preorder.isEmpty()) {
            return null;
        }
        
        Character rootVal = preorder.get(0);
        int rootIdx = inorder.indexOf(rootVal);
        
        List<Character> leftInorder = new ArrayList<>(inorder.subList(0, rootIdx));
        List<Character> rightInorder = new ArrayList<>(inorder.subList(rootIdx + 1, inorder.size()));
        
        int leftSize = leftInorder.size();
        List<Character> leftPreorder = new ArrayList<>(preorder.subList(1, 1 + leftSize));
        List<Character> rightPreorder = new ArrayList<>(preorder.subList(1 + leftSize, preorder.size()));
        
        Tree left = buildTree(leftPreorder, leftInorder);
        Tree right = buildTree(rightPreorder, rightInorder);
        
        return new Tree(rootVal, left, right);
    }
}
