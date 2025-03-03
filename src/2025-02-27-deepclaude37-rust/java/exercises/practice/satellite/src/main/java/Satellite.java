import java.util.*;

public class Satellite {
    public Tree treeFromTraversals(List<Character> preorderInput, List<Character> inorderInput) {
        if (preorderInput == null || inorderInput == null) {
            throw new IllegalArgumentException("Traversals must not be null");
        }

        if (preorderInput.size() != inorderInput.size()) {
            throw new IllegalArgumentException("traversals must have the same length");
        }

        Set<Character> preSet = new HashSet<>(preorderInput);
        Set<Character> inSet = new HashSet<>(inorderInput);
        if (!preSet.equals(inSet)) {
            throw new IllegalArgumentException("traversals must contain the same elements");
        }

        if (preorderInput.isEmpty()) {
            return new Tree(null);
        }

        Node root = buildTree(preorderInput, inorderInput);
        return new Tree(root);
    }

    private Node buildTree(List<Character> preorder, List<Character> inorder) {
        if (preorder.isEmpty()) {
            return null;
        }

        Character rootVal = preorder.get(0);
        Node root = new Node(rootVal);

        int rootIndex = inorder.indexOf(rootVal);
        if (rootIndex == -1) {
            throw new IllegalArgumentException("Root not found in inorder traversal");
        }

        List<Character> leftInorder = new ArrayList<>(inorder.subList(0, rootIndex));
        List<Character> rightInorder = new ArrayList<>(inorder.subList(rootIndex + 1, inorder.size()));

        int leftSize = leftInorder.size();

        List<Character> leftPreorder = new ArrayList<>(preorder.subList(1, 1 + leftSize));
        List<Character> rightPreorder = new ArrayList<>(preorder.subList(1 + leftSize, preorder.size()));

        root.left = buildTree(leftPreorder, leftInorder);
        root.right = buildTree(rightPreorder, rightInorder);

        return root;
    }
}
