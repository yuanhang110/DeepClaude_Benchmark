import json

class RestAPI:
    def __init__(self, database=None):
        self.database = {}
        if database:
            for user in database['users']:
                name = user['name']
                self.database[name] = {
                    'name': name,
                    'owes': user.get('owes', {}),
                    'owed_by': user.get('owed_by', {}),
                    'balance': 0.0  # Will be calculated when needed
                }

    def get(self, url, payload=None):
        if url == '/users':
            users = []
            if payload:
                data = json.loads(payload)
                requested_names = data.get('users', [])
                for name in requested_names:
                    if name in self.database:
                        user = self._get_user_with_balance(name)
                        users.append(user)
            else:
                for name in self.database:
                    user = self._get_user_with_balance(name)
                    users.append(user)
            
            # Sort users by name
            users.sort(key=lambda x: x['name'])
            return json.dumps({"users": users})
        
        return json.dumps({})

    def post(self, url, payload=None):
        if not payload:
            return json.dumps({})
            
        data = json.loads(payload)
        
        if url == '/add':
            name = data['user']
            if name not in self.database:
                self.database[name] = {
                    'name': name,
                    'owes': {},
                    'owed_by': {},
                }
            
            return json.dumps(self._get_user_with_balance(name))
            
        elif url == '/iou':
            lender = data['lender']
            borrower = data['borrower']
            amount = float(data['amount'])
            
            # Handle the lender's records
            if borrower in self.database[lender]['owes']:
                # If lender already owes borrower, reduce that debt first
                if self.database[lender]['owes'][borrower] >= amount:
                    self.database[lender]['owes'][borrower] -= amount
                    if self.database[lender]['owes'][borrower] == 0:
                        del self.database[lender]['owes'][borrower]
                    
                    # Reduce borrower's owed_by accordingly
                    self.database[borrower]['owed_by'][lender] -= amount
                    if self.database[borrower]['owed_by'][lender] == 0:
                        del self.database[borrower]['owed_by'][lender]
                    
                    amount = 0
                else:
                    # Partial debt reduction
                    amount -= self.database[lender]['owes'][borrower]
                    
                    # Remove the debt entirely
                    del self.database[borrower]['owed_by'][lender]
                    del self.database[lender]['owes'][borrower]
            
            # If there's still amount to be recorded
            if amount > 0:
                # Update lender's records
                self.database[lender]['owed_by'].setdefault(borrower, 0)
                self.database[lender]['owed_by'][borrower] += amount
                
                # Update borrower's records
                self.database[borrower]['owes'].setdefault(lender, 0)
                self.database[borrower]['owes'][lender] += amount
            
            # Prepare response with updated user information
            users = [
                self._get_user_with_balance(lender),
                self._get_user_with_balance(borrower)
            ]
            users.sort(key=lambda x: x['name'])
            
            return json.dumps({"users": users})
        
        return json.dumps({})
    
    def _get_user_with_balance(self, name):
        """Helper method to get a user with calculated balance"""
        user = dict(self.database[name])
        # Calculate balance: sum of owed_by minus sum of owes
        owed_by_sum = sum(user['owed_by'].values()) if user['owed_by'] else 0
        owes_sum = sum(user['owes'].values()) if user['owes'] else 0
        user['balance'] = owed_by_sum - owes_sum
        return user
