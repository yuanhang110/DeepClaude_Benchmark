import java.util.List;

public class Satellite {
    public Tree treeFromTraversals(List<Character> preorderInput, List<Character> inorderInput) {
        if (preorderInput.size() != inorderInput.size()) {
            throw new IllegalArgumentException("Traversals must have the same length");
        }
        if (preorderInput.isEmpty()) {
            throw new IllegalArgumentException("Traversals must not be empty");
        }
        
        Map<Character, Integer> inMap = new HashMap<>();
        for (int i = 0; i < inorderInput.size(); i++) {
            inMap.put(inorderInput.get(i), i);
        }
        
        Stack<Tree> stack = new Stack<>();
        Tree root = new Tree(preorderInput.get(0));
        stack.push(root);
        
        for (int i = 1; i < preorderInput.size(); i++) {
            Character currentVal = preorderInput.get(i);
            Tree node = new Tree(currentVal);
            Tree parent = null;
            
            while (!stack.isEmpty() && inMap.get(currentVal) < inMap.get(stack.peek().value)) {
                parent = stack.pop();
            }
            
            if (parent == null) {
                parent = stack.peek();
                parent.left = node;
            } else {
                parent.right = node;
            }
            
            stack.push(node);
        }
        
        return root;
    }
}
