//
// This is only a SKELETON file for the 'Promises' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export const promisify = (fn) => {
  return function(...args) {
    return new Promise((resolve, reject) => {
      fn.call(this, ...args, (error, data) => {
        if (error) {
          reject(error);
        } else {
          resolve(data);
        }
      });
    });
  };
};

export const all = (promises = []) => {
  return new Promise((resolve, reject) => {
    if (promises.length === 0) {
      resolve([]);
      return;
    }
    const results = new Array(promises.length);
    let count = 0;
    promises.forEach((promise, index) => {
      Promise.resolve(promise).then((result) => {
        results[index] = result;
        count++;
        if (count === promises.length) {
          resolve(results);
        }
      }).catch(reject);
    });
  });
};

export const allSettled = (promises = []) => {
  return new Promise((resolve) => {
    if (promises.length === 0) {
      resolve([]);
      return;
    }
    const results = new Array(promises.length);
    let count = 0;
    const checkAllSettled = () => {
      if (count === promises.length) {
        resolve(results);
      }
    };
    promises.forEach((promise, index) => {
      Promise.resolve(promise).then(
        (value) => {
          results[index] = value;
          count++;
          checkAllSettled();
        },
        (reason) => {
          results[index] = reason;
          count++;
          checkAllSettled();
        }
      );
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
      Promise.resolve(promise).then(resolve, reject);
    });
  });
};

export const any = (promises = []) => {
  return new Promise((resolve, reject) => {
    if (promises.length === 0) {
      resolve([]);
      return;
    }
    const errors = new Array(promises.length);
    let errorCount = 0;
    promises.forEach((promise, index) => {
      Promise.resolve(promise).then(
        (value) => {
          resolve(value);
        },
        (reason) => {
          errors[index] = reason;
          errorCount++;
          if (errorCount === promises.length) {
            reject(errors);
          }
        }
      );
    });
  });
};
