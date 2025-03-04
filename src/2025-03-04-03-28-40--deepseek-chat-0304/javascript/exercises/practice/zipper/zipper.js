export class Zipper {
  constructor(tree, path = []) {
    this.tree = tree;
    this.path = path;
  }

  static fromTree(tree) {
    return new Zipper(tree);
  }

  toTree() {
    return this.path.length ? this.path[0].tree : this.tree;
  }

  value() {
    return this.tree?.value;
  }

  left() {
    if (!this.tree?.left) return null;
    return new Zipper(this.tree.left, [...this.path, { tree: this.tree, dir: 'left' }]);
  }

  right() {
    if (!this.tree?.right) return null;
    return new Zipper(this.tree.right, [...this.path, { tree: this.tree, dir: 'right' }]);
  }

  up() {
    if (!this.path.length) return null;
    const parent = this.path[this.path.length - 1];
    return new Zipper(parent.tree, this.path.slice(0, -1));
  }

  setValue(value) {
    const newTree = { ...this.tree, value };
    if (!this.path.length) return new Zipper(newTree);
    
    // Reconstruct the tree from the bottom up
    let currentTree = newTree;
    for (let i = this.path.length - 1; i >= 0; i--) {
      const { tree, dir } = this.path[i];
      currentTree = { ...tree, [dir]: currentTree };
    }
    
    return new Zipper(currentTree);
  }

  setLeft(left) {
    const newTree = { ...this.tree, left };
    if (!this.path.length) return new Zipper(newTree);
    
    const parent = this.path[this.path.length - 1];
    const updatedParent = { ...parent.tree };
    updatedParent[parent.dir] = newTree;
    
    return new Zipper(updatedParent, this.path.slice(0, -1)).down(parent.dir);
  }

  setRight(right) {
    const newTree = { ...this.tree, right };
    if (!this.path.length) return new Zipper(newTree);
    
    const parent = this.path[this.path.length - 1];
    const updatedParent = { ...parent.tree };
    updatedParent[parent.dir] = newTree;
    
    return new Zipper(updatedParent, this.path.slice(0, -1)).down(parent.dir);
  }

  // Helper method to navigate down after updating parent
  down(dir) {
    return new Zipper(this.tree[dir], [...this.path, { tree: this.tree, dir }]);
  }
}
