//
// This is only a SKELETON file for the 'Grade School' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export class GradeSchool {
  #students = {};

  roster() {
    const result = {};
    // Get grades as numbers, sort them
    const grades = Object.keys(this.#students).map(Number).sort((a, b) => a - b);
    
    // Create a new object with sorted grades and their respective students
    for (const grade of grades) {
      // Return a copy of the array to prevent external modification
      result[grade] = [...this.#students[grade]];
    }
    
    return result;
  }

  add(name, grade) {
    // First, remove the student from any existing grade
    for (const gradeKey in this.#students) {
      const index = this.#students[gradeKey].indexOf(name);
      if (index !== -1) {
        this.#students[gradeKey].splice(index, 1);
      }
    }
    
    // Initialize the grade if it doesn't exist
    if (!this.#students[grade]) {
      this.#students[grade] = [];
    }
    
    // Add the student to the specified grade
    this.#students[grade].push(name);
    this.#students[grade].sort(); // Keep students sorted alphabetically
  }

  grade(gradeNumber) {
    // Return a copy of the array (or empty if grade doesn't exist)
    return [...(this.#students[gradeNumber] || [])];
  }
}
