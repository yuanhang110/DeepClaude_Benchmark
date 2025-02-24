//
// This is only a SKELETON file for the 'Grade School' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export class GradeSchool {
  constructor() {
    this._roster = {};
    this._studentGrades = new Map();
  }

  roster() {
    const sortedGrades = Object.keys(this._roster)
      .map(Number)
      .sort((a, b) => a - b);
    
    const result = {};
    for (const grade of sortedGrades) {
      result[grade] = [...this._roster[grade]];
    }
    
    return result;
  }

  add(name, grade) {
    if (this._studentGrades.has(name)) {
      const previousGrade = this._studentGrades.get(name);
      const index = this._roster[previousGrade].indexOf(name);
      if (index > -1) {
        this._roster[previousGrade].splice(index, 1);
      }
      this._studentGrades.delete(name);
    }

    if (!this._roster[grade]) {
      this._roster[grade] = [];
    }
    
    this._roster[grade].push(name);
    this._roster[grade].sort((a, b) => a.localeCompare(b));
    this._studentGrades.set(name, grade);
  }

  grade(gradeNumber) {
    const students = this._roster[gradeNumber] || [];
    return [...students];
  }
}
