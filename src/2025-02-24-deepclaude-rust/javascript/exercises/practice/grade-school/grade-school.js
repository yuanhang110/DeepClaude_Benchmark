//
// This is only a SKELETON file for the 'Grade School' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export class GradeSchool {
  constructor() {
    this._grades = {};
    this._studentGrades = {};
  }

  roster() {
    const sortedGrades = Object.keys(this._grades).sort((a, b) => parseInt(a) - parseInt(b));
    const result = {};
    sortedGrades.forEach(grade => {
      result[grade] = [...this._grades[grade]];
    });
    return result;
  }

  add(name, grade) {
    // If student exists in a different grade, remove them first
    if (this._studentGrades[name] !== undefined) {
      const oldGrade = this._studentGrades[name];
      this._grades[oldGrade] = this._grades[oldGrade].filter(student => student !== name);
      // Clean up empty grade arrays
      if (this._grades[oldGrade].length === 0) {
        delete this._grades[oldGrade];
      }
    }
    
    // Add to new grade
    if (!this._grades[grade]) {
      this._grades[grade] = [];
    }
    this._grades[grade].push(name);
    this._grades[grade].sort();
    this._studentGrades[name] = grade;
  }

  grade(grade) {
    const students = this._grades[grade] || [];
    return [...students];
  }
}
