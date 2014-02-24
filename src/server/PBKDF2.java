package server;

import java.util.UUID;
import java.security.SecureRandom;
import java.util.Random;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/*
 * Klass med endast statiska metoder för att skapa PBKDF2 hashade strängar.
 */
public class PBKDF2{
  private static final int ITERATIONS = 1000;
  private static final int KEY_LENGTH = 192;

  /*
   * Skapa hashen (i form av en sträng).
   */
  public static String generateHash(String pw, byte[] saltBytes) throws InvalidKeySpecException, NoSuchAlgorithmException{
    char[] pwChars = pw.toCharArray();
    PBEKeySpec spec = new PBEKeySpec(
        pwChars,
        saltBytes,
        ITERATIONS,
        KEY_LENGTH);
    SecretKeyFactory key = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    byte[] hash = key.generateSecret(spec).getEncoded();
    System.out.println("Generated hash: "+String.format("%x", new BigInteger(hash)));
    return String.format("%x", new BigInteger(hash));
  }

  public static String generateHashWithSaltString(String pw, String saltString) throws InvalidKeySpecException, NoSuchAlgorithmException {
    return generateHash(pw, saltString.getBytes());
  }

  /*
   * generera random 32 BYTES salt.
   * vi använder här SecureRandom, den är säker
   * eftersom den använder secure i namnet
   */
  public static byte[] generateSalt() {
    final Random r = new SecureRandom();
    byte[] salt = new byte[32];
    r.nextBytes(salt);
    System.out.println("Generated salt: "+new String(salt));
    return salt; 
  }

}
