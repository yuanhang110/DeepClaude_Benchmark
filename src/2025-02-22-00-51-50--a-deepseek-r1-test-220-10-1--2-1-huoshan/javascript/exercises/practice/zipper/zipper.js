//
// This is only a SKELETON file for the 'Zipper' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export class Zipper {
  constructor(tree, path) {
    this.tree = tree;
    this.path = path;
  }

  static fromTree(tree) {
    return new Zipper(tree, []);
  }

  toTree() {
    let current = this.tree;
    for (let i = this.path.length - 1; i >= 0; i--) {
      const crumb = this.path[i];
      current = crumb.direction === 'left' 
        ? { value: crumb.value, left: current, right: crumb.right }
        : { value: crumb.value, left: crumb.left, right: current };
    }
    return current;
  }

  value() {
    return this.tree?.value ?? null;
  }

  left() {
    if (!this.tree?.left) return null;
    return new Zipper(this.tree.left, [
      ...this.path,
      { value: this.tree.value, right: this.tree.right, direction: 'left' }
    ]);
  }

  right() {
    if (!this.tree?.right) return null;
    return new Zipper(this.tree.right, [
      ...this.path,
      { value: this.tree.value, left: this.tree.left, direction: 'right' }
    ]);
  }

  up() {
    if (this.path.length === 0) return null;
    const crumb = this.path[this.path.length - 1];
    const parent = crumb.direction === 'left'
      ? { value: crumb.value, left: this.tree, right: crumb.right }
      : { value: crumb.value, left: crumb.left, right: this.tree };
    return new Zipper(parent, this.path.slice(0, -1));
  }

  setValue(value) {
    return new Zipper(
      { value, left: this.tree?.left, right: this.tree?.right },
      this.path
    );
  }

  setLeft(left) {
    return new Zipper(
      { value: this.tree.value, left, right: this.tree.right },
      this.path
    );
  }

  setRight(right) {
    return new Zipper(
      { value: this.tree.value, left: this.tree.left, right },
      this.path
    );
  }
}
