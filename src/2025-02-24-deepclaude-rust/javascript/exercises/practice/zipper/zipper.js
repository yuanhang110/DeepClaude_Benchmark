//
// This is only a SKELETON file for the 'Zipper' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export class Zipper {
  constructor(focus, crumbs) {
    this.focus = focus;
    this.crumbs = crumbs;
  }

  static fromTree(tree) {
    return new Zipper(tree, []);
  }

  toTree() {
    let current = this;
    while (current.crumbs.length > 0) {
      current = current.up();
      if (!current) break;
    }
    return current ? current.focus : null;
  }

  value() {
    return this.focus.value;
  }

  left() {
    if (!this.focus.left) return null;
    const crumb = {
      dir: 'left',
      value: this.focus.value,
      right: this.focus.right,
    };
    return new Zipper(this.focus.left, [...this.crumbs, crumb]);
  }

  right() {
    if (!this.focus.right) return null;
    const crumb = {
      dir: 'right',
      value: this.focus.value,
      left: this.focus.left,
    };
    return new Zipper(this.focus.right, [...this.crumbs, crumb]);
  }

  up() {
    if (this.crumbs.length === 0) return null;
    const crumb = this.crumbs[this.crumbs.length - 1];
    const remainingCrumbs = this.crumbs.slice(0, -1);

    let newFocus;
    if (crumb.dir === 'left') {
      newFocus = {
        value: crumb.value,
        left: this.focus,
        right: crumb.right,
      };
    } else {
      newFocus = {
        value: crumb.value,
        left: crumb.left,
        right: this.focus,
      };
    }

    return new Zipper(newFocus, remainingCrumbs);
  }

  setValue(value) {
    const newFocus = {
      value: value,
      left: this.focus.left,
      right: this.focus.right,
    };
    return new Zipper(newFocus, this.crumbs);
  }

  setLeft(left) {
    const newFocus = {
      value: this.focus.value,
      left: left,
      right: this.focus.right,
    };
    return new Zipper(newFocus, this.crumbs);
  }

  setRight(right) {
    const newFocus = {
      value: this.focus.value,
      left: this.focus.left,
      right: right,
    };
    return new Zipper(newFocus, this.crumbs);
  }
}
