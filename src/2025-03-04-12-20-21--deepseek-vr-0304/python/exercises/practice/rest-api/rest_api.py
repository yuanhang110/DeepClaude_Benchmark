import json

class RestAPI:
    def __init__(self, database=None):
        self.database = database if database is not None else {"users": []}

    def get(self, url, payload=None):
        if url == "/users":
            users = self.database["users"]
            if payload:
                payload = json.loads(payload)
                if "users" in payload:
                    requested = payload["users"]
                    filtered = [u for u in users if u["name"] in requested]
                else:
                    filtered = users.copy()
            else:
                filtered = users.copy()
            
            response = []
            for user in filtered:
                owed = sum(user["owed_by"].values())
                owes = sum(user["owes"].values())
                balance = owed - owes
                response.append({
                    "name": user["name"],
                    "owes": user["owes"].copy(),
                    "owed_by": user["owed_by"].copy(),
                    "balance": balance
                })
            sorted_users = sorted(response, key=lambda x: x["name"])
            return json.dumps({"users": sorted_users})
        return json.dumps({})

    def post(self, url, payload=None):
        if url == "/add":
            payload = json.loads(payload)
            name = payload["user"]
            new_user = {
                "name": name,
                "owes": {},
                "owed_by": {}
            }
            self.database["users"].append(new_user)
            return json.dumps({
                "name": name,
                "owes": {},
                "owed_by": {},
                "balance": 0.0
            })
        elif url == "/iou":
            payload = json.loads(payload)
            lender_name = payload["lender"]
            borrower_name = payload["borrower"]
            amount = payload["amount"]
            
            # Find users
            lender = next(u for u in self.database["users"] if u["name"] == lender_name)
            borrower = next(u for u in self.database["users"] if u["name"] == borrower_name)
            
            # Update lender's records
            current_net = lender["owed_by"].get(borrower_name, 0) - lender["owes"].get(borrower_name, 0)
            new_net = current_net + amount
            
            if new_net > 0:
                lender["owed_by"][borrower_name] = new_net
                if borrower_name in lender["owes"]:
                    del lender["owes"][borrower_name]
            elif new_net < 0:
                lender["owes"][borrower_name] = -new_net
                if borrower_name in lender["owed_by"]:
                    del lender["owed_by"][borrower_name]
            else:
                if borrower_name in lender["owed_by"]:
                    del lender["owed_by"][borrower_name]
                if borrower_name in lender["owes"]:
                    del lender["owes"][borrower_name]
            
            # Update borrower's records
            borrower_net = -new_net
            if borrower_net > 0:
                borrower["owes"][lender_name] = borrower_net
                if lender_name in borrower["owed_by"]:
                    del borrower["owed_by"][lender_name]
            elif borrower_net < 0:
                borrower["owed_by"][lender_name] = -borrower_net
                if lender_name in borrower["owes"]:
                    del borrower["owes"][lender_name]
            else:
                if lender_name in borrower["owed_by"]:
                    del borrower["owed_by"][lender_name]
                if lender_name in borrower["owes"]:
                    del borrower["owes"][lender_name]
            
            # Prepare response
            lender_owed = sum(lender["owed_by"].values())
            lender_owes = sum(lender["owes"].values())
            lender_balance = lender_owed - lender_owes
            
            borrower_owed = sum(borrower["owed_by"].values())
            borrower_owes = sum(borrower["owes"].values())
            borrower_balance = borrower_owed - borrower_owes
            
            response_users = [
                {
                    "name": lender_name,
                    "owes": lender["owes"].copy(),
                    "owed_by": lender["owed_by"].copy(),
                    "balance": lender_balance
                },
                {
                    "name": borrower_name,
                    "owes": borrower["owes"].copy(),
                    "owed_by": borrower["owed_by"].copy(),
                    "balance": borrower_balance
                }
            ]
            sorted_users = sorted(response_users, key=lambda x: x["name"])
            return json.dumps({"users": sorted_users})
        return json.dumps({})
