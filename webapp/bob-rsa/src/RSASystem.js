export const RSASystem = {
  logs: [],

  _log: function (message) {
    if (typeof message !== "string") {
      message = JSON.stringify(message);
    }
    const now = new Date();
    message = now.toLocaleTimeString() + ": " + message;
    this.logs.push(message);
  },

  generateKeys: function (p, q, e) {
    let n, eulerN, d;

    this._log(`Generating keys for p = ${p}, q = ${q}, e = ${e}`);

    if (!(this.isPrime(p) && this.isPrime(q))) {
      this._log(
        `Prime status:\np = ${p} : ${this.isPrime(
          p
        )} \nq = ${q} : ${this.isPrime(q)}\n`
      );
      return null;
    }

    // this._log(
    //   `Prime status:\np = ${p} : ${this.isPrime(p)} \nq = ${q} : ${this.isPrime(
    //     q
    //   )}\n`
    // );
    this._log(`p and q are prime numbers`);

    n = p * q;

    this._log(`n = p * q = ${n}`);

    let primes = this.getPrimeFactorisation(n);
    let expPrimes = this.getPrimeFactorisation(e);

    for (let integer of expPrimes) {
      if (n % integer === 0) {
        this._log("n and e are not relatively prime, choose different values");
        return null;
      }
    }

    eulerN = this.getEulerTotient(n, primes);

    this._log(`Euler's totient of n = ${n} is ${eulerN}`);

    for (let integer of expPrimes) {
      this._log(`Checking if ${integer} is a factor of ${eulerN}`);
      if (eulerN % integer === 0) {
        this._log(
          "phi(n) and e are not relatively prime, choose different values"
        );
        return null;
      }
    }

    d = this.getModularInverse(eulerN, e, n);
    
    this._log(`Finished key generation!`);
    
    return [n, e, d];
  },

  isPrime: function (e) {
    if (e < 2) return false;
    if (e === 2) return true;
    if (e % 2 === 0) return false;

    let square = Math.sqrt(e);
    for (let i = 3; i <= square; i++) {
      if (e % i === 0) return false;
    }

    return true;
  },

  getEulerTotient: function (n, factorsList) {
    this._log(`Finding Euler's totient of ${n} with factors ${factorsList}`);
    let totient = 1;
    if (factorsList.length === 1) {
      totient = factorsList[0] - 1;
      return totient;
    }

    let counter = 1;
    for (let i = 0; i < factorsList.length - 1; i++) {
      let curFact = factorsList[i],
        nextFact = factorsList[i + 1];

      if (curFact === nextFact) {
        counter++;
      } else {
        totient =
          counter === 1
            ? totient * (curFact - 1)
            : totient * (Math.pow(curFact, counter - 1) * (curFact - 1));
        counter = 1;
      }
    }

    let size = factorsList.length;
    if (factorsList[size - 2] === factorsList[size - 1]) {
      let fact = factorsList[size - 2];
      totient = totient * (Math.pow(fact, counter - 1) * (fact - 1));
    } else {
      totient = totient * (factorsList[size - 1] - 1);
    }

    return totient;
  },

  getPrimeFactorisation: function (n) {
    this._log(`Finding prime factors of ${n}`);
    let factorsList = [];
    let square = Math.sqrt(n);

    while (n % 2 === 0) {
      factorsList.push(2);
      n /= 2;
    }

    for (let i = 3; i <= square; i += 2) {
      while (n % i === 0) {
        factorsList.push(i);
        n /= i;
      }
    }

    if (n > 2) {
      factorsList.push(n);
    }

    this._log(`Prime factors of ${n} are ${factorsList}`);

    return factorsList;
  },

  getModularInverse: function (r0, r1, n) {
    this._log(`Finding modular inverse of ${r1} mod ${r0}`);

    let x0 = 1,
      x1 = 0,
      y0 = 0,
      y1 = 1;

    while (r1 !== 0) {
      let q = Math.floor(r0 / r1);
      [r0, r1] = [r1, r0 - r1 * q];
      [x0, x1] = [x1, x0 - x1 * q];
      [y0, y1] = [y1, y0 - y1 * q];
    }

    this._log(`Modular inverse of ${r1} mod ${r0} is ${y0}`);

    if (y0 < 0) {
      this._log(`Modular inverse of ${r1} mod ${r0} is negative, adding n (${n})`);
    }

    return y0 < 0 ? y0 + n : y0;
  },

  encrypt: function (m, n, e) {
    return this.squareLeftRightMultiply(m, e, n);
  },

  /**
   * 
   * @param {String} message 
   * @param {number} n Public Key
   * @param {number} [e=65537] Public Key Exponent 
   * @returns {Array} Encrypted message
   */
  encryptString: function (message, n, e=65537) {
    let encryptedMessage = [];
    for (let i = 0; i < message.length; i++) {
      let charCode = message.charCodeAt(i) - 64;
      console.log(charCode, " Charcode");
      let encryptedCharCode = this.encrypt(charCode, n, e);
      encryptedMessage.push(encryptedCharCode);
    }
    return encryptedMessage;
  },

  decrypt: function (c, n, e) {
    return this.squareLeftRightMultiply(c, e, n);
  },

  decryptMessage: function (message, n, d) {
    let decryptedMessage = "";
    for (let i = 0; i < message.length; i++) {
      let charCode = message[i];
      
      let decryptedCharCode = this.decrypt(charCode, n, d) + 64;
      decryptedMessage += String.fromCharCode(decryptedCharCode);
      console.log(this.decrypt(charCode, n, d));
    }
    return decryptedMessage;
  },

  squareLeftRightMultiply: function (b, e, n) {
    let r = 1;
    while (e !== 0) {
      if (e % 2 === 0) {
        e /= 2;
        b = (Math.pow(b, 2)) % n;
      } else {
        e = (e - 1) / 2;
        r = (r * b) % n;
        b = (Math.pow(b, 2)) % n;
      }
    }
    return r;
  },
};
