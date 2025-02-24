//
// This is only a SKELETON file for the 'Zipper' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export class Zipper {
  constructor(tree, breadcrumbs) {
    this._tree = tree;
    this._breadcrumbs = breadcrumbs;
  }

  static fromTree(tree) {
    return new Zipper(tree, []);
  }

  toTree() {
    let current = this;
    while (current._breadcrumbs.length > 0) {
      current = current.up();
    }
    return current._tree;
  }

  value() {
    return this._tree?.value ?? null;
  }

  left() {
    if (!this._tree?.left) return null;
    return new Zipper(this._tree.left, [
      ...this._breadcrumbs,
      {
        value: this._tree.value,
        right: this._tree.right,
        direction: 'left'
      }
    ]);
  }

  right() {
    if (!this._tree?.right) return null;
    return new Zipper(this._tree.right, [
      ...this._breadcrumbs,
      {
        value: this._tree.value,
        left: this._tree.left,
        direction: 'right'
      }
    ]);
  }

  up() {
    if (this._breadcrumbs.length === 0) return null;
    const crumb = this._breadcrumbs[this._breadcrumbs.length - 1];
    const newBreadcrumbs = this._breadcrumbs.slice(0, -1);
    
    const parentTree = {
      value: crumb.value,
      left: crumb.direction === 'left' ? this._tree : crumb.left,
      right: crumb.direction === 'right' ? this._tree : crumb.right
    };
    
    return new Zipper(parentTree, newBreadcrumbs);
  }

  setValue(value) {
    if (!this._tree) return this;
    const newTree = { ...this._tree, value };
    return new Zipper(newTree, this._breadcrumbs);
  }

  setLeft(left) {
    if (!this._tree) return this;
    const newTree = { ...this._tree, left };
    return new Zipper(newTree, this._breadcrumbs);
  }

  setRight(right) {
    if (!this._tree) return this;
    const newTree = { ...this._tree, right };
    return new Zipper(newTree, this._breadcrumbs);
  }
}
