//
// This is only a SKELETON file for the 'Rest API' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export class RestAPI {
  constructor(database = {}) {
    this.database = { ...database };
    if (!this.database.users) {
      this.database.users = {};
    }
  }

  get(url) {
    if (url.startsWith('/users')) {
      const query = url.includes('?') ? new URLSearchParams(url.split('?')[1]) : new URLSearchParams();
      const requested = query.get('users') ? query.get('users').split(',') : [];
      const users = Object.values(this.database.users);
      const filtered = requested.length ? users.filter(u => requested.includes(u.name)) : users;
      const sorted = filtered.sort((a, b) => a.name.localeCompare(b.name));
      
      const result = sorted.map(user => {
        const owedByTotal = Object.values(user.owed_by || {}).reduce((sum, val) => sum + val, 0);
        const owesTotal = Object.values(user.owes || {}).reduce((sum, val) => sum + val, 0);
        return {
          name: user.name,
          owes: { ...user.owes },
          owed_by: { ...user.owed_by },
          balance: owedByTotal - owesTotal
        };
      });

      return { users: result };
    }
    throw new Error('Invalid endpoint');
  }

  post(url, payload) {
    if (url === '/add') {
      const { user } = payload;
      if (this.database.users[user]) {
        throw new Error('User already exists');
      }
      const newUser = {
        name: user,
        owes: {},
        owed_by: {},
      };
      this.database.users[user] = newUser;
      return {
        name: newUser.name,
        owes: {},
        owed_by: {},
        balance: 0
      };
    }

    if (url === '/iou') {
      const { lender, borrower, amount } = payload;
      
      // Initialize users if they don't exist yet
      if (!this.database.users[lender]) {
        this.database.users[lender] = { name: lender, owes: {}, owed_by: {} };
      }
      
      if (!this.database.users[borrower]) {
        this.database.users[borrower] = { name: borrower, owes: {}, owed_by: {} };
      }

      const lenderUser = this.database.users[lender];
      const borrowerUser = this.database.users[borrower];

      // Add amount to borrower's debt to lender
      borrowerUser.owes[lender] = (borrowerUser.owes[lender] || 0) + amount;
      
      // Add amount to lender's credit from borrower
      lenderUser.owed_by[borrower] = (lenderUser.owed_by[borrower] || 0) + amount;
      
      // Calculate net debts between the two users and adjust if needed
      this._normalizeDebt(lenderUser, borrowerUser);

      // Return updated users sorted by name
      const updated = [lenderUser, borrowerUser].sort((a, b) => a.name.localeCompare(b.name));
      return { 
        users: updated.map(user => {
          const owedByTotal = Object.values(user.owed_by || {}).reduce((sum, val) => sum + val, 0);
          const owesTotal = Object.values(user.owes || {}).reduce((sum, val) => sum + val, 0);
          return {
            name: user.name,
            owes: { ...user.owes },
            owed_by: { ...user.owed_by },
            balance: owedByTotal - owesTotal
          };
        })
      };
    }

    throw new Error('Invalid endpoint');
  }
  
  _normalizeDebt(user1, user2) {
    const name1 = user1.name;
    const name2 = user2.name;
    
    // Ensure owes and owed_by properties exist
    if (!user1.owes) user1.owes = {};
    if (!user1.owed_by) user1.owed_by = {};
    if (!user2.owes) user2.owes = {};
    if (!user2.owed_by) user2.owed_by = {};
    
    // If both users owe each other, calculate the net
    if (user1.owes[name2] && user2.owes[name1]) {
      if (user1.owes[name2] > user2.owes[name1]) {
        // User1 owes User2 more, so subtract User2's debt
        user1.owes[name2] -= user2.owes[name1];
        user2.owed_by[name1] = user1.owes[name2];
        delete user2.owes[name1];
        delete user1.owed_by[name2];
      } else if (user1.owes[name2] < user2.owes[name1]) {
        // User2 owes User1 more, so subtract User1's debt
        user2.owes[name1] -= user1.owes[name2];
        user1.owed_by[name2] = user2.owes[name1];
        delete user1.owes[name2];
        delete user2.owed_by[name1];
      } else {
        // Debts cancel out exactly
        delete user1.owes[name2];
        delete user2.owes[name1];
        delete user1.owed_by[name2];
        delete user2.owed_by[name1];
      }
    }
  }
}
