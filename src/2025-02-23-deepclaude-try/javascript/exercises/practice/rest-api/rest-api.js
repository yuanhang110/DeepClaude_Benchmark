//
// This is only a SKELETON file for the 'Rest API' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export class RestAPI {
  constructor(database = {}) {
    this.users = {};
    if (database.users) {
      database.users.forEach(user => {
        this.users[user.name] = user;
      });
    }
  }

  get(url) {
    const urlParts = new URL(url, `http://localhost`).searchParams;
    const usersParam = urlParts.get('users');
    let responseUsers = Object.values(this.users);

    if (usersParam) {
      const requestedUsers = usersParam.split(',');
      responseUsers = responseUsers.filter(user => requestedUsers.includes(user.name));
    }

    return {
      users: responseUsers.sort((a, b) => a.name.localeCompare(b.name))
    };
  }

  post(url, payload) {
    if (url === '/add') {
      const { user } = payload;
      if (!this.users[user]) {
        this.users[user] = { name: user, owes: {}, owed_by: {}, balance: 0 };
      }
      return this.users[user];
    } else if (url === '/iou') {
      const { lender, borrower, amount } = payload;
      if (!this.users[lender]) {
        this.users[lender] = { name: lender, owes: {}, owed_by: {}, balance: 0 };
      }
      if (!this.users[borrower]) {
        this.users[borrower] = { name: borrower, owes: {}, owed_by: {}, balance: 0 };
      }
      this._updateUser (lender, borrower, amount);
      return {
        users: [this.users[lender], this.users[borrower]].sort((a, b) => a.name.localeCompare(b.name))
      };
    }
  }

  _updateUser(lender, borrower, amount) {
    // First check if there are existing debts in either direction
    const existingLenderOwes = this.users[lender].owes[borrower] || 0;
    const existingBorrowerOwes = this.users[borrower].owes[lender] || 0;
    
    // Calculate the net change after the new loan
    if (existingLenderOwes > 0) {
      // If lender already owes borrower
      if (amount > existingLenderOwes) {
        // New loan is larger than existing debt
        delete this.users[lender].owes[borrower];
        delete this.users[borrower].owed_by[lender];
        const netAmount = amount - existingLenderOwes;
        this.users[borrower].owes[lender] = netAmount;
        this.users[lender].owed_by[borrower] = netAmount;
      } else {
        // New loan reduces existing debt
        const netAmount = existingLenderOwes - amount;
        if (netAmount === 0) {
          delete this.users[lender].owes[borrower];
          delete this.users[borrower].owed_by[lender];
        } else {
          this.users[lender].owes[borrower] = netAmount;
          this.users[borrower].owed_by[lender] = netAmount;
        }
      }
    } else if (existingBorrowerOwes > 0) {
      // If borrower already owes lender
      const newTotal = existingBorrowerOwes + amount;
      this.users[borrower].owes[lender] = newTotal;
      this.users[lender].owed_by[borrower] = newTotal;
    } else {
      // No existing debts
      this.users[borrower].owes[lender] = amount;
      this.users[lender].owed_by[borrower] = amount;
    }

    // Recalculate balances
    for (const user of [lender, borrower]) {
      const owedByTotal = Object.values(this.users[user].owed_by).reduce((sum, val) => sum + val, 0);
      const owesTotal = Object.values(this.users[user].owes).reduce((sum, val) => sum + val, 0);
      this.users[user].balance = owedByTotal - owesTotal;
    }
  }
}
