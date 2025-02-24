//
// This is only a SKELETON file for the 'Rest API' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export class RestAPI {
  constructor(database = { users: {} }) {
    this.db = database;
    if (!this.db.users) {
      this.db.users = {};
    }
  }

  get(url) {
    if (url.startsWith('/users')) {
      const queryPart = url.split('?')[1] || '';
      const params = new URLSearchParams(queryPart);
      const requestedNames = params.get('users') ? params.get('users').split(',') : [];
      
      const users = requestedNames.length 
        ? requestedNames.map(name => this.db.users[name]).filter(user => user !== undefined)
        : Object.values(this.db.users);
      
      const processedUsers = users.map(user => {
        const owedByTotal = Object.values(user.owed_by || {}).reduce((sum, val) => sum + val, 0);
        const owesTotal = Object.values(user.owes || {}).reduce((sum, val) => sum + val, 0);
        const balance = owedByTotal - owesTotal;
        
        return {
          name: user.name,
          owes: user.owes || {},
          owed_by: user.owed_by || {},
          balance: parseFloat(balance.toFixed(2))
        };
      }).sort((a, b) => a.name.localeCompare(b.name));
      
      return { users: processedUsers };
    }
    throw new Error(`Invalid endpoint: ${url}`);
  }

  post(url, payload) {
    if (url === '/add') {
      const userName = payload.user;
      if (this.db.users[userName]) {
        throw new Error('User already exists');
      }
      
      const newUser = {
        name: userName,
        owes: {},
        owed_by: {},
        balance: 0.0
      };
      
      this.db.users[userName] = newUser;
      return newUser;
    }
    
    if (url === '/iou') {
      const { lender, borrower, amount } = payload;
      if (!this.db.users[lender] || !this.db.users[borrower]) {
        throw new Error('User not found');
      }
      
      // Update lender's owed_by and borrower's owes
      const lenderUser = this.db.users[lender];
      const borrowerUser = this.db.users[borrower];
      
      // Calculate net debt
      const existingDebtFromBorrower = lenderUser.owed_by[borrower] || 0;
      const existingDebtToLender = borrowerUser.owes[lender] || 0;
      
      // Calculate new debt
      let newLenderDebt = existingDebtFromBorrower - existingDebtToLender + amount;
      let newBorrowerDebt = existingDebtToLender - existingDebtFromBorrower - amount;
      
      // Update lender's records
      if (newLenderDebt > 0) {
        lenderUser.owed_by[borrower] = newLenderDebt;
        delete lenderUser.owes[borrower];
      } else if (newLenderDebt < 0) {
        lenderUser.owes[borrower] = -newLenderDebt;
        delete lenderUser.owed_by[borrower];
      } else {
        delete lenderUser.owed_by[borrower];
        delete lenderUser.owes[borrower];
      }
      
      // Update borrower's records
      if (newBorrowerDebt > 0) {
        borrowerUser.owes[lender] = newBorrowerDebt;
        delete borrowerUser.owed_by[lender];
      } else if (newBorrowerDebt < 0) {
        borrowerUser.owed_by[lender] = -newBorrowerDebt;
        delete borrowerUser.owes[lender];
      } else {
        delete borrowerUser.owed_by[lender];
        delete borrowerUser.owes[lender];
      }
      
      // Prepare response with updated users
      const responseUsers = [lenderUser, borrowerUser].map(user => {
        const owedTotal = Object.values(user.owed_by || {}).reduce((sum, val) => sum + val, 0);
        const owesTotal = Object.values(user.owes || {}).reduce((sum, val) => sum + val, 0);
        return {
          name: user.name,
          owes: { ...user.owes },
          owed_by: { ...user.owed_by },
          balance: parseFloat((owedTotal - owesTotal).toFixed(2))
        };
      }).sort((a, b) => a.name.localeCompare(b.name));
      
      return { users: responseUsers };
    }
    
    throw new Error(`Invalid endpoint: ${url}`);
  }
}
