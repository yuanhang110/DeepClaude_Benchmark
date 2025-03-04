class School:
    def __init__(self):
        self._roster = {}
        self._added_status = []

    def add_student(self, name, grade):
        # Check if student already exists in any grade
        for students in self._roster.values():
            if name in students:
                self._added_status.append(False)
                return False
                
        # Add student to grade
        if grade not in self._roster:
            self._roster[grade] = []
        self._roster[grade].append(name)
        self._roster[grade].sort()
        self._added_status.append(True)
        return True

    def roster(self):
        result = []
        for grade in sorted(self._roster.keys()):
            result.extend(self._roster[grade])
        return result

    def grade(self, grade_number):
        return self._roster.get(grade_number, []).copy()

    def added(self):
        return self._added_status.copy()
