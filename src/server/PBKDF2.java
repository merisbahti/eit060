package server;

import java.util.UUID;
import java.security.SecureRandom;
import java.util.Random;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class PBKDF2{
  private static final int ITERATIONS = 1000;
  private static final int KEY_LENGTH = 192;

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

  public static byte[] generateSalt() {
    final Random r = new SecureRandom();
    byte[] salt = new byte[32];
    r.nextBytes(salt);
    return salt; 
  }

}
