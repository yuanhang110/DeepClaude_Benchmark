//
// This is only a SKELETON file for the 'Promises' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export const promisify = (fn) => {
  return (...args) => {
    return new Promise((resolve, reject) => {
      fn(...args, (error, result) => {
        if (error) {
          reject(error);
        } else {
          resolve(result);
        }
      });
    });
  };
};

export const all = (promises = []) => {
  return new Promise((resolve, reject) => {
    const results = [];
    let completed = 0;
    if (promises.length === 0) {
      resolve(results);
      return;
    }
    promises.forEach((promise, index) => {
      Promise.resolve(promise).then((value) => {
        results[index] = value;
        completed++;
        if (completed === promises.length) {
          resolve(results);
        }
      }).catch(reject);
    });
  });
};

export const allSettled = (promises = []) => {
  return new Promise((resolve) => {
    const results = [];
    let completed = 0;
    if (promises.length === 0) {
      resolve(results);
      return;
    }
    const checkCompletion = () => {
      completed++;
      if (completed === promises.length) {
        resolve(results);
      }
    };
    promises.forEach((promise, index) => {
      Promise.resolve(promise).then((value) => {
        results[index] = value;
        checkCompletion();
      }).catch((reason) => {
        results[index] = reason;
        checkCompletion();
      });
    });
  });
};

export const race = (promises = []) => {
  return new Promise((resolve, reject) => {
    if (promises.length === 0) {
      resolve([]);
      return;
    }
    promises.forEach((promise) => {
      Promise.resolve(promise).then(resolve).catch(reject);
    });
  });
};

export const any = (promises = []) => {
  return new Promise((resolve, reject) => {
    const errors = [];
    let rejectedCount = 0;
    if (promises.length === 0) {
      resolve([]);
      return;
    }
    promises.forEach((promise, index) => {
      Promise.resolve(promise).then(resolve).catch((error) => {
        errors[index] = error;
        rejectedCount++;
        if (rejectedCount === promises.length) {
          reject(errors);
        }
      });
    });
  });
};
