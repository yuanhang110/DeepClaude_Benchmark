import json

class RestAPI:
    def __init__(self, database=None):
        self.users = {}
        if database:
            for user_data in database.get('users', []):
                name = user_data['name']
                self.users[name] = {
                    'owes': user_data.get('owes', {}),
                    'owed_by': user_data.get('owed_by', {})
                }

    def get(self, url, payload=None):
        if url == '/users':
            requested_names = []
            if payload:
                payload_data = json.loads(payload)
                requested_names = payload_data.get('users', [])
            else:
                requested_names = list(self.users.keys())
            users_list = []
            for name in sorted(requested_names):
                user = self.users.get(name)
                if user:
                    owes = {k: v for k, v in user['owes'].items()}
                    owed_by = {k: v for k, v in user['owed_by'].items()}
                    balance = sum(owed_by.values()) - sum(owes.values())
                    users_list.append({
                        'name': name,
                        'owes': owes,
                        'owed_by': owed_by,
                        'balance': balance
                    })
            users_list.sort(key=lambda x: x['name'])
            return json.dumps({'users': users_list})
        return json.dumps({})

    def post(self, url, payload=None):
        if url == '/add':
            data = json.loads(payload)
            user_name = data['user']
            if user_name not in self.users:
                self.users[user_name] = {'owes': {}, 'owed_by': {}}
            new_user = {
                'name': user_name,
                'owes': {},
                'owed_by': {},
                'balance': 0.0
            }
            return json.dumps(new_user)
        
        elif url == '/iou':
            data = json.loads(payload)
            lender = data['lender']
            borrower = data['borrower']
            amount = data['amount']
            
            lender_data = self.users.get(lender)
            borrower_data = self.users.get(borrower)
            if not lender_data or not borrower_data:
                return json.dumps({'users': []})

            # Calculate net balance between users
            existing_debt = borrower_data['owes'].get(lender, 0.0) - lender_data['owes'].get(borrower, 0.0)
            new_debt = existing_debt + amount
            
            # Clear existing entries
            if lender in borrower_data['owes']:
                del borrower_data['owes'][lender]
            if borrower in lender_data['owed_by']:
                del lender_data['owed_by'][borrower]
            if borrower in lender_data['owes']:
                del lender_data['owes'][borrower]
            if lender in borrower_data['owed_by']:
                del borrower_data['owed_by'][lender]
            
            if new_debt > 0:
                borrower_data['owes'][lender] = new_debt
                lender_data['owed_by'][borrower] = new_debt
            elif new_debt < 0:
                lender_data['owes'][borrower] = -new_debt
                borrower_data['owed_by'][lender] = -new_debt
            
            # Prepare response
            users = []
            for name in sorted([lender, borrower]):
                user = self.users[name]
                owes_total = sum(user['owes'].values())
                owed_total = sum(user['owed_by'].values())
                users.append({
                    'name': name,
                    'owes': user['owes'],
                    'owed_by': user['owed_by'],
                    'balance': owed_total - owes_total
                })
            
            return json.dumps({'users': users})
            
        return json.dumps({})
