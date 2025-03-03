//
// This is only a SKELETON file for the 'Zipper' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export class Zipper {
  constructor(tree, breadcrumbs) {
    this.tree = tree;
    this.breadcrumbs = breadcrumbs;
  }

  static fromTree(tree) {
    return new Zipper(tree, []);
  }

  toTree() {
    let zipper = this;
    while (zipper.breadcrumbs.length > 0) {
      zipper = zipper.up();
      if (!zipper) return null;
    }
    return zipper.tree;
  }

  value() {
    return this.tree?.value ?? null;
  }

  left() {
    if (!this.tree?.left) return null;
    const newTree = this.tree.left;
    const newBreadcrumbs = [...this.breadcrumbs, { parentValue: this.tree.value, right: this.tree.right }];
    return new Zipper(newTree, newBreadcrumbs);
  }

  right() {
    if (!this.tree?.right) return null;
    const newTree = this.tree.right;
    const newBreadcrumbs = [...this.breadcrumbs, { parentValue: this.tree.value, left: this.tree.left }];
    return new Zipper(newTree, newBreadcrumbs);
  }

  up() {
    if (this.breadcrumbs.length === 0) return null;
    const lastCrumb = this.breadcrumbs[this.breadcrumbs.length - 1];
    let newTree;
    if ('right' in lastCrumb) {
      // We moved left previously, so parent's left is current tree
      newTree = {
        value: lastCrumb.parentValue,
        left: this.tree,
        right: lastCrumb.right
      };
    } else {
      // We moved right previously, so parent's right is current tree
      newTree = {
        value: lastCrumb.parentValue,
        left: lastCrumb.left,
        right: this.tree
      };
    }
    const newBreadcrumbs = this.breadcrumbs.slice(0, -1);
    return new Zipper(newTree, newBreadcrumbs);
  }

  setValue(value) {
    if (!this.tree) return null;
    const newTree = { ...this.tree, value };
    return new Zipper(newTree, this.breadcrumbs);
  }

  setLeft(left) {
    if (!this.tree) return null;
    const newTree = { ...this.tree, left };
    return new Zipper(newTree, this.breadcrumbs);
  }

  setRight(right) {
    if (!this.tree) return null;
    const newTree = { ...this.tree, right };
    return new Zipper(newTree, this.breadcrumbs);
  }
}
