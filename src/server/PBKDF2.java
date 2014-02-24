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
 * Class with static methods for creation of PBKDF2 hashed strings.
 */
public class PBKDF2{
  private static final int ITERATIONS = 1000;
  private static final int KEY_LENGTH = 192;

  /*
   * Create hash, in form of a string
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
   * Generate random 32 bytes salt
   * SecureRandom is being used, it's safe as fuck
   * since it uses the word secure in its name.
   */
  public static byte[] generateSalt() {
    final Random r = new SecureRandom();
    byte[] salt = new byte[32];
    r.nextBytes(salt);
    System.out.println("Generated salt: "+new String(salt));
    return salt; 
  }

}
