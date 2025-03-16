class School:
    def __init__(self):
        self._students = {}  # Dictionary to store grade -> list of students
        self._added = []  # Track addition success status for each student
        self._students_by_name = set()  # Track all student names to prevent duplicates

    def add_student(self, name, grade):
        """
        Add a student to a grade. If the student already exists in any grade, 
        the operation fails and added() will record False.
        """
        # Create grade if it doesn't exist
        if grade not in self._students:
            self._students[grade] = []
        
        # Check if student exists in any grade
        if name in self._students_by_name:
            self._added.append(False)
            return
        
        # Add student and keep the list sorted
        self._students[grade].append(name)
        self._students[grade].sort()
        self._students_by_name.add(name)
        self._added.append(True)

    def roster(self):
        """
        Return a sorted list of all students in all grades.
        Sort by grade (ascending) then by student name (alphabetically).
        """
        result = []
        # Get grades in ascending order
        for grade in sorted(self._students.keys()):
            result.extend(self._students[grade])
        return result

    def grade(self, grade_number):
        """
        Return a sorted list of all students in the specified grade.
        """
        # Return a copy of the list to prevent external modification
        return self._students.get(grade_number, []).copy()

    def added(self):
        """
        Return list of success statuses for all add_student operations.
        """
        return self._added
