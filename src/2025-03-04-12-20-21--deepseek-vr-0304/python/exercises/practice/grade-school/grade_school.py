class School:
    def __init__(self):
        self.students = {}
        self.all_students = set()
        self.added_results = []

    def add_student(self, name, grade):
        if name in self.all_students:
            self.added_results.append(False)
            return
        if grade not in self.students:
            self.students[grade] = []
        self.students[grade].append(name)
        self.students[grade].sort()
        self.all_students.add(name)
        self.added_results.append(True)

    def roster(self):
        roster_list = []
        for grade in sorted(self.students.keys()):
            roster_list.extend(self.students[grade])
        return roster_list

    def grade(self, grade_number):
        return self.students.get(grade_number, []).copy()

    def added(self):
        return self.added_results
