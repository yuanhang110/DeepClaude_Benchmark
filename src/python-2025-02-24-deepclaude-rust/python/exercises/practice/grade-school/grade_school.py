class School:
    def __init__(self):
        self._student_grades = {}
        self._grade_students = {}
        self._added_list = []

    def add_student(self, name, grade):
        if name in self._student_grades:
            self._added_list.append(False)
            return
        self._student_grades[name] = grade
        if grade not in self._grade_students:
            self._grade_students[grade] = set()
        self._grade_students[grade].add(name)
        self._added_list.append(True)

    def roster(self):
        sorted_grades = sorted(self._grade_students.keys())
        result = []
        for grade in sorted_grades:
            students = sorted(self._grade_students[grade])
            result.extend(students)
        return result

    def grade(self, grade_number):
        students = self._grade_students.get(grade_number, set())
        return sorted(students)

    def added(self):
        return self._added_list
