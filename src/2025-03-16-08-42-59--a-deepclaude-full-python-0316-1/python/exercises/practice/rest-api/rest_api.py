import json

class RestAPI:
    def __init__(self, database=None):
        self.database = database if database else {"users": []}

    def get(self, url, payload=None):
        if url == "/users":
            if payload:
                data = json.loads(payload)
                requested_users = data["users"]
                users = [user for user in self.database["users"] if user["name"] in requested_users]
            else:
                users = self.database["users"]
            return json.dumps({"users": users})
        return json.dumps({})

    def post(self, url, payload=None):
        if url == "/add":
            data = json.loads(payload)
            new_user = {
                "name": data["user"],
                "owes": {},
                "owed_by": {},
                "balance": 0.0
            }
            self.database["users"].append(new_user)
            return json.dumps(new_user)
        
        elif url == "/iou":
            data = json.loads(payload)
            lender_name = data["lender"]
            borrower_name = data["borrower"]
            amount = data["amount"]
            
            # Find users
            lender = next(user for user in self.database["users"] if user["name"] == lender_name)
            borrower = next(user for user in self.database["users"] if user["name"] == borrower_name)
            
            # Check if borrower already owes lender
            if lender_name in borrower["owes"]:
                borrower_owes = borrower["owes"][lender_name]
                if amount <= borrower_owes:
                    # Reduce existing debt
                    borrower["owes"][lender_name] -= amount
                    lender["owed_by"][borrower_name] -= amount
                    
                    # Remove if zero
                    if borrower["owes"][lender_name] == 0:
                        del borrower["owes"][lender_name]
                        del lender["owed_by"][borrower_name]
                else:
                    # Debt reversal
                    del borrower["owes"][lender_name]
                    del lender["owed_by"][borrower_name]
                    
                    difference = amount - borrower_owes
                    lender["owes"][borrower_name] = difference
                    borrower["owed_by"][lender_name] = difference
            # Check if lender already owes borrower
            elif borrower_name in lender["owes"]:
                lender_owes = lender["owes"][borrower_name]
                lender["owes"][borrower_name] += amount
                borrower["owed_by"][lender_name] += amount
            else:
                # New debt
                if borrower_name not in lender["owed_by"]:
                    lender["owed_by"][borrower_name] = 0
                if lender_name not in borrower["owes"]:
                    borrower["owes"][lender_name] = 0
                
                lender["owed_by"][borrower_name] += amount
                borrower["owes"][lender_name] += amount
            
            # Update balances
            lender["balance"] = sum(lender["owed_by"].values()) - sum(lender["owes"].values())
            borrower["balance"] = sum(borrower["owed_by"].values()) - sum(borrower["owes"].values())
            
            # Clean up empty dicts
            for user in [lender, borrower]:
                if not user["owes"]:
                    user["owes"] = {}
                if not user["owed_by"]:
                    user["owed_by"] = {}
            
            return json.dumps({"users": sorted([lender, borrower], key=lambda x: x["name"])})
