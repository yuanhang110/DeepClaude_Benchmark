export class RestAPI {
  constructor(database = {}) {
    this.users = database.users || {};
  }

  _getUserBalance(user) {
    const owedBy = Object.values(user.owed_by || {}).reduce((a, b) => a + b, 0);
    const owes = Object.values(user.owes || {}).reduce((a, b) => a + b, 0);
    return owedBy - owes;
  }

  _createUserResponse(user) {
    return {
      name: user.name,
      owes: user.owes || {},
      owed_by: user.owed_by || {},
      balance: this._getUserBalance(user)
    };
  }

  get(url) {
    const [path, query] = url.split('?');
    if (path === '/users') {
      const params = new URLSearchParams(query);
      const requestedUsers = params.get('users')?.split(',') || [];
      const users = requestedUsers.length > 0
        ? requestedUsers.map(name => this._createUserResponse(this.users[name]))
        : Object.values(this.users).map(user => this._createUserResponse(user));
      return { users: users.sort((a, b) => a.name.localeCompare(b.name)) };
    }
    return { users: [] };
  }

  post(url, payload) {
    if (url === '/add') {
      const newUser = {
        name: payload.user,
        owes: {},
        owed_by: {}
      };
      this.users[payload.user] = newUser;
      return this._createUserResponse(newUser);
    }
    if (url === '/iou') {
      const { lender, borrower, amount } = payload;
      if (!this.users[lender] || !this.users[borrower]) {
        return { users: [] };
      }

      // Update lender's owed_by and owes
      if (this.users[lender].owes[borrower]) {
        this.users[lender].owes[borrower] -= amount;
        if (this.users[lender].owes[borrower] <= 0) {
          delete this.users[lender].owes[borrower];
          this.users[lender].owed_by[borrower] = Math.abs(this.users[lender].owes[borrower] || 0);
        }
      } else {
        this.users[lender].owed_by[borrower] = (this.users[lender].owed_by[borrower] || 0) + amount;
      }

      // Update borrower's owes and owed_by
      if (this.users[borrower].owed_by[lender]) {
        this.users[borrower].owed_by[lender] -= amount;
        if (this.users[borrower].owed_by[lender] <= 0) {
          delete this.users[borrower].owed_by[lender];
          this.users[borrower].owes[lender] = Math.abs(this.users[borrower].owed_by[lender] || 0);
        }
      } else {
        this.users[borrower].owes[lender] = (this.users[borrower].owes[lender] || 0) + amount;
      }

      // Clean up zero balances
      if (this.users[lender].owed_by[borrower] === 0) delete this.users[lender].owed_by[borrower];
      if (this.users[lender].owes[borrower] === 0) delete this.users[lender].owes[borrower];
      if (this.users[borrower].owed_by[lender] === 0) delete this.users[borrower].owed_by[lender];
      if (this.users[borrower].owes[lender] === 0) delete this.users[borrower].owes[lender];

      return {
        users: [this._createUserResponse(this.users[lender]), this._createUserResponse(this.users[borrower])]
          .sort((a, b) => a.name.localeCompare(b.name))
      };
    }
    return { users: [] };
  }
}
