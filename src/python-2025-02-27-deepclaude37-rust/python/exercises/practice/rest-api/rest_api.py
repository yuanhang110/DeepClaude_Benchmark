class RestAPI:
    def __init__(self, database=None):
        self.database = database if database is not None else {'users': []}

    def get(self, url, payload=None):
        import json
        if url == '/users':
            requested_users = []
            if payload:
                payload_data = json.loads(payload)
                requested_users = payload_data.get('users', [])
            users = self.database['users']
            if requested_users:
                filtered_users = [u for u in users if u['name'] in requested_users]
            else:
                filtered_users = users.copy()
            sorted_users = sorted(filtered_users, key=lambda x: x['name'])
            response = []
            for user in sorted_users:
                owed = sum(user['owed_by'].values())
                owes = sum(user['owes'].values())
                response.append({
                    'name': user['name'],
                    'owes': user['owes'],
                    'owed_by': user['owed_by'],
                    'balance': owed - owes
                })
            return json.dumps({'users': response})
        return json.dumps({})

    def post(self, url, payload=None):
        import json
        if url == '/add':
            data = json.loads(payload)
            new_user = {
                'name': data['user'],
                'owes': {},
                'owed_by': {}
            }
            self.database['users'].append(new_user)
            return json.dumps({
                'name': new_user['name'],
                'owes': new_user['owes'],
                'owed_by': new_user['owed_by'],
                'balance': 0.0
            })
        elif url == '/iou':
            data = json.loads(payload)
            lender_name = data['lender']
            borrower_name = data['borrower']
            amount = data['amount']
            
            lender = next(u for u in self.database['users'] if u['name'] == lender_name)
            borrower = next(u for u in self.database['users'] if u['name'] == borrower_name)
            
            # Process IOU - handle existing debts between users
            if lender_name in borrower['owed_by']:
                owed = borrower['owed_by'][lender_name]
                if owed >= amount:
                    borrower['owed_by'][lender_name] -= amount
                    if borrower['owed_by'][lender_name] == 0:
                        del borrower['owed_by'][lender_name]
                    lender['owes'][borrower_name] -= amount
                    if lender['owes'][borrower_name] == 0:
                        del lender['owes'][borrower_name]
                else:
                    remaining = amount - owed
                    del borrower['owed_by'][lender_name]
                    if borrower_name in lender['owes']:
                        del lender['owes'][borrower_name]
                    borrower['owes'][lender_name] = borrower['owes'].get(lender_name, 0) + remaining
                    lender['owed_by'][borrower_name] = lender['owed_by'].get(borrower_name, 0) + remaining
            else:
                borrower['owes'][lender_name] = borrower['owes'].get(lender_name, 0) + amount
                lender['owed_by'][borrower_name] = lender['owed_by'].get(borrower_name, 0) + amount
            
            # Prepare response with updated user data
            users = [lender, borrower]
            users.sort(key=lambda x: x['name'])
            response = []
            for user in users:
                owed = sum(user['owed_by'].values())
                owes = sum(user['owes'].values())
                response.append({
                    'name': user['name'],
                    'owes': user['owes'],
                    'owed_by': user['owed_by'],
                    'balance': owed - owes
                })
            return json.dumps({'users': response})
        return json.dumps({})
