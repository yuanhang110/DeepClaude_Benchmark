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
      Promise.resolve(promise).then(value => {
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
    
    const checkCompletion = () => {
      if (completed === promises.length) {
        resolve(results);
      }
    };
    
    promises.forEach((promise, index) => {
      Promise.resolve(promise).then(value => {
        results[index] = { status: 'fulfilled', value };
      }).catch(reason => {
        results[index] = { status: 'rejected', reason };
      }).finally(() => {
        completed++;
        checkCompletion();
      });
    });
    
    if (promises.length === 0) {
      resolve(results);
    }
  });
};

export const race = (promises = []) => {
  if (promises.length === 0) {
    return Promise.resolve([]);
  }
  return new Promise((resolve, reject) => {
    promises.forEach(promise => {
      Promise.resolve(promise).then(resolve).catch(reject);
    });
  });
};

export const any = (promises = []) => {
  return new Promise((resolve, reject) => {
    const errors = [];
    let rejected = 0;
    
    if (promises.length === 0) {
      reject(new AggregateError([], 'All promises were rejected'));
      return;
    }
    
    promises.forEach((promise, index) => {
      Promise.resolve(promise).then(resolve).catch(error => {
        errors[index] = error;
        rejected++;
        if (rejected === promises.length) {
          reject(new AggregateError(errors.filter(e => e !== undefined), 'All promises were rejected'));
        }
      });
    });
  });
};
