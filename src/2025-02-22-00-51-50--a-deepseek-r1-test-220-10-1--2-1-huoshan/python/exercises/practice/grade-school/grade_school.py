class School:
    def __init__(self):
        self.students = set()
        self.grades = {}
        self.added_results = []

    def add_student(self, name, grade):
        if name in self.students:
            self.added_results.append(False)
            return
        self.students.add(name)
        if grade not in self.grades:
            self.grades[grade] = []
        self.grades[grade].append(name)
        self.grades[grade].sort()
        self.added_results.append(True)

    def roster(self):
        sorted_grades = sorted(self.grades.keys())
        result = []
        for grade in sorted_grades:
            result.extend(self.grades[grade])
        return result

    def grade(self, grade_number):
        return self.grades.get(grade_number, []).copy()

    def added(self):
        return self.added_results
