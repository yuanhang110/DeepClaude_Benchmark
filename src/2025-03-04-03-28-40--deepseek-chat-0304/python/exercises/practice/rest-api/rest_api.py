import json
from collections import defaultdict

class RestAPI:
    def __init__(self, database=None):
        self.users = {}
        if database:
            for user_data in database['users']:
                self.users[user_data['name']] = {
                    'owes': defaultdict(float, user_data.get('owes', {})),
                    'owed_by': defaultdict(float, user_data.get('owed_by', {}))
                }
                self._update_balance(user_data['name'])

    def _update_balance(self, name):
        """Calculate and update user's balance"""
        user = self.users[name]
        total_owed = sum(user['owes'].values())
        total_owed_by = sum(user['owed_by'].values())
        user['balance'] = round(total_owed_by - total_owed, 2)

    def _get_user_json(self, name):
        """Get user data in JSON format"""
        user = self.users[name]
        return {
            "name": name,
            "owes": dict(user['owes']),
            "owed_by": dict(user['owed_by']),
            "balance": user['balance']
        }

    def get(self, url, payload=None):
        if url == '/users':
            if payload:
                names = json.loads(payload)['users']
                users = [self._get_user_json(name) for name in sorted(names) if name in self.users]
            else:
                users = [self._get_user_json(name) for name in sorted(self.users.keys())]
            return json.dumps({"users": users})
        return ''

    def post(self, url, payload=None):
        if not payload:
            return ''
            
        data = json.loads(payload)
        if url == '/add':
            name = data['user']
            if name not in self.users:
                self.users[name] = {
                    'owes': defaultdict(float),
                    'owed_by': defaultdict(float),
                    'balance': 0.0
                }
            return json.dumps(self._get_user_json(name))
            
        elif url == '/iou':
            lender = data['lender']
            borrower = data['borrower']
            amount = data['amount']
            
            # Check if lender already owes borrower
            if borrower in self.users[lender]['owes']:
                owed_amount = self.users[lender]['owes'][borrower]
                if amount >= owed_amount:
                    # Clear existing debt and create new IOU
                    del self.users[lender]['owes'][borrower]
                    del self.users[borrower]['owed_by'][lender]
                    new_amount = amount - owed_amount
                    if new_amount > 0:
                        self.users[lender]['owed_by'][borrower] = new_amount
                        self.users[borrower]['owes'][lender] = new_amount
                else:
                    # Reduce existing debt
                    self.users[lender]['owes'][borrower] -= amount
                    self.users[borrower]['owed_by'][lender] -= amount
            else:
                # Create new IOU
                self.users[lender]['owed_by'][borrower] += amount
                self.users[borrower]['owes'][lender] += amount
            
            # Update balances
            self._update_balance(lender)
            self._update_balance(borrower)
            
            return json.dumps({
                "users": sorted([
                    self._get_user_json(lender),
                    self._get_user_json(borrower)
                ], key=lambda x: x['name'])
            })
        return ''
