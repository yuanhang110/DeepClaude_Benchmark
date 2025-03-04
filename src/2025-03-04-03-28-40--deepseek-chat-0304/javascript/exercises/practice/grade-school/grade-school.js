export class GradeSchool {
  constructor() {
    this._roster = {};
  }

  roster() {
    // Return a deep copy of the roster sorted by grade and name
    const sortedRoster = {};
    Object.keys(this._roster).sort((a, b) => a - b).forEach(grade => {
      sortedRoster[grade] = [...this._roster[grade]].sort();
    });
    return sortedRoster;
  }

  add(name, grade) {
    // First remove the student from any existing grade
    for (const g in this._roster) {
      if (this._roster[g].includes(name)) {
        this._roster[g] = this._roster[g].filter(n => n !== name);
        if (this._roster[g].length === 0) {
          delete this._roster[g];
        }
        break;
      }
    }
    
    // Add to new grade
    if (!this._roster[grade]) {
      this._roster[grade] = [];
    }
    this._roster[grade].push(name);
  }

  grade(grade) {
    // Return a sorted copy of students for the grade, or empty array if none
    if (!this._roster[grade]) {
      return [];
    }
    return [...this._roster[grade]].sort();
  }
}
