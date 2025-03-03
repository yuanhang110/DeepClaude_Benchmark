class School:
    def __init__(self):
        self._roster = {}
        self._all_students = set()
        self._added_results = []

    def add_student(self, name, grade):
        if name in self._all_students:
            self._added_results.append(False)
            return
        
        if grade not in self._roster:
            self._roster[grade] = []
        
        self._roster[grade].append(name)
        self._all_students.add(name)
        self._added_results.append(True)

    def roster(self):
        result = []
        for grade in sorted(self._roster.keys()):
            result.extend(sorted(self._roster[grade]))
        return result

    def grade(self, grade_number):
        return sorted(self._roster.get(grade_number, []))

    def added(self):
        return self._added_results
