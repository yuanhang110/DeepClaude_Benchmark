export class RestAPI {
  constructor(database = { users: [] }) {
    this.users = database.users.reduce((acc, user) => {
      acc[user.name] = {
        ...user,
        owes: user.owes || {},
        owed_by: user.owed_by || {},
        balance: (user.owed_by ? Object.values(user.owed_by).reduce((a, b) => a + b, 0) : 0) -
                 (user.owes ? Object.values(user.owes).reduce((a, b) => a + b, 0) : 0)
      };
      return acc;
    }, {});
  }

  get(url) {
    const [path, params] = url.split('?');
    if (path === '/users') {
      const query = params ? new URLSearchParams(params) : null;
      const names = query ? query.get('users')?.split(',') : null;
      const users = names ? 
        names.map(name => this.getUser(name)).filter(Boolean) :
        Object.values(this.users);
      return {
        users: users.sort((a, b) => a.name.localeCompare(b.name))
      };
    }
    throw new Error('Invalid endpoint');
  }

  post(url, payload) {
    switch (url) {
      case '/add':
        return this.addUser(payload.user);
      case '/iou':
        return this.addIOU(payload.lender, payload.borrower, payload.amount);
      default:
        throw new Error('Invalid endpoint');
    }
  }

  getUser(name) {
    const user = this.users[name];
    if (!user) return null;
    return {
      name: user.name,
      owes: { ...user.owes },
      owed_by: { ...user.owed_by },
      balance: user.balance
    };
  }

  addUser(name) {
    if (this.users[name]) {
      throw new Error('User already exists');
    }
    this.users[name] = {
      name,
      owes: {},
      owed_by: {},
      balance: 0
    };
    return this.getUser(name);
  }

  addIOU(lender, borrower, amount) {
    if (!this.users[lender] || !this.users[borrower]) {
      throw new Error('User not found');
    }

    // Update lender's owed_by
    this.users[lender].owed_by[borrower] = 
      (this.users[lender].owed_by[borrower] || 0) + amount;
    this.users[lender].balance += amount;

    // Update borrower's owes
    this.users[borrower].owes[lender] = 
      (this.users[borrower].owes[lender] || 0) + amount;
    this.users[borrower].balance -= amount;

    // Simplify debts if possible
    const lenderOwed = this.users[lender].owed_by[borrower] || 0;
    const borrowerOwes = this.users[borrower].owes[lender] || 0;
    
    if (lenderOwed > 0 && borrowerOwes > 0) {
      if (lenderOwed > borrowerOwes) {
        this.users[lender].owed_by[borrower] -= borrowerOwes;
        delete this.users[borrower].owes[lender];
      } else if (lenderOwed < borrowerOwes) {
        this.users[borrower].owes[lender] -= lenderOwed;
        delete this.users[lender].owed_by[borrower];
      } else {
        delete this.users[lender].owed_by[borrower];
        delete this.users[borrower].owes[lender];
      }
    }

    return {
      users: [this.getUser(lender), this.getUser(borrower)]
        .sort((a, b) => a.name.localeCompare(b.name))
    };
  }
}
