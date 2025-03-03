import java.util.function.Consumer;
import java.util.List;
import java.util.ArrayList;

public class Tree {
    public Character value;
    public Tree left;
    public Tree right;
    
    public Tree(Character value) {
        this.value = value;
        this.left = null;
        this.right = null;
    }
    
    public List<Character> inorder() {
        List<Character> inorder = new ArrayList<>();
        inorder(this, inorder::add);
        return inorder;
    }

    private void inorder(Tree node, Consumer<Character> emitter) {
        if (node == null) {
            return;
        }
        inorder(node.left, emitter);
        emitter.accept(node.value);
        inorder(node.right, emitter);
    }

    public List<Character> preorder() {
        List<Character> preorder = new ArrayList<>();
        preorder(this, preorder::add);
        return preorder;
    }

    private void preorder(Tree node, Consumer<Character> emitter) {
        if (node == null) {
            return;
        }
        emitter.accept(node.value);
        preorder(node.left, emitter);
        preorder(node.right, emitter);
    }

    public List<Character> postorder() {
        List<Character> postorder = new ArrayList<>();
        postorder(this, postorder::add);
        return postorder;
    }

    private void postorder(Tree node, Consumer<Character> emitter) {
        if (node == null) {
            return;
        }
        postorder(node.left, emitter);
        postorder(node.right, emitter);
        emitter.accept(node.value);
    }
}
