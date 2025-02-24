import json

class RestAPI:
    def __init__(self, database=None):
        self.database = database if database is not None else {"users": []}

    def get(self, url, payload=None):
        if url == "/users":
            users = self.database["users"]
            requested_users = json.loads(payload)["users"] if payload else []
            filtered_users = [u for u in users if not requested_users or u["name"] in requested_users]
            sorted_users = sorted(filtered_users, key=lambda x: x["name"])
            for user in sorted_users:
                user["balance"] = sum(user["owed_by"].values()) - sum(user["owes"].values())
            return json.dumps({"users": sorted_users})
        return json.dumps({"error": "Not found"})

    def post(self, url, payload=None):
        if not payload:
            return json.dumps({"error": "Payload required"})
        data = json.loads(payload)
        if url == "/add":
            user_name = data["user"]
            if any(user["name"] == user_name for user in self.database["users"]):
                return json.dumps({"error": "User already exists"})
            new_user = {
                "name": user_name,
                "owes": {},
                "owed_by": {},
                "balance": 0.0
            }
            self.database["users"].append(new_user)
            return json.dumps(new_user)
        elif url == "/iou":
            lender = data["lender"]
            borrower = data["borrower"]
            amount = data["amount"]
            users = self.database["users"]
            
            lender_data = next((u for u in users if u["name"] == lender), None)
            borrower_data = next((u for u in users if u["name"] == borrower), None)
            
            if not lender_data or not borrower_data:
                return json.dumps({"error": "User not found"})
            
            # Update borrower's owes and lender's owed_by
            borrower_data["owes"][lender] = borrower_data["owes"].get(lender, 0.0) + amount
            lender_data["owed_by"][borrower] = lender_data["owed_by"].get(borrower, 0.0) + amount

            # Calculate net between borrower and lender
            b_owes_l = borrower_data["owes"].get(lender, 0.0)
            l_owes_b = lender_data["owes"].get(borrower, 0.0)
            net = b_owes_l - l_owes_b

            if net > 0:
                # Borrower owes lender
                borrower_data["owes"][lender] = net
                lender_data["owed_by"][borrower] = net
                # Remove reverse entries
                if borrower in lender_data["owes"]:
                    del lender_data["owes"][borrower]
                if lender in borrower_data["owed_by"]:
                    del borrower_data["owed_by"][lender]
            elif net < 0:
                # Lender owes borrower
                lender_data["owes"][borrower] = -net
                borrower_data["owed_by"][lender] = -net
                # Remove forward entries
                if lender in borrower_data["owes"]:
                    del borrower_data["owes"][lender]
                if borrower in lender_data["owed_by"]:
                    del lender_data["owed_by"][borrower]
            else:
                # Remove all entries
                if lender in borrower_data["owes"]:
                    del borrower_data["owes"][lender]
                if borrower in lender_data["owed_by"]:
                    del lender_data["owed_by"][borrower]
                if borrower in lender_data["owes"]:
                    del lender_data["owes"][borrower]
                if lender in borrower_data["owed_by"]:
                    del borrower_data["owed_by"][lender]

            # Clean up any zero amounts
            for user in [lender_data, borrower_data]:
                for other in list(user["owes"].keys()):
                    if user["owes"][other] == 0.0:
                        del user["owes"][other]
                for other in list(user["owed_by"].keys()):
                    if user["owed_by"][other] == 0.0:
                        del user["owed_by"][other]

            # Prepare response
            response_users = sorted([lender_data, borrower_data], key=lambda x: x["name"])
            # Calculate balances for response
            for user in response_users:
                user["balance"] = sum(user["owed_by"].values()) - sum(user["owes"].values())
            return json.dumps({"users": response_users})
        return json.dumps({"error": "Not found"})
