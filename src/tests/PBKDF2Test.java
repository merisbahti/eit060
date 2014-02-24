package tests;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import org.junit.*;
import server.*;

public class PBKDF2Test extends junit.framework.TestCase {
  public void testCreateHash() {
    try {
      String hash = PBKDF2.generateHash("My hidden password!",PBKDF2.generateSalt());
    } catch (InvalidKeySpecException e) {
      fail("The keys specification is invalid. Wrong code or some bigger conspirecy");
    } catch (NoSuchAlgorithmException e) {
      fail("This computer does not support PBKDF2 med Hmac SHA1 and should therefor not be used as server.");
    }
    /*
     * Please note that this test is only used to
     * examine if the server supports out hash algorithms.
     */
    assertTrue(true);
  }
}
