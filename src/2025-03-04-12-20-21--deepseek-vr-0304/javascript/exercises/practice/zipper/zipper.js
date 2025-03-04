//
// This is only a SKELETON file for the 'Zipper' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export class Zipper {
  constructor(tree, path = []) {
    this.tree = tree;
    this.path = path;
  }

  static fromTree(tree) {
    return new Zipper(tree);
  }

  toTree() {
    let current = this;
    while (current.path.length > 0) {
      current = current.path[0];
    }
    return current.tree;
  }

  value() {
    return this.tree ? this.tree.value : null;
  }

  left() {
    if (!this.tree || !this.tree.left) return null;
    const path = [{
      value: this.tree.value,
      left: null,
      right: this.tree.right,
      prev: this.path[0]
    }, ...this.path];
    return new Zipper(this.tree.left, path);
  }

  right() {
    if (!this.tree || !this.tree.right) return null;
    const path = [{
      value: this.tree.value,
      left: this.tree.left,
      right: null,
      prev: this.path[0]
    }, ...this.path];
    return new Zipper(this.tree.right, path);
  }

  up() {
    if (this.path.length === 0) return null;
    const [current, ...rest] = this.path;
    const newTree = {
      value: current.value,
      left: current.left || null,
      right: current.right || null
    };
    if (this.tree === current.left) {
      newTree.left = this.tree;
    } else {
      newTree.right = this.tree;
    }
    return new Zipper(newTree, rest);
  }

  setValue(value) {
    const newTree = {
      value: value,
      left: this.tree.left,
      right: this.tree.right
    };
    return new Zipper(newTree, this.path);
  }

  setLeft(tree) {
    const newTree = {
      value: this.tree.value,
      left: tree,
      right: this.tree.right
    };
    return new Zipper(newTree, this.path);
  }

  setRight(tree) {
    const newTree = {
      value: this.tree.value,
      left: this.tree.left,
      right: tree
    };
    return new Zipper(newTree, this.path);
  }
}
