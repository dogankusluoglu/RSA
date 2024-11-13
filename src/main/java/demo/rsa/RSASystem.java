package demo.rsa;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RSASystem {

    public static void main(String[] args) {

        int keys[] = generateKeys(61, 67, 17);

        char msgAlph[] = (" MEET AT NINE ").toCharArray();
        int ciphers[] = new int[msgAlph.length];
        char decrypted[] = new char[msgAlph.length];

        System.out.println(" Encrypted message : ");
        for (int i = 0; i < msgAlph.length; i++) {
            int m = ((int) msgAlph[i]) - 64;
            ciphers[i] = encrypt(m, keys[0], keys[1]);
            System.out.print(ciphers[i] + " ");
        }
        System.out.println();

        System.out.println(" Decrypted message : ");
        for (int i = 0; i < msgAlph.length; i++) {
            int m = decrypt(ciphers[i], keys[0], keys[2]) + 64;
            char c = (char) m;
            decrypted[i] = c;
            System.out.print(decrypted[i] + " ");
        }
    }

    public static int[] generateKeys(int p, int q, int e) {

        int n, eulerN, d;

        if (!(isPrime(p) && isPrime(q))) {
            System.out.printf(" Prime status :\np = %d : %b \n ", p, isPrime(p));
            System.out.printf(" q = %d : %b \n ", q, isPrime(q));
            return null;
        }

        n = p * q;

        List< Integer> primes = getPrimeFactorisation(n);
        List< Integer> expPrimes = getPrimeFactorisation(e);

        for (Integer integer : expPrimes) {
            if (n % integer == 0) {
                System.out.println(" n and e are not relatively prime ,choose different values ");
                return null;
            }
        }

        eulerN = getEulerTotient(n, primes);

        for (Integer integer : expPrimes) {
            if (eulerN % integer == 0) {
                System.out.println(" phi ( n ) and e are not relatively prime , choose different values ");
                return null;
            }
        }

        d = getModularInverse(eulerN, e, n);

        int keys[] = {n, e, d};
        return keys;
    }

    public static boolean isPrime(int e) {
        if (e < 2) {
            return false;
        }
        if (e == 2) {
            return true;
        }
        if (e % 2 == 0) {
            return false;
        }

        int square = (int) Math.sqrt(e);

        for (int i = 3; i < square; i++) {
            if (e % i == 0) {
                return false;
            }
        }

        return true;
    }

    public static int getEulerTotient(int n, List< Integer> factorsList) {
        int totient = 1;

        // Calculate Euler ’s Totient
        if (factorsList.size() == 1) {
            totient = factorsList.get(0) - 1;
            return totient;
        }
        int counter = 1;

        for (int i = 0; i < factorsList.size() - 1; i++) {
            int curFact = factorsList.get(i), nextFact = factorsList.get(i + 1);

            if (curFact == nextFact) {
                counter++;
            } else {
                // Only one factor of this value
                if (counter == 1) {
                    totient = totient * (curFact - 1);
                } else { // Calc factor to the power of counter minus one times ( factor - 1)
                    totient = (int) (totient * (Math.pow(curFact,
                            counter - 1) * (curFact - 1)));
                }
                counter = 1;
            }
        }

        int size = factorsList.size();

        if (Objects.equals(factorsList.get(size - 2), factorsList.get(size - 1))) {
            int fact = factorsList.get(size - 2);
            totient = (int) (totient * (Math.pow(fact, counter - 1)
                    * (fact - 1)));
        } else {
            totient = totient * (factorsList.get(size - 1) - 1);
        }

        return totient;
    }

    public static List< Integer> getPrimeFactorisation(int n) {
        // Prime factorisation
        List< Integer> factorsList = new ArrayList<>();

        int square = (int) Math.sqrt(n);

        while (n % 2 == 0) {
            factorsList.add(2);
            n = n / 2;
        }

        for (int i = 3; i < square; i += 2) {
            while (n % i == 0) {
                factorsList.add(i);
                n = n / i;
            }
        }

        if (n > 2) {
            factorsList.add(n);
        }

        return factorsList;
    }

    public static int getModularInverse(int r0, int r1, int n) {
        int x0 = 1, x1 = 0, y0 = 0, y1 = 1;

        while (r1 != 0) {
            int q = Math.floorDiv(r0, r1);

            int rtmp = r1;
            r1 = r0 - (r1 * q);
            r0 = rtmp;

            int xtmp = x1;
            x1 = x0 - (x1 * q);
            x0 = xtmp;

            int ytmp = y1;
            y1 = y0 - (y1 * q);
            y0 = ytmp;
        }

        if (y0 < 0) {
            y0 += n;
        }

        return y0;
    }

    public static int encrypt(int m, int n, int e) {
        int c = squareLeftRightMultiply(m, e, n);

        return c;
    }

    public static int decrypt(int c, int n, int e) {
        int m = squareLeftRightMultiply(c, e, n);

        return m;
    }

    public static int squareLeftRightMultiply(int b, int e, int n) {
        int r = 1;
        while (e != 0) {
            if (e % 2 == 0) {
                e = e / 2;
                b = (int) Math.pow(b, 2) % n;
            } else {
                e = (e - 1) / 2;
                r = (r * b) % n;
                b = (int) Math.pow(b, 2) % n;
            }
        }
        return r;
    }
}