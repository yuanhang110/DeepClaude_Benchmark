class School:
    def __init__(self):
        self.students = {}  # Dictionary mapping grade numbers to lists of student names
        self.student_set = set()  # Set of all student names for quick duplicate checking
        self.additions = []  # Track addition success status

    def add_student(self, name, grade):
        """
        Add a student to a grade. If the student is already in the school, do nothing.
        """
        if name in self.student_set:
            self.additions.append(False)
            return "Student already exists"
        
        self.student_set.add(name)
        
        if grade not in self.students:
            self.students[grade] = []
        
        self.students[grade].append(name)
        self.students[grade].sort()  # Keep students alphabetically sorted within each grade
        self.additions.append(True)

    def roster(self):
        """
        Return a sorted list of all students in all grades.
        Grades are sorted numerically, and students within a grade are sorted alphabetically.
        """
        result = []
        for grade in sorted(self.students.keys()):
            result.extend(self.students[grade])
        return result

    def grade(self, grade_number):
        """
        Return a sorted list of all students in the given grade.
        """
        return self.students.get(grade_number, [])

    def added(self):
        """
        Return a list of booleans showing success of each addition attempt
        """
        return self.additions
