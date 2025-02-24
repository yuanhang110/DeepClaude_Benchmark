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

export const all = (promises) => {
  return new Promise((resolve, reject) => {
    if (promises === undefined) {
      resolve(undefined);
      return;
    }
    if (!Array.isArray(promises)) {
      promises = [promises];
    }
    if (promises.length === 0) {
      resolve([]);
      return;
    }
    const results = new Array(promises.length);
    let completed = 0;
    
    promises.forEach((promise, index) => {
      Promise.resolve(promise)
        .then(result => {
          results[index] = result;
          completed++;
          if (completed === promises.length) {
            resolve(results);
          }
        })
        .catch(reject);
    });
  });
};

export const allSettled = (promises) => {
  return new Promise((resolve) => {
    if (promises === undefined) {
      resolve(undefined);
      return;
    }
    if (!Array.isArray(promises)) {
      promises = [promises];
    }
    if (promises.length === 0) {
      resolve([]);
      return;
    }
    const results = new Array(promises.length);
    let completed = 0;

    promises.forEach((promise, index) => {
      Promise.resolve(promise)
        .then(value => {
          results[index] = value;
          completed++;
          if (completed === promises.length) {
            resolve(results);
          }
        })
        .catch(error => {
          results[index] = error;
          completed++;
          if (completed === promises.length) {
            resolve(results);
          }
        });
    });
  });
};

export const race = (promises) => {
  return new Promise((resolve, reject) => {
    if (!Array.isArray(promises)) {
      promises = [promises];
    }
    if (promises.length === 0) {
      resolve([]);
      return;
    }
    promises.forEach(promise => {
      Promise.resolve(promise).then(resolve).catch(reject);
    });
  });
};

export const any = (promises) => {
  return new Promise((resolve, reject) => {
    if (promises === undefined) {
      resolve(undefined);
      return;
    }
    if (!Array.isArray(promises)) {
      promises = [promises];
    }
    if (promises.length === 0) {
      resolve([]);
      return;
    }
    const errors = new Array(promises.length);
    let rejectedCount = 0;

    promises.forEach((promise, index) => {
      Promise.resolve(promise)
        .then(resolve)
        .catch(error => {
          errors[index] = error;
          rejectedCount++;
          if (rejectedCount === promises.length) {
            reject(errors);
          }
        });
    });
  });
};
