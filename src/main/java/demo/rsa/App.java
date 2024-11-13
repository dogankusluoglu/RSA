package demo.rsa;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Arrays;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.javalin.Javalin;

public class App {

    public static void main(String[] args) {
        Javalin app = Javalin.create(config -> {

            // Enable CORS for all origins (for the sake of the demo)
            config.bundledPlugins.enableCors(cors -> {
                cors.addRule(it -> {
                    it.anyHost();
                });
            });
        }
        ).start(7000);

        app.get("/", ctx -> ctx.result("Hello World"));
        int[] keys = getKeys();
        int n = keys[0];
        int e = keys[1];
        int d = keys[2];

        System.out.println("Public key : " + n + " " + e);
        System.out.println("We can't share this with anyone 🤭: " + d);

        app.get("/public-key", ctx -> {
            String json = String.format("{\"n\": %d, \"e\": %d}", n, e);
            ctx.result(json);
        });

        app.post("/decrypt", ctx -> {
            String encrypted = ctx.body();
            System.out.println("Encrypted: " + encrypted);
            String decrypted = "";
            for (int i = 0; i < encrypted.length(); i++) {
                int c = (int) encrypted.charAt(i);
                int m = RSASystem.decrypt(c, n, d);
                decrypted += (char) m;
            }

            String jsonDecryptInfo = String.format("{\"message\": \"%s\"}", decrypted);
            System.out.println(jsonDecryptInfo);

            ctx.result(jsonDecryptInfo);
        });

        app.post("/reversa", ctx -> {
            /**
             * Attributes required: - message: String (encrypted message) - the
             * public key to re-encrypt the message with before sending it back
             * (n, e)
             */
            String jsonString = ctx.body();
            
            ObjectMapper mapper = new ObjectMapper();
            EncryptedMessage rsaMessage = mapper.readValue(jsonString, EncryptedMessage.class);
            
            int[] encryptedMessage = rsaMessage.message;
            int clientN = rsaMessage.n;
            int clientE = rsaMessage.e;

            System.out.println("Encrypted: " + Arrays.toString(encryptedMessage));

            char[] decrypted = new char[encryptedMessage.length];

            // Decrypt the message
            for (int i = 0; i < encryptedMessage.length; i++) {
                int m = RSASystem.decrypt(encryptedMessage[i], n, d) + 64;
                char c = (char) m;
                decrypted[i] = c;
                System.out.println(decrypted[i] + " ");
            }

            System.out.println("Decrypted: " + Arrays.toString(decrypted));
            String mString = new String(decrypted);
            
            String reversed = new StringBuilder(mString).reverse().toString();
            System.out.println("Reversed: " + reversed);

            // Encrypt the reversed message
            int[] encryptedReversed = new int[reversed.length()];
            for (int i = 0; i < reversed.length(); i++) {
                int c = (int) reversed.charAt(i) - 64;
                int m = RSASystem.encrypt(c, clientN, clientE);
                encryptedReversed[i] = m;
            }

            ctx.result("{\"message\": " + Arrays.toString(encryptedReversed) + "}");

        });
    }

    public static int[] getKeys() {
        SecureRandom random = new SecureRandom();

        int p = BigInteger.probablePrime(7, random).intValue();
        int q = BigInteger.probablePrime(7, random).intValue();
        System.err.println("p: " + p);
        System.err.println("q: " + q);
        int e = 65537;

        return RSASystem.generateKeys(p, q, e);
    }

}
