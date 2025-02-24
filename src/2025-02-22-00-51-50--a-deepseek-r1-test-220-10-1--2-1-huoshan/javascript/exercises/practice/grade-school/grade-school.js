//
// This is only a SKELETON file for the 'Grade School' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export class GradeSchool {
  constructor() {
    this._students = new Map();
  }

  roster() {
    const sorted = {};
    // Get sorted grades and sort each grade's names
    Array.from(this._students.keys())
      .sort((a, b) => a - b)
      .forEach(grade => {
        sorted[grade] = this.grade(grade);
      });
    return sorted;
  }

  add(name, grade) {
    // Check if student exists and remove from current grade
    const currentGrade = this._findGrade(name);
    if (currentGrade !== undefined) {
      this._students.get(currentGrade).delete(name);
    }
    
    // Add to new grade
    if (!this._students.has(grade)) {
      this._students.set(grade, new Set());
    }
    this._students.get(grade).add(name);
  }

  grade(gradeNumber) {
    const grade = this._students.get(gradeNumber);
    return grade ? Array.from(grade).sort() : [];
  }

  _findGrade(name) {
    // Find which grade the student is in
    for (const [grade, students] of this._students.entries()) {
      if (students.has(name)) {
        return grade;
      }
    }
    return undefined;
  }
}
