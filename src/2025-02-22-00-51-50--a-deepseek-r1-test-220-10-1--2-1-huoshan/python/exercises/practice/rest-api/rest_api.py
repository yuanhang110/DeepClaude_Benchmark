import json
from collections import defaultdict

class RestAPI:
    class User:
        def __init__(self, name):
            self.name = name
            self.owes = defaultdict(float)
            self.owed_by = defaultdict(float)
        
        @property
        def balance(self):
            return sum(self.owed_by.values()) - sum(self.owes.values())
        
        def to_json(self):
            return {
                "name": self.name,
                "owes": {k: round(v, 2) for k, v in self.owes.items() if v > 0},
                "owed_by": {k: round(v, 2) for k, v in self.owed_by.items() if v > 0},
                "balance": round(self.balance, 2)
            }
    
    def __init__(self, database=None):
        self.users = {}
        if database:
            for user_data in database.get('users', []):
                user = self.User(user_data['name'])
                # Initialize owes relationships
                for borrower, amount in user_data['owes'].items():
                    user.owes[borrower] = amount
                # Initialize owed_by relationships
                for lender, amount in user_data['owed_by'].items():
                    user.owed_by[lender] = amount
                self.users[user.name] = user
    
    def get(self, url, payload=None):
        if url == '/users':
            if payload:
                requested_users = json.loads(payload).get('users', [])
                filtered = [u for name, u in self.users.items() if name in requested_users]
            else:
                filtered = list(self.users.values())
            
            filtered.sort(key=lambda u: u.name)
            return json.dumps({"users": [u.to_json() for u in filtered]})
        
        return json.dumps({})
    
    def post(self, url, payload=None):
        if url == '/add':
            data = json.loads(payload) if payload else {}
            name = data.get('user', '')
            if name in self.users:
                return json.dumps({"error": "User already exists"}), 400
            
            new_user = self.User(name)
            self.users[name] = new_user
            return json.dumps(new_user.to_json())
        
        elif url == '/iou':
            data = json.loads(payload) if payload else {}
            lender_name = data.get('lender', '')
            borrower_name = data.get('borrower', '')
            amount = data.get('amount', 0.0)
            
            lender = self.users.get(lender_name)
            borrower = self.users.get(borrower_name)
            
            if not lender or not borrower:
                return json.dumps({"error": "User not found"}), 404
            
            # Update lender's owed_by and borrower's owes
            lender.owed_by[borrower_name] += amount
            borrower.owes[lender_name] += amount
            
            # Simplify mutual debts
            for user1, user2 in [(lender, borrower), (borrower, lender)]:
                debt = min(user1.owed_by.get(user2.name, 0), user2.owed_by.get(user1.name, 0))
                if debt > 0:
                    user1.owed_by[user2.name] -= debt
                    user2.owed_by[user1.name] -= debt
                    if user1.owed_by[user2.name] <= 0:
                        del user1.owed_by[user2.name]
                    if user2.owed_by[user1.name] <= 0:
                        del user2.owed_by[user1.name]
            
            # Return updated users sorted by name
            updated_users = sorted([lender, borrower], key=lambda u: u.name)
            return json.dumps({"users": [u.to_json() for u in updated_users]})
        
        return json.dumps({})
