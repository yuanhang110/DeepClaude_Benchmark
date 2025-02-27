//
// This is only a SKELETON file for the 'Rest API' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export class RestAPI {
  constructor(database = { users: [] }) {
    this.db = database.users ? database : { users: [] };
  }

  get(url) {
    if (url.startsWith('/users')) {
      const match = url.match(/\?users=(.*)/);
      const users = match ? match[1].split(',') : null;
      
      let result = this.db.users;
      if (users) {
        result = result.filter(user => users.includes(user.name));
      }
      return { users: result.sort((a, b) => a.name.localeCompare(b.name)) };
    }
    throw new Error('Invalid URL');
  }

  post(url, payload) {
    if (url === '/add') {
      const newUser = {
        name: payload.user,
        owes: {},
        owed_by: {},
        balance: 0
      };
      
      if (this.db.users.some(user => user.name === payload.user)) {
        throw new Error('User already exists');
      }
      
      this.db.users.push(newUser);
      return newUser;
    }
    
    if (url === '/iou') {
      const { lender, borrower, amount } = payload;
      let lenderUser = this.db.users.find(u => u.name === lender);
      let borrowerUser = this.db.users.find(u => u.name === borrower);
      
      if (!lenderUser || !borrowerUser) {
        throw new Error('User not found');
      }

      // Handle existing debts in opposite direction
      const existingDebt = borrowerUser.owed_by[lender] || 0;
      if (existingDebt > 0) {
        if (existingDebt >= amount) {
          // Reduce existing debt
          borrowerUser.owed_by[lender] -= amount;
          lenderUser.owes[borrower] -= amount;
          if (borrowerUser.owed_by[lender] === 0) {
            delete borrowerUser.owed_by[lender];
            delete lenderUser.owes[borrower];
          }
        } else {
          // Clear existing debt and create new debt in opposite direction
          delete borrowerUser.owed_by[lender];
          delete lenderUser.owes[borrower];
          const remaining = amount - existingDebt;
          borrowerUser.owes[lender] = remaining;
          lenderUser.owed_by[borrower] = remaining;
        }
      } else {
        // Add to existing debt or create new debt
        borrowerUser.owes[lender] = (borrowerUser.owes[lender] || 0) + amount;
        lenderUser.owed_by[borrower] = (lenderUser.owed_by[borrower] || 0) + amount;
      }

      // Update balances
      lenderUser.balance = Object.values(lenderUser.owed_by).reduce((sum, val) => sum + val, 0) -
                          Object.values(lenderUser.owes).reduce((sum, val) => sum + val, 0);
      borrowerUser.balance = Object.values(borrowerUser.owed_by).reduce((sum, val) => sum + val, 0) -
                            Object.values(borrowerUser.owes).reduce((sum, val) => sum + val, 0);

      return {
        users: [lenderUser, borrowerUser].sort((a, b) => a.name.localeCompare(b.name))
      };
    }
    
    throw new Error('Invalid URL');
  }
}
